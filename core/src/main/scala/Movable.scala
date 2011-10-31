package no.lau.movement

import no.lau.domain._

trait QueuedMovement extends Movable {
  var movementQueue:List[Direction] = List()
  def queueMovement(dir:Direction) { movementQueue = movementQueue ::: List(dir) }
  def progressionHalted { /* println("Further progression halted") */}
}

trait Movable extends GamePiece {
  val game: Game

  /**
   * Used for moving gamepieces around the gameBoard
   * If the route ends up in an illegal move at one stage, the movement will be dropped and an IllegalMovementException will be thrown
   * The route can consist of up to four GamePieces to do checks
   **/
  def move(inThatDirection: Direction) {
    val firstPlace = game.whereIs(this, game.previousGameBoard).get
    val secondPlace = goingTowards(firstPlace, inThatDirection)
    val thirdPlace = goingTowards(secondPlace, inThatDirection)
    val fourthPlace = goingTowards(thirdPlace, inThatDirection)
    val movement = (this, whosInMyWay(secondPlace), whosInMyWay(thirdPlace), whosInMyWay(fourthPlace))

    movement match {
      case (pusher:Pusher, Some(movable:Movable), Some(victim:Mortal), Some(gp:GamePiece)) => victim.kill; game.currentGameBoard -= thirdPlace; movable.move(secondPlace, thirdPlace) //Squishing
      case (offender:Meelee, Some(victim:Mortal), _, _) => println("We have a " + offender + " attacking a " + victim); victim.kill
      case (pusher: Pusher, Some(movable:Movable), _, _) => movable.move(inThatDirection)
      //case (_, Some(movable:Movable), _, _) => movable.move(inThatDirection) // Moving multiple blocks - This is problematic in combination with squishing
      case (movable:Movable, None, _, _) => //Continue with your move
      case (_, Some(gamePiece: GamePiece), _, _) => throw IllegalMoveException("Trying to move unmovable Gamepiece")
    }
    move(firstPlace, secondPlace)
  }

  private def goingTowards(oldLocation:Location, inThatDirection:Direction):Location = Location(oldLocation.x + inThatDirection.dir.x, oldLocation.y + inThatDirection.dir.y)

  private def whosInMyWay(newLocation:Location):Option[GamePiece] = game.previousGameBoard.get(newLocation)

  private def move(oldLocation: Location, newLocation: Location) {
    game.currentGameBoard -= oldLocation
    game.currentGameBoard += newLocation -> this
  }

  def whereAreYou = game.whereIs(this, game.previousGameBoard)
}

case class Location(x:Int, y:Int)

sealed abstract class Direction(val dir: Location)
case object Up extends Direction(Location(0, 1))
case object Down extends Direction(Location(0, -1))
case object Right extends Direction(Location(1, 0))
case object Left extends Direction(Location(-1, 0))
case object Wait extends Direction(Location(0, 0))

