package no.lau.movement

import no.lau.domain._
import org.junit.Assert._
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(classOf[JUnit4])
class CascadingMovementTest {

  val game = new GameImpl(3, 2) {
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
    val game = new GameImpl(4, 0) {
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
    val game = new GameImpl(4, 0) {
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
  @Test
   def asyncronousMovementStacksUpMovementAndWaitsForTicks() {
    val game = new GameImpl(3, 0) { override def createBoarder() {/* Do not create border */ } }
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
    val game = new GameImpl(1, 1) { override def createBoarder() {/* Do not create border */ } }
    val monsterGunnar = new Monster(game) with QueuedMovement {game.currentGameBoard += Location(0, 0) -> this}
    game.printableBoard
    monsterGunnar.queueMovement(Up)
    monsterGunnar.queueMovement(Right)
    assertEquals (game printableBoard, "..\nH.\n")
    game.newTurn
    assertEquals (game printableBoard, "H.\n..\n")
    game.newTurn
    assertEquals (game printableBoard, ".H\n..\n")
  }
}