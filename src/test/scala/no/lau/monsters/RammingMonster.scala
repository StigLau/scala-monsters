package no.lau.monsters

import no.lau.movement._
import no.lau.domain._

/**
 * This code is an ugly hack for getting a first version up and running :)
 */
class RammingMonster(game: Game, id: Any) extends Monster(game, id) with StackableMovement with TickListener with Meelee with Mortal {
  override def tick {
    enemies.firstOption match {
      case Some(enemy) => {
        val direction = findPathTo(enemies.head)
        stackMovement(direction)
      }
      case None =>
    }
  }

  def enemies:List[GamePiece] = game.currentGameBoard.values.filter((x: GamePiece) => x.isInstanceOf[Monster] && x != this).toList

  def findPathTo(enemy: GamePiece): Direction = {
    val iAmHere = game.whereIs(this, game.currentGameBoard)
    val enemyIsThere = game.whereIs(enemy, game.currentGameBoard)

    val xDistance = Math.abs(iAmHere._1 - enemyIsThere._1)
    val yDistance = Math.abs(iAmHere._2 - enemyIsThere._2)
    if (xDistance > yDistance) {
      if (iAmHere._1 > enemyIsThere._1)
        return Left
      else
        return Right
    } else {
      if (iAmHere._2 > enemyIsThere._2)
        return Down
      else
        return Up
    }
  }
}

