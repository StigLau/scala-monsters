package no.lau.monsters

import no.lau.domain.{Game, Monster}
import no.lau.movement.TickListener
import no.lau.domain.movement.{StackableMovement, Up, Movable}

class RammingMonster(game: Game, id: Any) extends Monster(game, id) with StackableMovement with TickListener {
  override def tick {
    stackMovement(Up)
    println("Moving up by tick")
  }
}
