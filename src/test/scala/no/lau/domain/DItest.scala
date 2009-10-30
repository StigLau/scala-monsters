import no.lau.movement.{Up, Direction}



// =======================
// service interfaces
trait GameTrait {
  def move(direction:Direction)
}

// =======================
// service implementations
class GameImpl extends GameTrait {
  def move(direction:Direction) = println("move " + direction)
}

// =======================
// service declaring two dependencies that it wants injected
class Monster2(implicit val game: GameTrait) {
  def move(direction:Direction) = game.move(direction)
}

// =======================
// import the services into the current scope and the wiring
// is done automatically using the implicits
import Services._

// =======================
// instantiate the services in a module
object Services {
  implicit val gameimpl = new GameImpl
}




object DItest {
  def main(args: Array[String]) {
    val monster = new Monster2
    monster.move(Up)
  }
}


