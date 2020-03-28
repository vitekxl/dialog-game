package phrases

import models.Answer
import models.items.phrase.FilteredPhrase
import phrases.configurator.FilteredPhraseConfigurator

class AutoPhrase(id: String, phrases: Array<String>,  answers: Array<Answer>) : FilteredPhrase(id, phrases, answers){
    init {
        FilteredPhraseConfigurator(this).count().auto()
    }
}