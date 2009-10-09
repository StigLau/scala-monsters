package no.lau.domain

case class Game(boardSize:int) {
  val rnd = new scala.util.Random
  val gameBoard = new Array[Array[Any]](boardSize, boardSize)

  //This code does currently not actually pic a free cell, and should be fixed accordingly 
  def getRandomFreeCell() = {
    val range = 0 to boardSize -1
    (rnd.nextInt(range length), rnd.nextInt(range length))
  }

  def addRandomly(any:Any) {
    val ran:Tuple2[Int, Int] = getRandomFreeCell()
    gameBoard(ran._1)(ran._2) = any
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

case class Player(name: String) extends GamePiece {
  //Print only 1. letter in name
  override def toString = name.substring(0, 1)
}

case class Monster() extends GamePiece {override def toString = "H"}

case class Wall() extends GamePiece {override def toString = "W"}