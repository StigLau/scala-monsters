package no.lau.domain

/**
 * BoardSize X and Y start from 0 to make computation easier to write :)
 */
case class Game(boardSizeX: Int, boardSizeY: Int) {
  val rnd = new scala.util.Random
  //todo new gameBoard should only contain None's
  import scala.collection.mutable.HashMap
  val gameBoard = new HashMap[Tuple2[Int, Int], GamePiece]

  //This code does currently not actually pic a free cell, and should be fixed accordingly
  def getRandomFreeCell() = getRandomCell

  def getRandomCell():Tuple2[Int, Int] =
    (rnd.nextInt((0 to boardSizeX) length), rnd.nextInt((0 to boardSizeY) length))


  def addRandomly(gamePiece: GamePiece) = gameBoard += getRandomFreeCell() -> gamePiece

  /**
   * Used by find where gamepieces are located on the map
   * todo this search can be done muuuuuch better!!!
   * or create a better representation for the data, and still print it out as a table
   */
  def whereIs(gamePiece:GamePiece):Tuple2[Int, Int] = {
    val foundItAt:Int = gameBoard.values.indexOf(gamePiece)
    gameBoard.keySet.toArray(foundItAt)
  }

  def printBoard() {
    for (column <- 0 to boardSizeY) {
      for (row <- 0 to boardSizeX) {
        //println (print(gameBoard.get(row, (boardSizeY) - column)))
        print(gameBoard.get(row, (boardSizeY) - column) match {
          case Some(gamePiece) => gamePiece
          case None => "."
          //case _ => None
        })
      }
      print("\n")
    }
  }
}

abstract class GamePiece 
//todo gamepieces should not themselves know where they are, the gameboard should!
trait Movable extends GamePiece {
  val game:Game
  

  /**
   * Moving in a direction should have a callback to inform that the procedure could not be done in a tick.
   * By doing this, the server can stack up movement, and the client can give a route to follow before in time.
   * If the route ends up in an illegal move at one stage, the rest of the movement will be dropped and the client informed.
   * The callback should be implemented as a closure (?)
   **/
  def move(direction:Direction.Value) {
    import Direction.{Up, Down, Left, Right}
    println(this + " moving " + direction);
    val oldLocation = game.whereIs(this)
    val newLocation = direction match {
      case Up => (oldLocation._1, oldLocation._2 + 1)
      case Right => (oldLocation._1 + 1, oldLocation._2 )
      case Down => (oldLocation._1, oldLocation._2 - 1)
      case Left => (oldLocation._1 - 1, oldLocation._2 )
    }
    //println(this + " found " + game.gameBoard.get(newLocation) + " at " + newLocation)
    //Is this the correct way to do this?
    game.gameBoard.get(newLocation) match {
      case Some(any) => any match {
        case movable:Movable => movable.move(direction)
        case gamePiece:GamePiece => throw new IllegalMoveException
      }
      case None => println(newLocation + " is free; continue!")
    }
    println(this + " moved from " + oldLocation + " to " + newLocation)
    game.gameBoard -= oldLocation 
    game.gameBoard += newLocation -> this
  }

  def whereAreYou = game.whereIs(this)
}
// Direction enum should preferably also provide a matrix to indicate that Up is (+1, +0), which could mean that Move didn't have to include the pattern matching.
object Direction extends Enumeration {
    val Up = Value("UP")
    val Down = Value("DOWN")
    val Right = Value("RIGHT")
    val Left = Value("LEFT")
}

abstract class Player(name: String) extends Movable {
  //Print only first letter in name
  override def toString = name.substring(0, 1)
}

case class Monster(game:Game) extends Movable { override def toString = "H" }

case class Block(game:Game) extends Movable {override def toString = "B"}

case class StaticWall() extends GamePiece {override def toString = "W"}

class IllegalMoveException extends Exception