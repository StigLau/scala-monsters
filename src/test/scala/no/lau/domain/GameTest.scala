package no.lau.domain

import no.lau.domain.movement.{Up, Down, Right, Left}

/**
 * Test used for setting up and testing that the game holds together. 
 */

object GameTest {
  def main(args: Array[String]) {
    val game = Game(40, 25)

    var newGameBoard = game.newTurn
    1 to 300 foreach {arg => game.addRandomly(Block(game, "a"))}
    println(game boardAsPrintable)

    //1 to 4 foreach { arg => game.addRandomly(new Monster() {})  }
    game.newTurn
    1 to 100 foreach {arg => game.addRandomly(Block(game, "a"))}
    println(game boardAsPrintable)


    //val playerLeif =  Player("Leif")
    //val playerKurt = Player("Kurt")
    //game.addRandomly(playerKurt)
    //game.addRandomly(playerLeif)


    //playerLeif.move(Direction.Up)
    //playerLeif.move(Direction.Right)

    val monsterGunnar = new Monster(game, "MonsterGunnar")
    game.addRandomly(monsterGunnar)

    println(monsterGunnar whereAreYou)
    monsterGunnar move (Up)
    monsterGunnar move (Right)
    println(monsterGunnar whereAreYou)
    println("Game ended")
  }
}

