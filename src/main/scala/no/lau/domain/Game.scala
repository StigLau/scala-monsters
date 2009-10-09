package no.lau.domain

case class Game(players:List[Player], gameboard:List[List[Object]])

case class Player(name:String)

case class Monster() {
  override def toString = "H"
}