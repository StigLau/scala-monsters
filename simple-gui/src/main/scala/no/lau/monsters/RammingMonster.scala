package no.lau.monsters

import no.lau.movement._
import no.lau.domain._

/**
 * This code is an ugly hack for getting a first version up and running :)
 */
trait SimpleMonsterAI extends QueuedMovement with TickListener {
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

  def prioritizedEnemy = enemies.headOption

  def findPathTo(enemy: GamePiece): Option[Direction] = {
    val iAmHereOption = game.whereIs(this, game.currentGameBoard)
    val enemyIsThereOption = game.whereIs(enemy, game.currentGameBoard)
    (iAmHereOption, enemyIsThereOption) match {
      case (Some(iAmHere), Some(enemyIsThere)) => {
        val xDistance = math.abs(iAmHere.x - enemyIsThere.x)
        val yDistance = math.abs(iAmHere.y - enemyIsThere.y)
        if (xDistance > yDistance) {
          if (iAmHere.x > enemyIsThere.x)
            Some(Left)
          else
            Some(Right)
        } else {
          if (iAmHere.y > enemyIsThere.y)
            Some(Down)
          else
            Some(Up)
        }
      }
      case _ => None
    }
  }
}

class RammingMonster(game: Game) extends Monster(game) with SimpleMonsterAI with Meelee with Mortal 

