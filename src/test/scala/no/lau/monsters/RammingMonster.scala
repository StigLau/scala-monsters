package no.lau.monsters

import no.lau.movement.TickListener
import no.lau.domain.{GamePiece, Game, Monster}
import no.lau.domain.movement._

/**
 * This code is an ugly hack for getting a first version up and running :)
 */
class RammingMonster(game: Game, id: Any) extends Monster(game, id) with StackableMovement with TickListener {
  override def tick {
    val enemies = findEnemies
    enemies.firstOption match {
      case Some(enemy) => {
        val direction = findPathTo(findEnemies.head)
        println("Going " + direction)
        stackMovement(direction)
      }
      case None => 
    }
  }

  def findEnemies:List[GamePiece] = game.currentGameBoard.values.filter((x: GamePiece) => x.isInstanceOf[Monster] && x != this).toList

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

