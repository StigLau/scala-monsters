package no.lau.domain

import java.awt.Font
import scala.swing._
import javax.swing.{InputMap, JComponent, KeyStroke, ActionMap}
import no.lau.monsters.RammingMonster
import no.lau.movement._

/**
 * @author: beiske
 * @author: StigLau
 */

object UI extends SimpleGUIApplication {
  var game =  GameConfiguration.myGame(printGameBoard)
  var directionHub = GameConfiguration.directionHub

   def top = new MainFrame {
     contents = new BorderPanel {
       add(gameBoard, BorderPanel.Position.Center)
     }
   }

  val gameBoard = new TextArea() {
    editable = false

    KeyStrokeHandler.populateInputMap(peer.getInputMap(JComponent.WHEN_FOCUSED))
    KeyStrokeHandler.populateActionMap(peer.getActionMap(), directionHub)
    peer.setFont(new Font("Monospaced", peer.getFont().getStyle(), 24));
  }

  def printGameBoard(): Unit = {gameBoard.text = game printableBoard}
}

object KeyStrokeHandler {
  def populateInputMap(inputMap: InputMap) {
    inputMap.put(KeyStroke.getKeyStroke("UP"), "up")
    inputMap.put(KeyStroke.getKeyStroke("DOWN"), "down")
    inputMap.put(KeyStroke.getKeyStroke("LEFT"), "left")
    inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "right")
  }

  def populateActionMap(actionMap: ActionMap, gamingInterface: AsymmetricGamingInterface) {
    actionMap.put("up", Action("KeyPressed") {gamingInterface ! Up}.peer)
    actionMap.put("down", Action("KeyPressed") {gamingInterface ! Down}.peer)
    actionMap.put("left", Action("KeyPressed") {gamingInterface ! Left}.peer)
    actionMap.put("right", Action("KeyPressed") {gamingInterface ! Right}.peer)
  }
}

object GameConfiguration {
  val game = Game(40, 25)
  val player = new Monster(game, "MonsterGunnar") with StackableMovement with Mortal with Pusher {
    override def toString = ""
  }
  val directionHub = new AsymmetricGamingImpl(game, player)

  def myGame(callback: () => Unit) = {
    val clock = new VerySimpleClock(game, 200, callback)

    val rnd = new scala.util.Random
    1 to 10 foreach {arg => game.addRandomly(Block(game, "a" + rnd.nextInt()))}
    1 to 10 foreach { arg =>
              val monster = new RammingMonster(game, "Monster " + rnd.nextInt()) {
                override def kill() {
                  super.kill()
                  clock.removeTickListener(this)
                }
              }
              game.addRandomly(monster)
              clock.addTickListener(monster)
    }
    game.addRandomly(player)

    directionHub.start
    clock.start
    game
  }
}

object GUIStarter {
  def main(args: Array[String]) = UI.main(null)
}