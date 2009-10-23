case class User(username:String, password:String)

trait UserRepositoryComponent {
  val userRepository = new UserRepository
  class UserRepository {
    def authenticate(user: User): User = {
      println("authenticating user: " + user)
      user
    }

    def create(user: User) = println("creating user: " + user)

    def delete(user: User) = println("deleting user: " + user)
  }
}

// using self-type annotation declaring the dependencies this
// component requires, in our case the UserRepositoryComponent
trait UserServiceComponent {
  this: UserRepositoryComponent =>
  val userService = new UserService
  class UserService {
    def authenticate(username: String, password: String): User = userRepository.authenticate(User(username, password))

    def create(username: String, password: String) = userRepository.create(new User(username, password))

    def delete(user: User) = userRepository.delete(user)
  }
}


object DItest {
  def main(args: Array[String]) {
    val woot = ComponentRegistry
    val userService = ComponentRegistry.userService
    val user = userService.authenticate("stig", "stog")
  }
}

object ComponentRegistry extends UserServiceComponent with UserRepositoryComponent

