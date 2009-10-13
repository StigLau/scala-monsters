import no.lau.domain._
import Direction.{Up, Down, Left, Right}
import org.junit.Assert._
import org.junit.Test

class CascadingMovementTest {
  val game = new Game(3, 2) {
    gameBoard += (0, 1) -> StaticWall()
    gameBoard += (1, 1) -> Block(this, "a")
    gameBoard += (2, 1) -> Block(this, "b")
  }

  val leif = new Monster(game, "MonsterLeif") {
    game.gameBoard += (1, 0) -> this
  }

  @Test def cascadingMovementTest() {
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
    assertEquals((0, 1), game.whereIs(StaticWall()))
    assertEquals((0, 2), game.whereIs(Block(game, "a")))
  }
}