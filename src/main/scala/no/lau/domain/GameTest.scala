package no.lau.domain

import Direction.{Up, Down, Left, Right}


/**
 * Test used for setting up and testing that the game holds together. 
 */
object GameTest {
  def main(args: Array[String]) {
    val game = Game(20, 40)

    1 to 240 foreach {arg => game.addRandomly(Block(game))}
    //1 to 4 foreach { arg => game.addRandomly(new Monster() {})  }


    val playerLeif = Player("Leif")
    val playerKurt = Player("Kurt")
    game.addRandomly(playerKurt)
    game.addRandomly(playerLeif)

    game.printBoard()

    //playerLeif.move(Direction.Up)
    //playerLeif.move(Direction.Right)

    val monsterGunnar = new Monster(game)

    println(monsterGunnar whereAreYou)
    monsterGunnar move (Up)
    monsterGunnar move (Right)
    println(monsterGunnar whereAreYou)
    "Game ended"
  }
}

object CascadingMovementTest {
  def main(args: Array[String]) {

    val game = new Game(4, 4) {
      gameBoard(0)(1) = Block(this)
      gameBoard(1)(1) = Block(this)
      gameBoard(2)(1) = Block(this)
    }

    val leif = new Monster(game) {
      game.gameBoard(1)(0) = this
    }

    println("He is " + game.whereIs(leif))

    game printBoard()
    leif move(Up)
    game printBoard()
  }
}