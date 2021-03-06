package dialog.game.phrases.collections

import dialog.system.models.answer.Answer
import dialog.system.models.answer.AnswerType
import dialog.system.models.items.phrase.SimplePhrase


class PhraseCollection {
    companion object{
        public fun plugExitPhrase(id: String) : SimplePhrase {
            return SimplePhrase(
                id,
                "PLUG",
                arrayOf(
                    Answer(
                        "exit",
                        "go back",
                        AnswerType.EXIT
                    )
                )
            )
        }
    }
}