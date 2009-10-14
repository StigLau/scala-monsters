package no.lau.domain

/**
 * BoardSize X and Y start from 0 to make computation easier to write :)
 */
case class Game(boardSizeX: Int, boardSizeY: Int) {
  val rnd = new scala.util.Random
  val gameBoard = new scala.collection.mutable.HashMap[Tuple2[Int, Int], GamePiece]

  /**
   * Simple algorithm for scattering out different objects.
   * Can be increasingly time consuming when nr of free cells -> 0
   * Got any better ways of doing this? :)
   */
  def findRandomFreeCell(): Tuple2[Int, Int] = {
    val randomCell = (rnd.nextInt((0 to boardSizeX) length), rnd.nextInt((0 to boardSizeY) length))
    if (gameBoard.get(randomCell) isEmpty)
      randomCell
    else
      findRandomFreeCell
  }


  def addRandomly(gamePiece: GamePiece) = gameBoard += findRandomFreeCell() -> gamePiece

  //Algorithm can take some time when nr of free cells --> 0
  def whereIs(gamePiece:GamePiece):Tuple2[Int, Int] = {
    val foundItAt:Int = gameBoard.values.indexOf(gamePiece)
    gameBoard.keySet.toArray(foundItAt)
  }
  //todo return table as string, as opposed to this function printing out each line
  def boardAsPrintable() {
    for (column <- 0 to boardSizeY) {
      for (row <- 0 to boardSizeX) {
        print(gameBoard.get(row, boardSizeY - column) match {
          case Some(gamePiece) => gamePiece
          case None => "."
        })
      }
      print("\n")
    }
  }
}

trait GamePiece

trait Movable extends GamePiece {
  val game:Game //todo game should preferably be referenced some other way


  /**
   * Used for moving gamepieces around the gameBoard
   * If the route ends up in an illegal move at one stage, the movement will be dropped and an IllegalMovementException will be thrown
   **/
  def move(direction:Direction) {
    println(this + " moving " + direction);
    val oldLocation = game.whereIs(this)
    val newLocation = direction match {
      case Up => (oldLocation._1, oldLocation._2 + 1)
      case Down => (oldLocation._1, oldLocation._2 - 1)
      case Right => (oldLocation._1 + 1, oldLocation._2 )
      case Left => (oldLocation._1 - 1, oldLocation._2 )
    }
    if(isOverBorder(newLocation)) throw IllegalMoveException("Move caused movable to travel across the border")

    //Is this the correct way to do this?
    game.gameBoard.get(newLocation) match {
      case Some(any) => any match {
        case movable:Movable => movable.move(direction)
        case gamePiece:GamePiece => throw IllegalMoveException("Trying to move unmovable Gamepiece")
      }
      case None => println(newLocation + " is free; continue!")
    }
    println(this + " moved from " + oldLocation + " to " + newLocation)
    game.gameBoard -= oldLocation 
    game.gameBoard += newLocation -> this
  }

  private def isOverBorder(newLocation:Tuple2[Int, Int]) = newLocation._1 > game.boardSizeX || newLocation._1 < 0 || newLocation._2 > game.boardSizeY || newLocation._2 < 0

  def whereAreYou = game.whereIs(this)
}

/**
 * Marks that a gamePiece can be squeezed
 */
trait Squeezable extends GamePiece

// Direction enum should preferably also provide a matrix to indicate that Up is (+1, +0), which could mean that Move didn't have to include the pattern matching.
object Direction extends Enumeration {
  val Up, Down, Right, Left = Value
}

sealed abstract class Direction(val dir:Tuple2[Int, Int])
case object Up extends Direction(0, 1)
case object Down extends Direction(0, -1)
case object Right extends Direction(1, 0)
case object Left extends Direction(-1, 0)

abstract class Player(name: String) extends Movable {
  //First letter in name
  override def toString = name.substring(0, 1)
}

case class Monster(game:Game, id:Any) extends Movable with Squeezable { override def toString = "H" }

//Todo blocks should need no identity. When one is moved, it is essentially deleted, and replaced by a new. If possible :) Or has an autogenerated id
case class Block(game:Game, id:Any) extends Movable { override def toString = "B" }

case class StaticWall() extends GamePiece {override def toString = "W"}

case class IllegalMoveException(message:String) extends Throwable