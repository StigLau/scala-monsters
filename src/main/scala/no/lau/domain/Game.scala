package no.lau.domain

import collection.mutable.HashMap
import scala.util.Random
import no.lau.movement.{Location, QueuedMovement, Movable}

/**
 * BoardSize X and Y start from 0 to make computation easier to write :)
 */
trait GameTrait {
  val boardSizeX: Int
  val boardSizeY: Int

  var gameBoards:List[HashMap[Location, GamePiece]] = List(new HashMap[Location, GamePiece])

  def currentGameBoard():HashMap[Location, GamePiece] = gameBoards.first
  def previousGameBoard():HashMap[Location, GamePiece] = {
    if(gameBoards.tail.size > 0)
      gameBoards.tail.first
    else
      gameBoards.first
  }

  def newTurn = {
    gameBoards = cloneCurrent :: gameBoards
    for(gamePiece <- previousGameBoard.values) {
      (gamePiece, gamePiece) match {
        case (stackable: QueuedMovement, movable: Movable) => {

          if (stackable.movementQueue.size > 0) println(stackable.movementQueue)

          stackable.movementQueue.firstOption match {
            case Some(direction) => {
              try {
                movable.move(direction)
                stackable.movementQueue = stackable.movementQueue.tail
              } catch {
                case ime: IllegalMoveException => {
                  //println("Illegal Move for " + movable + ": " + ime.getMessage)
                  stackable.movementQueue = List()
                  stackable.progressionHalted
                }
              }
            }
            case None =>
          }
        }
        case _ =>
      }
    }
    currentGameBoard
  }

  private def cloneCurrent = currentGameBoard.clone.asInstanceOf[HashMap[Location, GamePiece]]

  /**
   * Simple algorithm for selecting free tiles on a board
   */
  def findRandomFreeCell(): Location = {
    val freeCells = for {
      i <- 0 to boardSizeX
      j <- 0 to boardSizeY
      if currentGameBoard.get(Location(i, j)) isEmpty}
    yield Location(i, j)

    freeCells(new Random().nextInt(freeCells.length))
  }

  def addRandomly(gamePiece: GamePiece) = currentGameBoard += findRandomFreeCell -> gamePiece

  def whereIs(gamePiece: GamePiece, gameBoard:HashMap[Location, GamePiece]): Option[Location] = {
    val foundItAt: Int = gameBoard.values.indexOf(gamePiece)
    if(foundItAt != -1)
      Some(gameBoard.keySet.toArray(foundItAt))
    else
      None
  }

  def printableBoard = {
    val table = for (y <- 0 to boardSizeY)
    yield {
        val row = for (x <- 0 to boardSizeX) yield currentGameBoard.getOrElse(Location(x, boardSizeY - y), ".")
        row.foldLeft("")(_ + _) + "\n"
      }
    table.foldLeft("")(_ + _)
  }

  def createBoarder() {
    for(x <- 0 to boardSizeX; y <- 1 to boardSizeY -1) {
      currentGameBoard += Location(x, 0) -> new StaticWall()
      currentGameBoard += Location(x, boardSizeY) -> new StaticWall()
      currentGameBoard += Location(0, y) -> new StaticWall()
      currentGameBoard += Location(boardSizeX, y) -> new StaticWall()
    }
  }
}
class Game(val boardSizeX: Int, val boardSizeY: Int) extends GameTrait {
  createBoarder()
}
/** Marker - GamePiece of the Game Board */
trait GamePiece
/** Marker - GamePiece can be killed */
trait Mortal { def kill {println(this + " was killed!")} }
/** Marker - Monster kan kill by eating */
trait Meelee
/** Marker - Able to push stuff */
trait Pusher
/** Marker - a playable character */
trait Player { override def toString = "∆" }
/** The generic NPC or playable charachter */
class Monster(val game: Game) extends GamePiece{ override def toString = "H" }
/** Block can be pushed */
class Block(val game: Game) extends Movable {override def toString = "B"}
/** Can not be pushed  */
class StaticWall() extends GamePiece {override def toString = "W"}
/** Main exception for instructing that movement was illegal */ 
case class IllegalMoveException(val message: String) extends Throwable {
  override def getMessage = message
}