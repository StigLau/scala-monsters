package no.lau.domain

import no.lau.domain.movement._

import java.awt.Font

import scala.swing._
import javax.swing.{InputMap, SwingUtilities, JComponent, KeyStroke, ActionMap}

/**
 * Created by IntelliJ IDEA.
 * User: beiske
 */

object UI extends SimpleGUIApplication {
   val game = Game(40, 25)
   val rnd = new scala.util.Random
   1 to 100 foreach {arg => game.addRandomly(Block(game, "a" + rnd.nextInt()))}
   1 to 2 foreach {arg => game.addRandomly(Monster(game, "monster" + rnd.nextInt()))}

   val monsterGunnar = new Monster(game, "MonsterGunnar")
   game.addRandomly(monsterGunnar)
   val gameBoard = new TextArea(){
     editable = false
     text = game printableBoard

     populateInputMap(peer.getInputMap(JComponent.WHEN_FOCUSED))
     populateActionMap(monsterGunnar, peer.getActionMap())
     peer.setFont(new Font("Monospaced", peer.getFont().getStyle(), 24));
   }

   def top = new MainFrame {
     contents = new BorderPanel {
       add(gameBoard, BorderPanel.Position.Center)
     }
   }


  def populateInputMap(inputMap: InputMap) {
    inputMap.put(KeyStroke.getKeyStroke("UP"), "up")
    inputMap.put(KeyStroke.getKeyStroke("DOWN"), "down")
    inputMap.put(KeyStroke.getKeyStroke("LEFT"), "left")
    inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "right")
    inputMap.put(KeyStroke.getKeyStroke("typed w"), "up")
    inputMap.put(KeyStroke.getKeyStroke("typed s"), "down")
    inputMap.put(KeyStroke.getKeyStroke("typed a"), "left")
    inputMap.put(KeyStroke.getKeyStroke("typed d"), "right")
  }

  def populateActionMap(movable:Movable, actionMap: ActionMap) {
    actionMap.put("up", Action("KeyPressed") {move(movable, Up)}.peer)
    actionMap.put("down", Action("KeyPressed") {move(movable, Down)}.peer)
    actionMap.put("left", Action("KeyPressed") {move(movable, Left)}.peer)
    actionMap.put("right", Action("KeyPressed") {move(movable, Right)}.peer)
  }

  def move(movable: Movable, direction: Direction) {
    movable.move(direction)
    gameBoard.text = game printableBoard
  }

  override def main(args: Array[String]) = {
    SwingUtilities.invokeLater {
      new Runnable {def run() {init(); top.pack(); top.visible = true}}
    }
  }
}

