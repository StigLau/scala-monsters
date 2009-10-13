import no.lau.domain._

object CascadingMovementTest {
  def main(args: Array[String]) {
    val game1 = new Game(3, 2) {
      gameBoard += (0, 1) -> new StaticWall()
      gameBoard += (1, 1) -> Block(this, "a")
      gameBoard += (2, 1) -> Block(this, "b")
    }

    val leif = new Monster(game1, "MonsterLeif") {
      game.gameBoard += (1, 0) -> this
    }

    import Direction.{Up, Down, Left, Right}
    game1 printBoard ()
    leif move (Up)
    game1 printBoard ()
    leif move (Right)
    game1 printBoard ()
    leif move (Up)
    game1 printBoard ()
    leif move (Left)
    game1 printBoard ()
  }
}