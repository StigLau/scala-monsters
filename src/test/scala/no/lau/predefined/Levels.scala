package no.lau.predefined

import no.lau.domain._
import no.lau.movement.{Clock, QueuedMovement}
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

abstract class LevelEasyE extends Config {
  val level = Levels(true, true, false, 0 to 0, 0,	0, 4)
}

trait Config{
  val player:Monster with QueuedMovement
  val game:Game
  val level:Levels
  def gameConfig(clock:Clock) = {
    1 to 270 foreach {arg => game.addRandomly(Block(game, "block " + arg))}
    1 to 10 foreach {arg => game.addRandomly(StaticWall())}
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