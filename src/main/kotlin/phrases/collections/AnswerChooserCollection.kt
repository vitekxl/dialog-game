package phrases.collections

import models.Answer
import models.items.phrase.AnswerChooser

class AnswerChooserCollection {
    companion object{
        public fun auto() : AnswerChooser {
            return object : AnswerChooser {
                override fun chooseAnswer(answers: Array<Answer>): Answer {
                    return answers[0];
                }
            }
        }
    }
}