package no.lau.movement
import actors.Actor
import actors.Actor._
import no.lau.domain.movement.{Direction, StackableMovement}
import no.lau.domain.Game

class VerySimpleClock(game:Game, time:Long, renderingCallBack: () => Unit) extends Actor {
  def act() {
    loop {
      reactWithin(time) {
        case _ => game.newTurn; renderingCallBack()
      }
    }
  }
}

class AsymmetricGamingInterface(game: Game, stackableMovable: StackableMovement) extends Actor {
  def act() {
    loop {
      react {
        case direction: Direction => stackableMovable.stackMovement(direction)
        case _ => println("No direction")
      }
    }
  }
}