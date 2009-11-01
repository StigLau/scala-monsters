package no.lau.domain

import java.io.{InputStreamReader}
import actors.Actor
import actors.Actor.loop
import no.lau.movement._

/**
 * Test used for setting up and testing that the game holds together. 
 */

object AsciDemo {
  def main(args: Array[String]) {
    val game = Game(20, 10)
    val rnd = new scala.util.Random

    1 to 20 foreach {arg => game.addRandomly(Block(game, "block" + rnd.nextInt()))}
    1 to 2 foreach {arg => game.addRandomly(new Monster(game, "monster" + rnd.nextInt()) with StackableMovement)}


    val monsterGunnar = new Monster(game, "MonsterGunnar") with StackableMovement
    game.addRandomly(monsterGunnar)
    val directionHub = new AsymmetricGamingImpl(game, monsterGunnar)
    directionHub.start
    new KeyboardHandler(directionHub).start

    def printGameBoard(): Unit = println(game printableBoard); 
    new VerySimpleClock(game, 1000, printGameBoard).start
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


