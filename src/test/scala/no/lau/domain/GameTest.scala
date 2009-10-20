package no.lau.domain

import java.io.{InputStreamReader}
import no.lau.domain.movement._

/**
 * Test used for setting up and testing that the game holds together. 
 */

object GameTest {
  def main(args: Array[String]) {
    val game = Game(20, 10)
    val rnd = new scala.util.Random

    1 to 100 foreach {arg => game.addRandomly(Block(game, "a" + rnd.nextInt()))}


    val monsterGunnar = new Monster(game, "MonsterGunnar")
    game.addRandomly(monsterGunnar)



    //This is the walking around part
    var continue = true
    while (continue) {
      println(game printableBoard)
      val reader = new InputStreamReader(System.in)
      val value = reader.read match {
        case 'w' => Up
        case 'a' => Left
        case 's' => Down
        case 'd' => Right
        case 'q' => continue = false
        case _ => println("Instructions: Use w, a, s, d keys to move. Enter to accept. q to exit")
      }
      try {
        value match {
          case direction: Direction => {
            monsterGunnar.move(direction)
            game.newTurn
          }
          case _ => println("No direction")
        }
      } catch {
        case ime: IllegalMoveException => println("Illegal Move!! " + ime.getMessage)
      }
    }
    println("Game ended")
  }
}