package no.lau.movement

import actors.Actor
import no.lau.domain.Game

abstract class Clock extends Actor {
  var tickListeners: List[TickListener] = List()

  def addTickListener(newListener: TickListener) {tickListeners = newListener :: tickListeners}

  def removeTickListener(toBeRemoved: TickListener) {tickListeners.filterNot(_ == toBeRemoved)}
}

class VerySimpleClock(game: Game, time: Long, renderingCallBack: () => Unit) extends Clock {
  def act() {
    loop {
      reactWithin(time) {
        case _ => {
          game.newTurn
          for (tl <- tickListeners) {
            tl.tick
          }
          renderingCallBack()
        }
      }
    }
  }
}

trait AsymmetricGamingInterface extends Actor

class AsymmetricGamingImpl(mover: QueuedMovement) extends AsymmetricGamingInterface {
  def act() {
    loop {
      react {
        case direction: Direction => mover.queueMovement(direction)
        case _ => println("No direction")
      }
    }
  }
}

trait TickListener {
  def tick {println("Need to implement tick")}
}