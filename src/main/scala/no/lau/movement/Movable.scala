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
   **/

  def move(inThatDirection: Direction) {
    val oldLocation = game.whereIs(this, game.previousGameBoard)
    val newLocation = goingTowards(oldLocation, inThatDirection)

    if (isOverBorder(newLocation))
      throw IllegalMoveException("Move caused movable to travel across the border")

    //Is this the correct way to do this?
    whoIsAtNewLocation(oldLocation, inThatDirection) match {
      case Some(mortal: Mortal) => {
        this match {
          case meelee:Meelee => mortal.kill
          case block:Block => println("What is this block showing up here? Is the killee removed from the board?")
          case _ => throw IllegalMoveException("No Meelee trait!")
        }
        //todo Not sure what should be done for squeezing
        /*
        val wasSqueezed = try {
          mortal match {
            case movable:Movable => movable.tryToMove(inThatDirection); false}
          }
        catch {
          case ime: IllegalMoveException => mortal kill; true
        }
        if(!wasSqueezed) throw IllegalMoveException("Nothing to be squeezed against")
        */
      } //todo WAAAY ugly hack for squeezing monsters
      case Some(movable: Movable) => {
        //Code for squishing
        //Sequence for a squish: Pusher -> Movable -> Mortal -> GamePiece or  HBHB -> .HBB
        //val secondPlace = (newLocation._1 + inThatDirection.dir._1, newLocation._2 + inThatDirection.dir._2)
        //Pusher B Mortal
        val second = whoIsAtNewLocation(movable.whereAreYou, inThatDirection)
        if (second.isInstanceOf[Mortal]) {
          whoIsAtNewLocation(movable.whereAreYou, inThatDirection) match {
            case Some(gp: GamePiece) => second.asInstanceOf[Mortal].kill; movable.move(inThatDirection)
            case None => throw new IllegalMoveException("This code has to be checked!")
          }
        }
        else {
          this match {
            case pusher: Pusher => movable.move(inThatDirection)
            case _ => throw IllegalMoveException("Not allowed to push")
          }
        }
      }
      case Some(gamePiece: GamePiece) => throw IllegalMoveException("Trying to move unmovable Gamepiece")
      case None =>
    }
    move(oldLocation, newLocation)
  }

  private def goingTowards(oldLocation:Tuple2[Int, Int], inThatDirection:Direction):Tuple2[Int, Int] = (oldLocation._1 + inThatDirection.dir._1, oldLocation._2 + inThatDirection.dir._2)
  private def whoIsAtNewLocation(oldLocation:Tuple2[Int, Int], inThatDirection:Direction):Option[GamePiece] = whosInMyWay(goingTowards(oldLocation, inThatDirection))

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

