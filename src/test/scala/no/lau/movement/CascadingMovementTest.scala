package no.lau.movement

import no.lau.domain._
import org.junit.Assert._
import org.junit.Test
import no.lau.monsters.RammingMonster

class CascadingMovementTest {
  val game = new Game(3, 2) {
    currentGameBoard += Location(1, 1) -> new Block(this)
    currentGameBoard += Location(2, 1) -> new Block(this)
    currentGameBoard += Location(2, 0) -> new StaticWall()
    override def createBoarder() {/* Do not create border */ }
  }
  val currentGameBoard = game.currentGameBoard

  @Test def monsterMovingBlocksTest() {
    val leif = new Monster(game) with Movable with Pusher {game.currentGameBoard += Location(1, 0) -> this}
    println(game printableBoard)
    leif move (Up)
    assertEquals (game printableBoard,
      ".B..\n" +
      ".HB.\n" +
      "..W.\n")

    println(game printableBoard)
    leif move (Right)
    assertEquals (game printableBoard,
      ".B..\n" +
      "..HB\n" +
      "..W.\n")
    println(game printableBoard)
    leif move (Up)
    println(game printableBoard)
    leif move (Left)
    println(game printableBoard)
    assertEquals (game printableBoard,
      "BH..\n" +
      "...B\n" +
      "..W.\n")
  }

  //@Test moving two blocks is not implemented yet
  def cascadingMovementTest() {
    val leif = new Monster(game) with Movable with Pusher {
      currentGameBoard += Location(0, 1) -> this}
    leif move (Right)
    assertEquals (game printableBoard,
      "....\n" +
      ".HBB\n" +
      "..W.\n")
  }

  //@Test moving two blocks is not implemented yet
  def erronousMovementPushTwoBlocksRightOverTheBoarder() {
    val leif = new Monster(game) with Movable with Pusher {currentGameBoard += Location(0, 1) -> this}
    leif move (Right)
    try {leif move (Right)}
    catch {case ime: IllegalMoveException =>}
  }

  @Test def illegalMovementIntoStaticWall() {
    val leif = new Monster(game) with Movable {currentGameBoard += Location(2, 2) -> this}
    try {leif move (Down)}
    catch {case ime: IllegalMoveException =>}
  }
}


class SqueezingTest {
  @Test def squeezingMonster() {
    val game = new Game(4, 0) {
        currentGameBoard += Location(1, 0) -> new Block(this)
        currentGameBoard += Location(3, 0) -> new Block(this)
        override def createBoarder() {/* Do not create border */ }
      }
      val currentGameBoard = game.currentGameBoard

    val pusher = new Monster(game) with Movable with Pusher {
      currentGameBoard += Location(0, 0) -> this
      override def toString = "P"
    }
    val victim = new Monster(game) with Movable with Mortal {
      currentGameBoard += Location(2, 0) -> this
      override def toString = "V"
    }

    assertEquals ("PBVB.\n", game printableBoard)
    pusher move (Right)
    assertEquals (".PBB.\n", game printableBoard)
  }

  @Test def squeezingMonsterAgainstThinAirFails() {
    val game = new Game(4, 0) {
      currentGameBoard += Location(1, 0) -> new Block(this)
      currentGameBoard += Location(4, 0) -> new Block(this)
      override def createBoarder() {/* Do not create border */ }
    }
    val currentGameBoard = game.currentGameBoard

    val leif = new Monster(game) with Movable {currentGameBoard += Location(0, 0) -> this}
    val offer = new Monster(game) with Movable {currentGameBoard += Location(2, 0) -> this}

    assertEquals ("HBH.B\n", game printableBoard)
    try {leif move (Right)}
    catch {case ime: IllegalMoveException =>}
  }
}

class ClockedMovementTest {
  @Test def asyncronousMovementStacksUpMovementAndWaitsForTicks() {
    val game = new Game(3, 0)
    val currentGameBoard = game.currentGameBoard

    val leif = new Monster(game) with QueuedMovement {currentGameBoard += Location(0, 0) -> this}

    assertEquals ("H...\n", game printableBoard)
    leif queueMovement (Right)
    leif queueMovement (Right)
    leif queueMovement (Right)
    assertEquals ("H...\n", game printableBoard)
    game.newTurn
    assertEquals (".H..\n", game printableBoard)
    game.newTurn
    assertEquals ("..H.\n", game printableBoard)
    game.newTurn
    assertEquals ("...H\n", game printableBoard)

    game.newTurn
    assertEquals ("...H\n", game printableBoard)
  }

  @Test
  def stackableMovement() {
    val game = new Game(1, 1)
    val monsterGunnar = new Monster(game) with QueuedMovement {game.currentGameBoard += Location(0, 0) -> this}
    game.printableBoard
    monsterGunnar.queueMovement(Up)
    monsterGunnar.queueMovement(Right)
    assertEquals (game printableBoard, "..\nH.\n")
    game.newTurn
    assertEquals (game printableBoard, "H.\n..\n")
    game.newTurn
    assertEquals (game printableBoard, ".H\n..\n")
    ""
  }

  @Test
  def RamTest {
    val game = new Game(3, 3) {
      override def createBoarder() {/* Do not create border */ }
    }

    val rammstein = new RammingMonster(game) with Meelee {
      game.currentGameBoard += Location(3, 3) -> this
      override def toString = "R"
    }
    val victim = new Monster(game) with Player with Mortal {
      game.currentGameBoard += Location(0, 0) -> this
      override def toString = "V"
    }

    for ( step <- 0 to 5) {
      println(game.printableBoard)
      val direction = rammstein.findPathTo(rammstein.prioritizedEnemy.get).get
      println("Going " + direction)
      rammstein.move(direction)
    }
  }
}