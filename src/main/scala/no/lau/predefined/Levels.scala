package no.lau.predefined

import no.lau.movement.StackableMovement
import no.lau.domain._

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

object LevelEasyA {
  val level = Levels(true, false, false, 0 to 0, 0,	0, 1)
}

object LevelEasyB {
  val level = Levels(true, false, false, 0 to 0, 0,	0, 2)
}

object LevelEasyC {
  val level = Levels(true, false, false, 0 to 0, 0,	0, 3)

  val game = Game(40, 25)

  val player = new Monster(game, "MonsterGunnar") with StackableMovement with Mortal with Pusher {
    override def toString = ""
  }
}

/*
class TestLevels {
  @Test
  def testRunningLevels {
    val game = Game(40, 25)
   val rnd = new scala.util.Random
   1 to 1 foreach {arg => game.addRandomly(Block(game, "a" + rnd.nextInt()))}
   //1 to 10 foreach {arg => game.addRandomly(Monster(game, "monster" + rnd.nextInt()))}

   val monsterGunnar = new Monster(game, "MonsterGunnar") with StackableMovement with Mortal with Pusher {
     override def toString = ""
   }
  }
} */


