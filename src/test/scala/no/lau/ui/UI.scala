package no.lau.domain

import java.awt.Font
import scala.swing._
import javax.swing.{InputMap, JComponent, KeyStroke, ActionMap}
import no.lau.movement._
import no.lau.predefined.{Config, LevelEasyC}

/**
 * @author: beiske
 * @author: StigLau
 */

object UI extends SimpleGUIApplication {
  val config:Config = new Config() {
    val game = Game(40, 25)
    val player = new Monster(game, "MonsterGunnar") with Player with StackableMovement with Mortal with Pusher 
  }
  val directionHub = new AsymmetricGamingImpl(config.game, config.player)
  val clock = new VerySimpleClock(config.game, 200, printGameBoardCallback)
  config.gameConfig(clock, LevelEasyC.level)
  directionHub.start
  clock.start

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

  def printGameBoardCallback(): Unit = {gameBoard.text = config.game.printableBoard}
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


object GUIStarter {
  def main(args: Array[String]) = UI.main(null)
}