package no.lau.monsters

import no.lau.domain.{Game, Monster}
import no.lau.domain.movement.{Up, Movable}
import no.lau.movement.TickListener

class RammingMonster(game: Game, id: Any) extends Monster(game, id) with Movable with TickListener {
  override def tick {
    move(Up)
    println("Moving up by tick")
  }
}
