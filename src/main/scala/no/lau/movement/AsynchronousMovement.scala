package no.lau.movement
import actors.Actor
import actors.Actor._
import no.lau.domain.movement.{Movable, Up, Direction, StackableMovement}
import no.lau.domain.Game

class VerySimpleClock(game:Game, time:Long, renderingCallBack: () => Unit) extends Actor {
  def act() {
    loop {
      reactWithin(time) {
        case _ => {
          game.newTurn
          for(tl <- tickListeners) {
            tl.tick
          }
          renderingCallBack()
        }
      }
    }
  }

  var tickListeners:List[TickListener] = List()
  def addTickListener(newListener:TickListener) { tickListeners = newListener :: tickListeners }
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

trait TickListener {
  def tick { println("Need to implement tick")}
}