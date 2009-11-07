package no.lau.monsters

import no.lau.movement._
import no.lau.domain._

/**
 * This code is an ugly hack for getting a first version up and running :)
 */
trait SimpleMonsterAI extends StackableMovement with TickListener {
  override def tick {
    if(prioritizedEnemy != None)
      findPathTo(prioritizedEnemy.get) match {
        case Some(direction) => queueMovement(direction)
        case None =>
      }
    else
      None
  }

  def enemies:List[GamePiece] = game.currentGameBoard.values.filter((x: GamePiece) => x.isInstanceOf[Player] && x != this).toList

  def prioritizedEnemy = enemies.firstOption

  def findPathTo(enemy: GamePiece): Option[Direction] = {
    val iAmHereOption = game.whereIs(this, game.currentGameBoard)
    val enemyIsThereOption = game.whereIs(enemy, game.currentGameBoard)
    (iAmHereOption, enemyIsThereOption) match {
      case (Some(iAmHere), Some(enemyIsThere)) => {
        val xDistance = Math.abs(iAmHere._1 - enemyIsThere._1)
        val yDistance = Math.abs(iAmHere._2 - enemyIsThere._2)
        if (xDistance > yDistance) {
          if (iAmHere._1 > enemyIsThere._1)
            Some(Left)
          else
            Some(Right)
        } else {
          if (iAmHere._2 > enemyIsThere._2)
            Some(Down)
          else
            Some(Up)
        }
      }
      case _ => None
    }
  }
}

class RammingMonster(game: Game, id: Any) extends Monster(game, id) with SimpleMonsterAI with Meelee with Mortal 

