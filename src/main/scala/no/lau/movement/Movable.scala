package no.lau.movement

import no.lau.domain._

trait StackableMovement extends Movable {
  var movementStack:List[Direction] = List()
  def stackMovement(dir:Direction) { movementStack = movementStack ::: List(dir) }
  def progressionHalted { println("Further progression halted") } //todo implement what clients should do when progression halts
}

trait Movable extends GamePiece {
  val game: Game //todo game should preferably be referenced some other way

  /**
   * Used for moving gamepieces around the gameBoard
   * If the route ends up in an illegal move at one stage, the movement will be dropped and an IllegalMovementException will be thrown
   * The route can consist of up to four GamePieces to do checks
   * todo working with Tuple2[x, y] is not as it should be. Should be working with the objects, and some places ask where they are located.
   **/
  def move(inThatDirection: Direction) {
    val firstPlace = game.whereIs(this, game.previousGameBoard)
    val secondPlace = goingTowards(firstPlace, inThatDirection)
    val thirdPlace = goingTowards(secondPlace, inThatDirection)
    val fourthPlace = goingTowards(thirdPlace, inThatDirection)
    val movement = (this, whosInMyWay(secondPlace), whosInMyWay(thirdPlace), whosInMyWay(fourthPlace))

    if (isOverBorder(secondPlace))
      throw IllegalMoveException("Move caused movable to travel across the border")

    movement match {
      case (pusher:Pusher, Some(movable:Movable), Some(victim:Mortal), Some(gp:GamePiece)) => victim.kill; game.currentGameBoard -= thirdPlace; movable.move(inThatDirection) //Squishing
      case (offender:Meelee, Some(victim:Mortal), _, _) => println("We have a " + offender + " attacking a " + victim); victim.kill
      case (pusher: Pusher, Some(movable:Movable), _, _) => movable.move(inThatDirection)
      //case (_, Some(movable:Movable), _, _) => movable.move(inThatDirection) // Moving blocks
      case (movable:Movable, None, _, _) => //Continue with your move
      case (_, Some(gamePiece: GamePiece), _, _) => throw IllegalMoveException("Trying to move unmovable Gamepiece")
    }
    move(firstPlace, secondPlace)
  }

  private def goingTowards(oldLocation:Tuple2[Int, Int], inThatDirection:Direction):Tuple2[Int, Int] = (oldLocation._1 + inThatDirection.dir._1, oldLocation._2 + inThatDirection.dir._2)

  private def whosInMyWay(newLocation:Tuple2[Int, Int]):Option[GamePiece] = game.previousGameBoard.get(newLocation)

  private def isOverBorder(newLocation: Tuple2[Int, Int]) = newLocation._1 > game.boardSizeX || newLocation._1 < 0 || newLocation._2 > game.boardSizeY || newLocation._2 < 0

  private def move(oldLocation: Tuple2[Int, Int], newLocation: Tuple2[Int, Int]) {
    game.currentGameBoard -= oldLocation
    game.currentGameBoard += newLocation -> this
  }

  def whereAreYou = game.whereIs(this, game.previousGameBoard)
}

// Direction enum should preferably also provide a matrix to indicate that Up is (+1, +0), which could mean that Move didn't have to include the pattern matching.
object Direction extends Enumeration {
  val Up, Down, Right, Left = Value
}

sealed abstract class Direction(val dir: Tuple2[Int, Int])
case object Up extends Direction(0, 1)
case object Down extends Direction(0, -1)
case object Right extends Direction(1, 0)
case object Left extends Direction(-1, 0)

