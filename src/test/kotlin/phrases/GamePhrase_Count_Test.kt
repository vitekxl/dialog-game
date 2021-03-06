package phrases

import dialog.game.phrases.AutoPhrase
import dialog.game.phrases.GamePhrase
import dialog.system.models.answer.Answer
import org.junit.jupiter.api.Test
import dialog.game.phrases.collections.AnswerChooserCollection
import tools.TestPhraseTools.Companion.createTestPhrase
import tools.TestPrinter

class GamePhrase_Count_Test {
    @Test
    fun test_GamePhrase_answer(){
        val phrase =  createTestPhrase<AutoPhrase>(
            arrayOf(
                Answer("1", "[1] first") ,
                Answer("*", "[*] other"),
                Answer("not 2", "[!2] not second"),
                Answer("2", "[2] second")
            )
        )
        phrase.answerChooser = AnswerChooserCollection.first();

        val first = phrase.run()
        val second = phrase.run()
        val third = phrase.run()
        val fourth = phrase.run()
        assert(first.id == "1")
        assert(second.id == "2")
        assert(third.id == "*")
        assert(fourth.id == "*")
    }

    @Test
    fun test_GamePhrase_phrase(){
        val phrase =  createTestPhrase<GamePhrase>(
            arrayOf( "[*] other", "[1] phrase 1", "[!2] not phrase 3", "[2] phrase 2", "[!2] not phrase 3"),
            arrayOf(
                Answer("1", "first")
            )
        )
        phrase.answerChooser = AnswerChooserCollection.first();
        val printer = TestPrinter();
        phrase.phrasePrinter = printer;

        val first = phrase.run()
        assert(printer.lastPhrase == "phrase 1")

        val second = phrase.run()
        assert(printer.lastPhrase == "phrase 2")

        val third = phrase.run()
        assert(printer.lastPhrase == "other")

        val fourth = phrase.run()
        assert(printer.lastPhrase == "other")
    }

    @Test
    fun test_operators_less_lessEqual(){
        val phrase =  createTestPhrase<GamePhrase>(
            arrayOf(
            Answer("1", "[<2] 1"),
            Answer("2", "[<=2] 2"),
            Answer("3", "[<=3] 3")
        )
        )
        phrase.answerChooser = AnswerChooserCollection.first();
        val list = arrayListOf<String>()
        for (i in 1 .. 3){
            list.add(phrase.run().id)
        }
        assert(list[0] == "1")
        assert(list[1] == "2")
        assert(list[2] == "3")

    }

    @Test
    fun test_operators_more_moreEqual(){
        val phrase =  createTestPhrase<GamePhrase>(
            arrayOf(
                Answer("3", "[>2] 3"),
                Answer("2", "[>1] 2"),
                Answer("1", "[>=1] 1")
            )
        )
        phrase.answerChooser = AnswerChooserCollection.first();
        val list = arrayListOf<String>()
        for (i in 1 .. 3){
            list.add(phrase.run().id)
        }
        assert(list[0] == "1")
        assert(list[1] == "2")
        assert(list[2] == "3")
    }
}