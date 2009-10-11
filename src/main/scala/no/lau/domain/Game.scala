package no.lau.domain

case class Game(boardSizeX: Int, boardSizeY: Int) {
  val rnd = new scala.util.Random
  //todo new gameBoard should only contain None's
  val gameBoard = new Array[Array[GamePiece]](boardSizeX, boardSizeY)

  //This code does currently not actually pic a free cell, and should be fixed accordingly
  def getRandomFreeCell() = getRandomCell

  def getRandomCell():Tuple2[Int, Int] =
    (rnd.nextInt((0 to boardSizeX - 1) length), rnd.nextInt((0 to boardSizeY - 1) length))


  def addRandomly(gamePiece: GamePiece) = {
    val placement: Tuple2[Int, Int] = getRandomFreeCell()
    gameBoard(placement._1)(placement._2) = gamePiece
  }

  def getPieceAt(location:Tuple2[Int, Int]):GamePiece = {
    gameBoard(location._1)(location._2)
  }

  /**
   * Used by find where gamepieces are located on the map
   * todo this search can be done muuuuuch better!!!
   * or create a better representation for the data, and still print it out as a table
   */
  def whereIs(gamePiece:GamePiece):Tuple2[Int, Int] = {
    for (column <- 0 to boardSizeY -1) {
      for (row <- 0 to boardSizeX -1) {
        if(gameBoard(row)(column) == gamePiece) {
          return (row, column)
        }
      }
    }
    throw new RuntimeException("Could not find gamePiece. The piece is probably not on the board. This is not the correct way of handling this problem, as pieces can be removed from the playboard after beeing squeezed")
  }

  //todo The printing of the board is oriented wrong Y-axis :)
  def printBoard() {
    for (column <- 0 to boardSizeY -1) {
      for (row <- 0 to boardSizeX -1) {
        print(gameBoard(row)(column) match {
          case gamePiece: GamePiece => gamePiece
          case null => " "
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
    println(this + " found " + game.getPieceAt(newLocation) + " at " + newLocation)
    game.getPieceAt(newLocation) match {
      case movable:Movable => movable.move(direction)
      case gamePiece:GamePiece => throw new IllegalMoveException
      case null => println("Open square; continue!")
    }
    println(this + " moved from " + oldLocation + " to " + newLocation)
    game.gameBoard(oldLocation._1)(oldLocation._2) = null
    game.gameBoard(newLocation._1)(newLocation._2) = this
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

case class Player(name: String) extends GamePiece {
  //Print only first letter in name
  override def toString = name.substring(0, 1)
}

case class Monster(game:Game) extends Movable {
  override def toString = "H"
}

case class Block(game:Game) extends Movable {override def toString = "W"}

case class StaticWall() extends GamePiece {override def toString = ""}

class IllegalMoveException extends Exception