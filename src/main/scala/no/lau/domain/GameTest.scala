package no.lau.domain


object GameTest {
  def main(args: Array[String]) {
    val player1 = new Player("Leif")
    val player2 = new Player("Kåre")

    val gameBoard = List(List())
    
    new Game(16, List(player1, player2), gameBoard)
    println ("finished")
  }
}