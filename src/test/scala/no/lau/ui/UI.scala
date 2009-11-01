package no.lau.domain

import java.awt.Font
import scala.swing._
import javax.swing.{InputMap, SwingUtilities, JComponent, KeyStroke, ActionMap}
import no.lau.monsters.RammingMonster
import no.lau.movement._

/**
 * @author: beiske
 * @author: StigLau
 */

object UI extends SimpleGUIApplication {
  var game:Game = null
  var directionHub:AsymmetricGamingInterface = null

   def top = new MainFrame {
     contents = new BorderPanel {
       add(gameBoard, BorderPanel.Position.Center)
     }
   }

  val gameBoard = new TextArea() {
    editable = false

    populateInputMap(peer.getInputMap(JComponent.WHEN_FOCUSED))
    populateActionMap(peer.getActionMap(), directionHub)
    peer.setFont(new Font("Monospaced", peer.getFont().getStyle(), 24));
  }
  
  def printGameBoard(): Unit = {gameBoard.text = game printableBoard}

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

  override def main(args: Array[String]) = {
    game = SetupGame.myCharacter(printGameBoard)
    directionHub = SetupGame.directionHub

    
    SwingUtilities.invokeLater {
      new Runnable {def run() {init(); top.pack(); top.visible = true}}
    }
  }
}


object SetupGame {
  val game = Game(40, 25)
  val directionHub = new AsymmetricGamingInterface(game, monsterGunnar)
  val monsterGunnar = new Monster(game, "MonsterGunnar") with StackableMovement with Mortal with Pusher {
    override def toString = ""
  }

  def myCharacter(callback: () => Unit) = {
    val rnd = new scala.util.Random
    1 to 1 foreach {arg => game.addRandomly(Block(game, "a" + rnd.nextInt()))}
    //1 to 10 foreach {arg => game.addRandomly(Monster(game, "monster" + rnd.nextInt()))}


    game.addRandomly(monsterGunnar)

    val clock = new VerySimpleClock(game, 200, callback)

    val rammstein = new RammingMonster(game, "Rammstein, the ramming monster") //with Pusher
    {
      override def kill() {
        super.kill()
        isKilled = true
        println("I'm meeelting!")
        clock.removeTickListener(this)
      }
    }
    game.addRandomly(rammstein)
    clock.addTickListener(rammstein)

    val rammy = new RammingMonster(game, "Rammy, the ramming monster") {
      override def kill() {
        super.kill()
        isKilled = true
        println("I'm meeelting!")
        clock.removeTickListener(this)
      }
    }
    game.addRandomly(rammy)
    clock.addTickListener(rammy)

    directionHub.start
    clock.start

    game
  }
}