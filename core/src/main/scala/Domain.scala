package no.lau.domain

import no.lau.movement.Movable

/** Marker - GamePiece of the Game Board */
trait GamePiece
/** Marker - GamePiece can be killed */
trait Mortal { def kill {println(this + " was killed!")} }
/** Marker - Monster kan kill by eating */
trait Meelee
/** Marker - Able to push stuff */
trait Pusher
/** Marker - a playable character */
trait Player { override def toString = "∆" }
/** The generic NPC or playable charachter */
class Monster(val game: Game) extends GamePiece{ override def toString = "H" }
/** Block can be pushed */
class Block(val game: Game) extends Movable {override def toString = "B"}
/** Can not be pushed  */
class StaticWall() extends GamePiece {override def toString = "W"}
/** Main exception for instructing that movement was illegal */
case class IllegalMoveException(val message: String) extends Throwable {
  override def getMessage = message
}