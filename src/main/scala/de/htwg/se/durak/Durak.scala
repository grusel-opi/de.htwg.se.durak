import scala.io.StdIn._

object Durak {

  val shuffledDeck: Deck = new Deck().shuffle
  var playerList: List[Player] = List()
  var attackCards: List[Card] = List()
  var blockCards: List[Card] = List()
  var handCardIdx: Option[Int] = None

  def addBlockCard(card: Card): Unit = blockCards = card :: blockCards

  def parsePlayerInput(p: Player, pInput: String): Unit = {
    val commandList: Array[String] = Array("play", "end", "take")
    val splittedPInput: Array[String] = pInput.split(" ")
    var validCommand: Boolean = false

    while (!validCommand) {
      splittedPInput(0) match {
        case "play" => {
          // block attack cards from opponent
          if (splittedPInput.length - 1 == 2) {
            if (!splittedPInput(1).isEmpty && !splittedPInput(2).isEmpty) {
              println("Recognized command to block attack cards")

              val blockCard: Card = p.handCards(splittedPInput(1).toInt)
              addBlockCard(blockCard)
              validCommand = true
            } else {
              throw new Exception("Not implemented yet!")
            }
          } else if (splittedPInput.length - 1 == 1) {
            println("Recognized command to attack")
            validCommand = true
          } else {
            System.err.println("Command \"play\" needs at least one parameter and maximum two parameters! Please try again!")
            val newPlayerInput: String = readLine()
            parsePlayerInput(p, newPlayerInput)
          }
        }

      }
    }

    println("Attack cards: " + attackCards)
    println("Block cards: " + blockCards)
    System.exit(0)
  }

  def main(args: Array[String]): Unit = {

    println("TEST: ")
    println("Please enter number of players or press enter for two players")
    val playerInput: String = readLine()
    val numOfP: Int = 2
    for (i <- 1 to numOfP) {
      println("Enter name for player" + i + ":")
      val playerName: String = readLine()
      playerList = new Player(playerName) :: playerList
    }

    println("Names set up, now play")

    parsePlayerInput(playerList(0), playerInput.toString)

    System.exit(0)

    println("--------------- Durak: ---------------")

    println("How many players?")

    val numberOfPlayers: Int = readLine().toInt

    for (i <- 1 to numberOfPlayers) {
      println("Enter name for player" + i + ":")
      val playerName: String = readLine()
      playerList = new Player(playerName) :: playerList
    }

    println("Player list:")
    println(playerList)
    println()
    println(shuffledDeck)
    println("Number of cards in deck: " + shuffledDeck.cards.length)
    println("--------------------------------------")
    println()
    println("Try adding hand cards to players:")

    for (i <- 0 to 4) {
      for (p <- playerList)
        p.pickCard(shuffledDeck)
    }

    println()
    println("HandCards:")

    for (p <- playerList) {
      println(p)
    }

    println()

    println("Number of cards in deck after player drawing cards: " + shuffledDeck.cards.length)
    println()

    val trumpCard: Card = shuffledDeck.popTopCard()

    println("Trump card: " + trumpCard)
    println("Number of cards after selecting trump card: " + shuffledDeck.cards.length)
    println("--------------------------------------")
    println()

    println("START GAME")
    println()

    var playerIdx: Int = 0

    while (playerList.length > 1) {
      println("--------------- " + playerList(playerIdx).name + "'s attack turn ---------------")
      println()
      println("Trump card: " + trumpCard)
      println("Hand cards: " + playerList(playerIdx).handCards)
      println()
      print("Which card you want to play? Enter card index: ")

      handCardIdx = Some(readLine().toInt)
      attackCards = playerList(playerIdx).handCards(handCardIdx.get) :: attackCards
      playerList(playerIdx).removeCard(handCardIdx.get)

      println()
      println("Attrack cards: " + attackCards)
      println("HandCards:" + playerList(playerIdx).handCards)
      println()
      print("Wana play another card? Enter card index or fin to finish your turn:")
      val playerInput: String = readLine()
      if (playerInput.equals("fin")) {
        println(playerList(playerIdx).name + " finished attack turn")
        println()

        playerIdx += 1

        println("--------------- " + playerList(playerIdx).name + "'s block turn --------------")
        println()
        println("Trump card: " + trumpCard)
        println("Hand cards: " + playerList(playerIdx).handCards)
        println("Cards you have to block: " + attackCards)
        println()
        print("Which card you want to play on a attack card? Enter both idices (separated by whitespace): ")

        handCardIdx = Some(readLine().toInt)
        blockCards = playerList(playerIdx).handCards(handCardIdx.get) :: blockCards
        playerList(playerIdx).removeCard(handCardIdx.get)

        println()
        println("Block cards: " + blockCards)

      } else {
        System.exit(0)
      }
    }

    println("Finished!")
  }
}
