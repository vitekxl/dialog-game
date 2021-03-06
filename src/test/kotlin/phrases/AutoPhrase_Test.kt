package phrases

import dialog.game.phrases.AutoPhrase
import dialog.system.models.answer.Answer
import org.junit.jupiter.api.Test
import tools.TestPhraseTools.Companion.createTestPhrase

class AutoPhrase_Test {
    @Test
    fun test_AutoPhrase(){
        val phrase =  createTestPhrase<AutoPhrase>(arrayOf(Answer("ok", "ans1") , Answer("error", "answ2") ))
        val res = phrase.run()
        assert(res.id == "ok")
    }
}