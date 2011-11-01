package no.lau.monsters

import scala.swing._
import javax.swing.{InputMap, JComponent, KeyStroke, ActionMap}
import no.lau.movement._
import no.lau.predefined.{LevelEasyE, LevelEasyC, LevelEasyB, Config}
import java.awt.{Dimension, Font}

/**
 * @author: beiske
 * @author: StigLau
 */

object MonsterGameUI extends SimpleSwingApplication {
  import no.lau.domain._

   def top = new MainFrame {
     size = new Dimension(400, 300)
     contents = new BorderPanel {
       add(gameBoard, BorderPanel.Position.Center)
     }
   }

  val config = startGame
  val directionHub = new AsymmetricGamingImpl(config.player)
  directionHub.start

  val gameBoard = new TextArea {
    editable = false

    KeyStrokeHandler.populateInputMap(peer.getInputMap(JComponent.WHEN_FOCUSED))
    KeyStrokeHandler.populateActionMap(peer.getActionMap(), directionHub)
    peer.setFont(new Font("Monospaced", peer.getFont().getStyle(), 24));
  }

  def printGameBoardCallback(): Unit = {gameBoard.text = config.game.printableBoard}

  def startGame() = {
    val config: Config = new LevelEasyE() {
      val game:Game = new GameImpl(40, 23)
      val player = new Monster(game) with Player with QueuedMovement with Mortal with Pusher
    }
    val clock = new VerySimpleClock(config.game, 200, printGameBoardCallback)
    config.gameConfig(clock)
    clock.start
    config
  }
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