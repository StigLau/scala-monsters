package no.lau.domain


object GameTest {
  def main(args: Array[String]) {
    val playerLeif = Player("Leif")
    val playerKurt = Player("Kurt")
    val monsterGunnar = Monster
    val monsterRonny = Monster

    val gameBoard = List(List())
    
    Game(List(playerKurt, playerLeif), gameBoard)
    println ("finished")
  }
}