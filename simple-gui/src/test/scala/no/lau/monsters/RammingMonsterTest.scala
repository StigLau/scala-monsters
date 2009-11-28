package no.lau.monsters

import org.junit.Test
import no.lau.movement.Location
import no.lau.domain._

class RammingMonsterTest {
  @Test
  def RamTest {
    val game = new GameImpl(3, 3) {
      override def createBoarder() { /* Do not create border */ }
    }

    val rammstein = new Monster(game) with Meelee {
      game.currentGameBoard += Location(3, 3) -> this
      override def toString = "R"
    }
    val victim = new Monster(game) with Player with Mortal {
      game.currentGameBoard += Location(0, 0) -> this
      override def toString = "V"
    }

    for (step <- 0 to 5) {
      println(game.printableBoard)
      val direction = rammstein.findPathTo(rammstein.prioritizedEnemy.get).get
      println("Going " + direction)
      rammstein.move(direction)
    }
  }
}