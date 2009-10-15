import no.lau.domain._
import org.junit.Assert._
import org.junit.Test
import no.lau.domain.movement.{Up, Down, Right, Left}

class CascadingMovementTest {
  val game = new Game(3, 2) {
    gameBoard += (1, 1) -> Block(this, "a")
    gameBoard += (2, 1) -> Block(this, "b")
    gameBoard += (2, 0) -> StaticWall()
  }
  val gameBoard = game.gameBoard

  @Test def monsterMovingBlocksTest() {
    val leif = new Monster(game, "MonsterLeif") {game.gameBoard += (1, 0) -> this}
    leif move (Up)
    assertEquals (game boardAsPrintable(),
      ".B..\n" +
      ".HB.\n" +
      "..W.\n")
    leif move (Right)
    assertEquals (game boardAsPrintable(),
      ".B..\n" +
      "..HB\n" +
      "..W.\n")
    leif move (Up)
    leif move (Left)
    assertEquals (game boardAsPrintable(),
      "BH..\n" +
      "...B\n" +
      "..W.\n")
  }

  @Test def cascadingMovementTest() {
    val leif = new Monster(game, "MonsterLeif") {gameBoard += (0, 1) -> this}
    leif move (Right)
    assertEquals (game boardAsPrintable(),
      "....\n" +
      ".HBB\n" +
      "..W.\n")
  }
  //Do something about the IllegalMoveException
  @Test def erronousMovementRightOverTheBoarder() {
    val leif = new Monster(game, "MonsterLeif") {gameBoard += (0, 1) -> this}
    leif move (Right)
    try {leif move (Right)}
    catch {case ime: IllegalMoveException =>}
  }

  @Test def erronousMovementDownOverTheBoarder() {
    val leif = new Monster(game, "MonsterLeif") {gameBoard += (1, 2) -> this}
    leif move (Down)
    try {leif move (Down)}
    catch {case ime: IllegalMoveException =>}
  }

  @Test def illegalMovementIntoStaticWall() {
    val leif = new Monster(game, "MonsterLeif") {gameBoard += (2, 2) -> this}
    try {leif move (Down)}
    catch {case ime: IllegalMoveException =>}
  }
}


class SqueezingTest {
  val game = new Game(3, 0) {
    gameBoard += (1, 0) -> Block(this, "a")
    gameBoard += (3, 0) -> Block(this, "b")
  }
  val gameBoard = game.gameBoard

  //todo implement squeezing
  @Test def squeezingMonster() {
    val leif = new Monster(game, "MonsterLeif") {gameBoard += (0, 0) -> this}
    val offer = new Monster(game, "Offer") {gameBoard += (2, 0) -> this}

    assertEquals (game boardAsPrintable(),"HBHB\n")
    leif move (Right)
    assertEquals (game boardAsPrintable(),".HBB\n")
  }
}