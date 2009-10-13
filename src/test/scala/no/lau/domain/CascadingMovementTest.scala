import no.lau.domain._
import Direction.{Up, Down, Left, Right}
import org.junit.Assert._
import org.junit.Test

class CascadingMovementTest {
  val game = new Game(3, 2) {
    gameBoard += (1, 1) -> Block(this, "a")
    gameBoard += (2, 1) -> Block(this, "b")
    gameBoard += (2, 0) -> StaticWall()
  }
  val gameBoard = game.gameBoard

  @Test def monsterMovingBlocksTest() {
    val leif = new Monster(game, "MonsterLeif") {
      game.gameBoard += (1, 0) -> this
    }
    game printBoard ()
    leif move (Up)
    assertEquals((1, 1), game.whereIs(leif))
    assertEquals((1, 2), game.whereIs(Block(game, "a")))
    game printBoard ()
    leif move (Right)
    assertEquals((2, 1), game.whereIs(leif))
    assertEquals((3, 1), game.whereIs(Block(game, "b")))
    game printBoard ()
    leif move (Up)
    game printBoard ()
    leif move (Left)
    game printBoard ()
    assertEquals((1, 2), game.whereIs(leif))
    assertEquals((0, 2), game.whereIs(Block(game, "a")))
  }

  @Test def cascadingMovementTest() {
    val leif = new Monster(game, "MonsterLeif") {
      game.gameBoard += (0, 1) -> this
    }
    game printBoard ()
    leif move (Right)
    game printBoard ()
    assertEquals(leif, gameBoard(1, 1))
    assertEquals(Block(game, "a"), gameBoard(2, 1))
    assertEquals(Block(game, "b"), gameBoard(3, 1))
  }
  //Do something about the IllegalMoveException
  @Test def erronousMovementOverTheBoarder() {
    val leif = new Monster(game, "MonsterLeif") {
      gameBoard += (0, 1) -> this
    }
    game printBoard()
    leif move (Right)
    leif move (Right)
  }

  @Test def illegalMovementIntoStaticWall() {
    val leif = new Monster(game, "MonsterLeif") {
      gameBoard += (2, 2) -> this
    }
    game printBoard ()
    leif move (Down)
  }
}