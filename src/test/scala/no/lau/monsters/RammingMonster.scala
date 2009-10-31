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
        findPathTo(enemies.head) match {
          case Some(direction) => stackMovement(direction)
          case None =>
        }
      }
      case None =>
    }
  }

  def enemies:List[GamePiece] = game.currentGameBoard.values.filter((x: GamePiece) => x.isInstanceOf[Monster] && x != this).toList

  def findPathTo(enemy: GamePiece): Option[Direction] = {
    val iAmHereOption = game.whereIs(this, game.currentGameBoard)
    val enemyIsThereOption = game.whereIs(enemy, game.currentGameBoard)
    if(iAmHereOption != None && enemyIsThereOption != None) {
      val iAmHere = iAmHereOption.get
      val enemyIsThere = enemyIsThereOption.get
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
    else
      None
  }
}

