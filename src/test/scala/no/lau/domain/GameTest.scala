package no.lau.domain

import no.lau.domain.movement.{Up, Down, Right, Left}

/**
 * Test used for setting up and testing that the game holds together. 
 */

object GameTest {
  def main(args: Array[String]) {
    val game = Game(20, 10)

    1 to 2 foreach {arg => game.addRandomly(Block(game, "a"))}
    println(game printableBoard)

    //1 to 4 foreach { arg => game.addRandomly(new Monster() {})  }
    1 to 1 foreach {arg => game.addRandomly(Block(game, "a"))}
    println(game printableBoard)


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
    game.newTurn
    println(game printableBoard)

    game.newTurn    
    monsterGunnar move (Right)
    println(game printableBoard)

    println(monsterGunnar whereAreYou)
    println("Game ended")
  }
}

