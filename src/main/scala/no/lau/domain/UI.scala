package no.lau.domain

import java.io.{InputStreamReader}
import no.lau.domain.movement._

import scala.actors.Actor
import scala.actors.Actor._


import scala.swing._

/**
 * Created by IntelliJ IDEA.
 * User: beiske
 * Date: Oct 19, 2009
 * Time: 3:27:26 PM
 * To change this template use File | Settings | File Templates.
 */

object UI extends SimpleGUIApplication {
   val game = Game(40, 25)
   val rnd = new scala.util.Random
   1 to 100 foreach {arg => game.addRandomly(Block(game, "a" + rnd.nextInt()))}
   val monsterGunnar = new Monster(game, "MonsterGunnar")
   game.addRandomly(monsterGunnar)
   val board = new TextArea(){
     editable = false
     text = game printableBoard
   }
   
   def top = new MainFrame {
     contents = new BorderPanel {
       import BorderPanel.Position._
       add(board, Center)
       KeyboardHandler.start
     }
   }
   def refresh() = board text = game printableBoard
}

object KeyboardHandler extends Actor {
  def act(){
     while (true) {
       val reader = new InputStreamReader(System.in)
       val value = reader.read match {
        case 'w' => Up
        case 'a' => Left
        case 's' => Down
        case 'd' => Right
        case _ => println("Instructions: Use w, a, s, d keys to move.")
      }
      try {
        value match {
          case direction: Direction => {
            UI.monsterGunnar.move(direction)
            UI.game.newTurn
            UI.refresh()
          }
          case _ => println("No direction")
        }
      } catch {
        case ime: IllegalMoveException => println("Illegal Move!! " + ime.getMessage)
      }
    }
  }
}     
  
  
