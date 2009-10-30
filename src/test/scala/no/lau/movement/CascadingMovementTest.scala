package no.lau.movement

import no.lau.domain._
import org.junit.Assert._
import org.junit.Test
import no.lau.monsters.RammingMonster

class CascadingMovementTest {
  val game = new Game(3, 2) {
    currentGameBoard += (1, 1) -> Block(this, "a")
    currentGameBoard += (2, 1) -> Block(this, "b")
    currentGameBoard += (2, 0) -> StaticWall()
  }
  val currentGameBoard = game.currentGameBoard

  @Test def monsterMovingBlocksTest() {
    val leif = new Monster(game, "MonsterLeif") with Movable with Pusher {game.currentGameBoard += (1, 0) -> this}
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
    val leif = new Monster(game, "MonsterLeif") with Movable with Pusher {
      currentGameBoard += (0, 1) -> this}
    leif move (Right)
    assertEquals (game printableBoard,
      "....\n" +
      ".HBB\n" +
      "..W.\n")
  }

  //@Test moving two blocks is not implemented yet
  def erronousMovementPushTwoBlocksRightOverTheBoarder() {
    val leif = new Monster(game, "MonsterLeif") with Movable with Pusher {currentGameBoard += (0, 1) -> this}
    leif move (Right)
    try {leif move (Right)}
    catch {case ime: IllegalMoveException =>}
  }

  @Test def erronousMovementDownOverTheBoarder() {
    val leif = new Monster(game, "MonsterLeif") with Movable with Pusher {currentGameBoard += (1, 2) -> this}
    leif move (Down)
    try {leif move (Down)}
    catch {case ime: IllegalMoveException =>}
  }

  @Test def illegalMovementIntoStaticWall() {
    val leif = new Monster(game, "MonsterLeif") with Movable {currentGameBoard += (2, 2) -> this}
    try {leif move (Down)}
    catch {case ime: IllegalMoveException =>}
  }
}


class SqueezingTest {
  @Test def squeezingMonster() {
    val game = new Game(3, 0) {
        currentGameBoard += (1, 0) -> Block(this, "a")
        currentGameBoard += (3, 0) -> Block(this, "b")
      }
      val currentGameBoard = game.currentGameBoard

    val pusher = new Monster(game, "Pusher") with Movable with Pusher {
      currentGameBoard += (0, 0) -> this
      override def toString = "P"
    }
    val victim = new Monster(game, "Victim") with Movable with Mortal {
      currentGameBoard += (2, 0) -> this
      override def toString = "V"
    }

    assertEquals ("PBVB\n", game printableBoard)
    pusher move (Right)
    assertEquals (".PBB\n", game printableBoard)
  }

  @Test def squeezingMonsterAgainstThinAirFails() {
    val game = new Game(4, 0) {
      currentGameBoard += (1, 0) -> Block(this, "a")
      currentGameBoard += (4, 0) -> Block(this, "b")
    }
    val currentGameBoard = game.currentGameBoard
    
    val leif = new Monster(game, "MonsterLeif") with Movable {currentGameBoard += (0, 0) -> this}
    val offer = new Monster(game, "Offer") with Movable {currentGameBoard += (2, 0) -> this}

    assertEquals ("HBH.B\n", game printableBoard)
    try {leif move (Right)}
    catch {case ime: IllegalMoveException =>}
  }
}

class ClockedMovementTest {
  @Test def asyncronousMovementStacksUpMovementAndWaitsForTicks() {
    val game = new Game(3, 0)
    val currentGameBoard = game.currentGameBoard

    val leif = new Monster(game, "MonsterLeif") with StackableMovement {currentGameBoard += (0, 0) -> this}

    assertEquals ("H...\n", game printableBoard)
    leif stackMovement (Right)
    leif stackMovement (Right)
    leif stackMovement (Right)
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
    val monsterGunnar = new Monster(game, "MonsterLeif") with StackableMovement {game.currentGameBoard += (0, 0) -> this}
    game.printableBoard
    monsterGunnar.stackMovement(Up)
    monsterGunnar.stackMovement(Right)
    assertEquals (game printableBoard, "..\nH.\n")
    game.newTurn
    assertEquals (game printableBoard, "H.\n..\n")
    game.newTurn
    assertEquals (game printableBoard, ".H\n..\n")
    ""
  }

  @Test
  def crossingOverBoarderOrIllegalMovementHaltsFurtherProgression() {
    val game = new Game(0, 0)
    val monsterGunnar = new Monster(game, "MonsterLeif") with StackableMovement {game.currentGameBoard += (0, 0) -> this}
    game.printableBoard
    monsterGunnar.stackMovement(Up)
    monsterGunnar.stackMovement(Right)
    assertEquals(2, monsterGunnar.movementStack.size)
    game.newTurn
    assertEquals(0, monsterGunnar.movementStack.size)
    ""
  }

  @Test
  def RamTest {
    val game = new Game(3, 3)

    val rammstein = new RammingMonster(game, "Rammstein, the ramming monster") with Meelee {
      game.currentGameBoard += (3, 3) -> this
      override def toString = "R"
    }
    val victim = new Monster(game, "Victim") with Mortal {
      game.currentGameBoard += (0, 0) -> this
      override def toString = "V"
    }

    for ( step <- 0 to 5) {
      println(game.printableBoard)
      val direction = rammstein.findPathTo(rammstein.enemies.head)
      println("Going " + direction)
      rammstein.move(direction)
    }
  }
}