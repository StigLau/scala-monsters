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

    1 to 20 foreach {arg => game.addRandomly(Block(game, "block" + rnd.nextInt()))}
    1 to 2 foreach {arg => game.addRandomly(Monster(game, "monster" + rnd.nextInt()))}


    val monsterGunnar = new Monster(game, "MonsterGunnar")
    game.addRandomly(monsterGunnar)
    val directionHub = new AsymmetricGamingInterface(game, monsterGunnar)
    directionHub.start
    new KeyboardHandler(directionHub).start

    new VerySimpleClock(game, 1000).start
  }
}

import actors.Actor
import actors.Actor._
class AsymmetricGamingInterface(game: Game, movable: Movable) extends Actor {
  def act() {
    loop {
      try {
        react {
          case direction: Direction => movable.move(direction)
          case _ => println("No direction")
        }
      } catch {
        case ime: IllegalMoveException => println("Illegal Move!! " + ime.getMessage)
      }
    }
  }
}

class KeyboardHandler(gamingInterface: AsymmetricGamingInterface) extends Actor {
  def act() {
    loop {
      val reader = new InputStreamReader(System.in)
      val keyInput = reader.read match {
        case 'w' => Up
        case 'a' => Left
        case 's' => Down
        case 'd' => Right
        case 'q' => println("Game ended"); exit()
        case _ => println("Instructions: Use w, a, s, d keys to move. Enter to accept. q to exit")
      }
      if (keyInput.isInstanceOf[Direction]) gamingInterface ! keyInput
    }
  }
}

class VerySimpleClock(game:Game, time:Long) extends Actor {
  def act() {
    loop {
      reactWithin(time) {
        case _ => game.newTurn; println(game printableBoard)
      }
    }
  }
}

