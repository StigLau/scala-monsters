import no.lau.domain._
import org.junit.Assert._
import org.junit.Test
import no.lau.domain.movement.{Up, Down, Right, Left}

class CascadingMovementTest {
  val game = new Game(3, 2) {
    currentGameBoard += (1, 1) -> Block(this, "a")
    currentGameBoard += (2, 1) -> Block(this, "b")
    currentGameBoard += (2, 0) -> StaticWall()
  }
  val currentGameBoard = game.currentGameBoard

  @Test def monsterMovingBlocksTest() {
    val leif = new Monster(game, "MonsterLeif") {game.currentGameBoard += (1, 0) -> this}
    println(game printableBoard())
    leif move (Up)
    assertEquals (game printableBoard(),
      ".B..\n" +
      ".HB.\n" +
      "..W.\n")

    println(game printableBoard())
    leif move (Right)
    assertEquals (game printableBoard(),
      ".B..\n" +
      "..HB\n" +
      "..W.\n")
    println(game printableBoard())
    leif move (Up)
    println(game printableBoard())
    leif move (Left)
    println(game printableBoard())
    assertEquals (game printableBoard(),
      "BH..\n" +
      "...B\n" +
      "..W.\n")
  }

  @Test def cascadingMovementTest() {
    val leif = new Monster(game, "MonsterLeif") {currentGameBoard += (0, 1) -> this}
    leif move (Right)
    assertEquals (game printableBoard(),
      "....\n" +
      ".HBB\n" +
      "..W.\n")
  }
  //Do something about the IllegalMoveException
  @Test def erronousMovementRightOverTheBoarder() {
    val leif = new Monster(game, "MonsterLeif") {currentGameBoard += (0, 1) -> this}
    leif move (Right)
    try {leif move (Right)}
    catch {case ime: IllegalMoveException =>}
  }

  @Test def erronousMovementDownOverTheBoarder() {
    val leif = new Monster(game, "MonsterLeif") {currentGameBoard += (1, 2) -> this}
    leif move (Down)
    try {leif move (Down)}
    catch {case ime: IllegalMoveException =>}
  }

  @Test def illegalMovementIntoStaticWall() {
    val leif = new Monster(game, "MonsterLeif") {currentGameBoard += (2, 2) -> this}
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

    val leif = new Monster(game, "MonsterLeif") {currentGameBoard += (0, 0) -> this}
    val offer = new Monster(game, "Offer") {currentGameBoard += (2, 0) -> this}

    assertEquals ("HBHB\n", game printableBoard())
    leif move (Right)
    assertEquals (".HBB\n", game printableBoard())
  }

  @Test def squeezingMonsterAgainstThinAirFails() {
    val game = new Game(4, 0) {
      currentGameBoard += (1, 0) -> Block(this, "a")
      currentGameBoard += (4, 0) -> Block(this, "b")
    }
    val currentGameBoard = game.currentGameBoard
    
    val leif = new Monster(game, "MonsterLeif") {currentGameBoard += (0, 0) -> this}
    val offer = new Monster(game, "Offer") {currentGameBoard += (2, 0) -> this}

    assertEquals ("HBH.B\n", game printableBoard())
    try {leif move (Right)}
    catch {case ime: IllegalMoveException =>}
  }
}

class ClockedMovementTest {
  @Test def asyncronousMovementStacksUpMovementAndWaitsForTicks() {
    val game = new Game(3, 0)
    val currentGameBoard = game.currentGameBoard

    val leif = new Monster(game, "MonsterLeif") {currentGameBoard += (0, 0) -> this}

    assertEquals ("H...\n", game printableBoard())
    leif stackMovement (Right)
    leif stackMovement (Right)
    leif stackMovement (Right)
    assertEquals ("H...\n", game printableBoard())
    game.newTurn
    assertEquals (".H..\n", game printableBoard())
    game.newTurn
    assertEquals ("..H.\n", game printableBoard())
    game.newTurn
    assertEquals ("...H\n", game printableBoard())
  }
}