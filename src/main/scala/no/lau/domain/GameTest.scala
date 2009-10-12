package no.lau.domain

import Direction.{Up, Down, Left, Right}


/**
 * Test used for setting up and testing that the game holds together. 
 */
/*
object GameTest {
  def main(args: Array[String]) {
    val game = Game(20, 40)

    1 to 240 foreach {arg => game.addRandomly(Block(game))}
    //1 to 4 foreach { arg => game.addRandomly(new Monster() {})  }


    //val playerLeif =  Player("Leif")
    //val playerKurt = Player("Kurt")
    //game.addRandomly(playerKurt)
    //game.addRandomly(playerLeif)

    game.printBoard()

    //playerLeif.move(Direction.Up)
    //playerLeif.move(Direction.Right)

    val monsterGunnar = new Monster(game)

    println(monsterGunnar whereAreYou)
    monsterGunnar move (Up)
    monsterGunnar move (Right)
    println(monsterGunnar whereAreYou)
    println("Game ended")
  }
}        */

//Something fishy happening on the last move!
object CascadingMovementTest {
  def main(args: Array[String]) {
    val game1 = new Game(3, 2) {
      gameBoard += (0, 1) -> StaticWall()
      gameBoard += (1, 1) -> Block(this)
      gameBoard += (2, 1) -> Block(this)
    }

    val leif = new Monster(game1) {
      game.gameBoard += (1, 0) -> this
    }

    game1 printBoard ()
    leif move (Up)
    game1 printBoard ()
    leif move (Right)
    game1 printBoard ()
    leif move (Up)
    game1 printBoard ()
    leif move (Left)
    game1 printBoard ()


    println("\n" +
            "Board end state should look like this:\n" +
            "BH..\n" +
            "W..B\n" +
            "....\n" +
            "The Highlander syndrome was here, and eats up all my B's!")
  }
}