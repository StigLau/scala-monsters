package no.lau.domain


object GameTest {
  def main(args: Array[String]) {
    val game = Game(30)

    1 to 120 foreach { arg => game.addRandomly(Wall()) }
    1 to 4 foreach { arg => game.addRandomly(Monster()) }

    val playerLeif = Player("Leif")
    val playerKurt = Player("Kurt")
    game.addRandomly(playerKurt)
    game.addRandomly(playerLeif)

    game.printBoard()
    "Game ended"
  }
}