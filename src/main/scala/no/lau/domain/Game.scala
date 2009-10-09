package no.lau.domain

case class Game(gameSize:Integer, players:List[Player], gameboard:List[List[Object]])

case class Player(name:String)