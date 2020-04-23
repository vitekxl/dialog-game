package minigames.tea.phrase

import game.Game
import game.GameData
import minigames.tea.TeaGame
import minigames.tea.tools.TeaGameUtils
import models.Answer
import models.items.phrase.FilteredPhrase
import models.items.phrase.PhrasePrinter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import phrases.configurator.FilteredPhraseConfigurator
import phrases.collections.PrinterCollection

class TeaGamePhrase (id: String, phrases: Array<String>, answers : Array<Answer>) : FilteredPhrase(id, phrases, answers){

    private val logger = LoggerFactory.getLogger(TeaGamePhrase::class.java) as Logger

    init {
        FilteredPhraseConfigurator(this).count().applyPhrases()

        after = after@{
            val answ = TeaGameUtils.answerToFlower(it) ?: return@after
            logger.info("selected Flower is ${answ.name}")
            TeaGame.currentTea.addFlower(answ)
        }
        before =  {
            logger.info("reset = ${GameData.boolGameVar("game_tea_restart")}")
            if(GameData.boolGameVar("game_tea_restart")){
                resetCount()
                TeaGame.reset()
                GameData.gameVariables["game_tea_restart"] = false;
            }

        }

        this.phrasePrinter = object : PhrasePrinter {
            override fun printTextDialog(text: String, answer: Array<Answer>) {
                PrinterCollection.defTextPrinter(text);
                println(TeaGameUtils.getLegend())
                PrinterCollection.defAnswersPrinter(answer)
            }
        }
    }
}