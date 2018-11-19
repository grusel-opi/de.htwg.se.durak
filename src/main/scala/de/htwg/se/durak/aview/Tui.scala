package de.htwg.se.durak.aview

import de.htwg.se.durak.controller.Controller
import de.htwg.se.durak.util.Observer

class Tui(controller: Controller) extends Observer {

  controller.add(this)

  var exit: Boolean = false

  def processInputLine(input: String): Unit = {
    input match {
      case "q" => println("quit")
      case "h" => println("help")
      case "n" => {
        newGame
        while (!controller.gameFinished && !exit) {
          attack
          if (!exit) block
        }
        exit = false
      }
      case _ => println("unknown command: " + input + "!")
    }
  }

  def newGame: Unit = {
    println("\nWelcome to Durak!\n")
    // print("Please enter the number of players: ")

    //    val numberOfPlayers: Int = readInt
    val numberOfPlayers: Int = 2

    //    var playerNames: List[String] = Nil
    //
    //    for (i <- 0 to numberOfPlayers - 1) {
    //      print("Please enter name for Player" + (i + 1) + ": ")
    //
    //      val playerName: String = readLine()
    //      playerNames = playerName :: playerNames
    //    }
    // println()

    //    controller.createNewGame(numberOfPlayers, playerNames)

    val playerNames: List[String] = List("Stephan", "Felix")
    controller.createNewGame(numberOfPlayers, playerNames)

  }

  def exitGame: Unit = {
    print("Do you really want to exit the game? Your progress will be lost! [Y]:")

    val newUserInput: String = readLine
    println()
    if (newUserInput.equals("y") || newUserInput.equals("Y") || newUserInput.equals("")) {
      exit = true
    } else {

    }
  }

  def attack: Unit = {
    val attackerName: String = controller.wrappedAttacker match {
      case None => throw new MatchError("ILLEGAL! wrappedAttacker should not be None!!!")
      case _ => controller.wrappedAttacker.get.toString
    }

    val attackerHandCardsAsString: String = controller.wrappedAttacker match {
      case None => throw new MatchError("ILLEGAL! wrappedAttacker should not be None!!!")
      case _ => controller.wrappedAttacker.get.handCards.mkString(", ") + "\n"
    }

    val victimName: String = controller.wrappedVictim match {
      case None => throw new MatchError("ILLEGAL! wrappedVictim should not be None!!!")
      case _ => controller.wrappedVictim.get.toString
    }

    val trumpCard: String = controller.wrappedDurakGame match {
      case None => throw new MatchError("ILLEGAL! wrappedDurakGame should not be None!!!")
      case _ => controller.wrappedDurakGame.get.trump.toString
    }

    println("It's " + attackerName + "'s turn to attack " + victimName + "!\n")
    println("Trump card:      " + trumpCard)
    println("Your hand cards: " + attackerHandCardsAsString)
    print("Please coose your attack Cards:")

    var attackFinished: Boolean = false

    while (!attackFinished) {
      val userInput: String = readLine

      val commandParametersTuple: (String, String) = tokenizeUserInput(userInput)

      println("commandParametersTuple: " + commandParametersTuple)

      if (userInput.equals("exit")) {
        exitGame
        if (exit) return
      }

      if (!userInput.equals("fin")) {
        val cardIdxList: List[Int] = checkAttackUserInput(userInput)

        for (cardIdx <- cardIdxList) {
          controller.addAttackCard(cardIdx)
        }

        if (controller.attackCards.length > 0) {

          controller.removeCardsFromAttackerHand
          print("\nPlease choose other cards, or enter \"fin\" to finish attacking for now: ")
        }

      } else {
        if (controller.attackCards.length > 0) {
          attackFinished = true
        } else {
          println("You haven't set any attack cards yet! Therefore you cannot finish your attack turn!")
        }
      }
    }

  }

  def checkAttackUserInput(userInput: String): List[Int] = {
    val tokens: Array[String] = userInput.split(" ")

    var cardIdxList: List[Int] = Nil

    for (token <- tokens) {

      if (!token.equals("exit") && !token.equals("remove")) {
        val cardIdx: Int = tokenOfUserInputToInt(token)

        if (cardIdx < 0) {
          println("Could not process input : \"" + token + "\"! Please only enter numbers!\n")
          getNewAttackUserInput
        } else if (cardIdx > controller.wrappedAttacker.get.handCards.length) {
          println("Error: index " + cardIdx + "is out of range! Please try again...")
        } else {
          cardIdxList = cardIdx :: cardIdxList
        }
      } else {
        println("Trump card: " + controller.wrappedDurakGame.get.trump)
        println(controller.wrappedAttacker.get.handCards.mkString(", ") + "\n")
        print("Please coose your attack Cards:")
      }
    }
    cardIdxList
  }

  def getNewAttackUserInput: Unit = {
    println("Trump card: " + controller.wrappedDurakGame.get.trump)
    println(controller.wrappedAttacker.get.handCards.mkString(", ") + "\n")
    print("Please coose your attack Cards:")
    checkAttackUserInput(readLine)
  }

  def tokenOfUserInputToInt(tokenOfUserInput: String): Int = {
    try {
      tokenOfUserInput.toInt
    }

    catch {
      case _: NumberFormatException => -1
    }
  }

  def block: Unit = {
    fillConsole
    println("It's " + controller.wrappedVictim.get + "'s block turn!\n")
    println("Cards on table: " + controller.attackCards.mkString(", "))
    println("Trump Card: " + controller.wrappedDurakGame.get.trump + "\n")

    var blockingFinished: Boolean = false

    while (!blockingFinished) {
      if (controller.getPossibleBlockingCards != List.empty) {
        println("Hand cards: " + controller.wrappedVictim.get.handCards)
        print("Cards you can choose to block: " + controller.possibleBlockingCardsToString + ": ")
        val userInput: String = readLine

        if (userInput.equals("exit")) {
          exitGame
          if (exit) return
        }

        if (!userInput.equals("fin")) {
          val cardIdxTupleList: List[(Int, Int)] = checkBlockUserInput(userInput)
          if (!cardIdxTupleList.isEmpty) {
            if (!cardIdxTupleList.equals(List((-1, -1)))) {
              println("cardIdxTupleList: " + cardIdxTupleList)
            } else {
              blockingFinished = true
            }
          }
        } else {

        }
      } else {
        println("SCHLUCK!")
      }
    }
  }

  def checkBlockUserInput(userInput: String): List[(Int, Int)] = {
    val tokens: Array[String] = userInput.split(" ")

    var cardsIdxTupleList: List[(Int, Int)] = Nil
    var cardsIdxList: List[Int] = Nil

    if (!tokens(0).equals("exit")) {

      if (tokens.length % 2 == 0) {
        var i: Int = 0

        while (i < tokens.length) {

          val blockCardIdx: Int = tokenOfUserInputToInt(tokens(i))

          val attackCardIdx: Int = tokenOfUserInputToInt(tokens(i + 1))

          if (blockCardIdx > 0 && attackCardIdx > 0) {
            cardsIdxTupleList = (blockCardIdx, attackCardIdx) :: cardsIdxTupleList
            i += 2
          } else if (blockCardIdx == -1) {
            println("Could not process input: \"" + tokens(i) + "\"! Please only enter numbers!\n")
            getNewBlockUserInput
          } else {
            println("Could not process input: \"" + tokens(i + 1) + "\"! Please only enter numbers!\n")
            getNewBlockUserInput
          }
        }
      } else {
        if (controller.attackCards.length == 1) {
          val blockCardIdx: Int = tokenOfUserInputToInt(tokens(0))

          if (tokens.length == 1) {
            if (!tokens(0).equals("take")) {
              if (blockCardIdx > 0) {
                cardsIdxTupleList = (blockCardIdx, 0) :: cardsIdxTupleList
              } else {
                println("Could not process input \"" + tokens(0) + "\"! Please only enter numbers!\n")
                getNewBlockUserInput
              }
            } else {
              println("MIES GESCHLUCKT!")
              cardsIdxTupleList = (-1, -1) :: cardsIdxTupleList
              controller.victimTakeAttackCards


            }
          } else {
            println("The number of indices is odd! Please only enter a even number of idices!\n")
            getNewBlockUserInput
          }
        }
      }
    }
    cardsIdxTupleList
  }

  def getNewBlockUserInput: Unit = {
    print("Cards you can choose to block " + controller.getPossibleBlockingCards + ":")
    checkBlockUserInput(readLine)
  }

  def fillConsole: Unit = {
    for (i <- 0 to 25) println()
  }

  def tokenizeUserInput(userInput: String): (String, String) = {
    val tokens: Array[String] = userInput.split(" ")

    println("tokens: " + tokens.mkString(", "));

    val commandParametersTuple: (String, String) = (tokens(0), tokens(1))

    return commandParametersTuple
  }

  override def update: Unit = print("")
}