package no.lau.predefined

import no.lau.domain._
import no.lau.movement.{Clock, StackableMovement}
import no.lau.monsters.RammingMonster

/**
 * Settings of each level according to the original Beasts Game
 */
case class Levels(
        pullBlocks:Boolean,
        gameSpeedUp:Boolean,
        explosiveBlocks:Boolean,
        superBeast:Range,
        ratioEggs:Int,
        hatchingSpeed:Int,
        startingBeasts:Int)

abstract class LevelEasyA extends Config {
  val level = Levels(true, false, false, 0 to 0, 0,	0, 1)
}

abstract class LevelEasyB extends Config {
  val level = Levels(true, false, false, 0 to 0, 0,	0, 2)
}

abstract class LevelEasyC extends Config {
  val level = Levels(true, false, false, 0 to 0, 0,	0, 3)
}

trait Config{
  val player:Monster with StackableMovement
  val game:Game
  val level:Levels
  def gameConfig(clock:Clock) = {
    1 to 250 foreach {arg => game.addRandomly(Block(game, "block " + arg))}
    1 to level.startingBeasts foreach { arg =>
              val monster = new RammingMonster(game, "monster " + arg) {
                override def kill { clock.removeTickListener(this) }
              }
              game.addRandomly(monster)
              clock.addTickListener(monster)
    }
    game.addRandomly(player)
  }

}