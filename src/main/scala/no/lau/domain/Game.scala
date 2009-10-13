package no.lau.domain

/**
 * BoardSize X and Y start from 0 to make computation easier to write :)
 */
case class Game(boardSizeX: Int, boardSizeY: Int) {
  val rnd = new scala.util.Random
  //todo new gameBoard should only contain None's
  import scala.collection.mutable.HashMap
  val gameBoard = new HashMap[Tuple2[Int, Int], GamePiece]

  /**
   * Simple algorithm for scattering out different objects.
   * Can be very time consuming when free cells -> 0
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

  /**
   * Used by find where gamepieces are located on the map
   * todo this search can be written muuuuuch better!!!
   */
  def whereIs(gamePiece:GamePiece):Tuple2[Int, Int] = {
    val foundItAt:Int = gameBoard.values.indexOf(gamePiece)
    gameBoard.keySet.toArray(foundItAt)
  }

  def printBoard() {
    for (column <- 0 to boardSizeY) {
      for (row <- 0 to boardSizeX) {
        print(gameBoard.get(row, boardSizeY - column) match {
          case Some(gamePiece) => gamePiece
          case None => "."
          //case _ => None
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
      case Right => {
        if(oldLocation._1 >= game.boardSizeX)
          throw new IllegalMoveException
        else
          (oldLocation._1 + 1, oldLocation._2 )
      }
      case Down => (oldLocation._1, oldLocation._2 - 1)
      case Left => (oldLocation._1 - 1, oldLocation._2 )
    }
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
  //First letter in name
  override def toString = name.substring(0, 1)
}

case class Monster(game:Game, id:Any) extends Movable { override def toString = "H" }

case class Block(game:Game, id:Any) extends Movable {
  override def toString = "B"
}

case class StaticWall() extends GamePiece {override def toString = "W"}

class IllegalMoveException extends Exception