package no.lau.domain

case class Game(boardSizeX: Int, boardSizeY: Int) {
  val rnd = new scala.util.Random
  val gameBoard = new Array[Array[Any]](boardSizeX, boardSizeY)

  //This code does currently not actually pic a free cell, and should be fixed accordingly
  def getRandomFreeCell() = getRandomCell()

  def getRandomCell():Tuple2[Int, Int] =
    (rnd.nextInt((0 to boardSizeX - 1) length), rnd.nextInt((0 to boardSizeY - 1) length))


  def addRandomly(gamePiece: GamePiece) = {
    val placement: Tuple2[Int, Int] = getRandomFreeCell()
    gameBoard(placement._1)(placement._2) = gamePiece
    gamePiece
  }

  def printBoard() {
    for (column <- gameBoard) {
      for (cell <- column) {
        print(cell match {
          case player: GamePiece => player
          case null => " "
          case _ => None
        })
      }
      print("\n")
    }
  }
}

class GamePiece

trait Movable {
  //Todo find a better initial value, None:Int or something!
  protected var location:Tuple2[Int, Int] = (-1, -1)

  /**
   * Moving in a direction should have a callback to inform that the procedure could not be done in a tick.
   * By doing this, the server can stack up movement, and the client can give a route to follow before in time.
   * If the route ends up in an illegal move at one stage, the rest of the movement will be dropped and the client informed.
   * The callback should be implemented as a closure (?)
   **/
  def move(direction:Direction.Value) {
    import Direction.{Up, Down, Left, Right}
    println(this + " moving " + direction); 
    location = direction match {
      case Up => (location._1, location._2 + 1)
      case Right => (location._1 + 1, location._2 )
      case Down => (location._1, location._2 - 1)
      case Left => (location._1 - 1, location._2 )
    }
  }
  def whereAreYou = location
}

object Direction extends Enumeration {
    val Up = Value("UP")
    val Down = Value("DOWN")
    val Right = Value("RIGHT")
    val Left = Value("LEFT")
}

case class Player(name: String) extends GamePiece {
  //Print only 1. letter in name
  override def toString = name.substring(0, 1)
}

abstract class Monster() extends GamePiece with Movable {
  override def toString = "H"
}

case class Block() extends GamePiece {override def toString = "W"}

case class StaticWall() extends GamePiece {override def toString = ""}