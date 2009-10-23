import no.lau.domain.movement.{Up, Direction}

trait UserRepositoryComponent {
  val userRepository: UserRepository
  class UserRepository {
    def move(direction:Direction) = {
      println("Moving " + direction)
    }
  }
}

// using self-type annotation declaring the dependencies this
// component requires, in our case the UserRepositoryComponent
trait UserServiceComponent {
  this: UserRepositoryComponent =>
  val userService: UserService
  class UserService {
    def move(direction:Direction) = userRepository.move(direction)
  }
}

object ComponentRegistry extends UserServiceComponent with UserRepositoryComponent {
  val userRepository = new UserRepository
  val userService = new UserService
}

object DItest {
  def main(args: Array[String]) {
    val woot = ComponentRegistry
    val userService = ComponentRegistry.userService
    userService.move(Up)
  }
}


