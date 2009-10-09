package no.lau.domain


object GameTest {
  def main(args: Array[String]) {
    val playerLeif = Player("Leif")
    val playerKurt = Player("Kurt")
    val monsterGunnar = Monster()
    val monsterRonny = Monster()

    val game = Game(16, List(playerKurt, playerLeif))

    0 to 30 foreach { arg => game.addRandomly(Wall()) }
    
    game.addRandomly(monsterGunnar)
    game.addRandomly(monsterRonny)
    game.addRandomly(playerKurt)
    game.addRandomly(playerLeif)
    
    game.printBoard()
    "Game ended"
  }
}