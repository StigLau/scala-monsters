import no.lau.domain.movement.{Up, Direction}

trait GameComponent {
  val game: Game
  class Game {
    def move(direction:Direction) = {
      println("Moving " + direction)
    }
  }
}

// using self-type annotation declaring the dependencies this
// component requires, in our case the GameComponent
trait MovableComponent {
  this: GameComponent => val movable: Movable
  class Movable {
    def move(direction:Direction) = game.move(direction)
  }
}

object ComponentRegistry extends MovableComponent with GameComponent {
  val game = new Game
  val movable = new Movable
}

object DItest {
  def main(args: Array[String]) {
    val movable = ComponentRegistry.movable
    movable.move(Up)
  }
}


