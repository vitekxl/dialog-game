package dialog.game.phrases.filters.inline.text

import dialog.game.phrases.filters.InlineTextPhraseFilter
import dialog.game.phrases.filters.labels.FilterLabel
import dialog.game.phrases.filters.labels.FilterLabelCollection
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tools.FiltersUtils
import java.lang.IllegalArgumentException


/**
 * [GETV][key=value][GETV][key1=value2]
 * [NOTV][key=value][NOTV][key2=value2]
 */
class GetVariableFilter(private val parameters: HashMap<String, Any?> ) :
    InlineTextPhraseFilter() {

    private val FilterLabelCollection = FilterLabelCollection();
    override val filterLabelsList: Array<FilterLabel> = arrayOf(FilterLabelCollection.GETV, FilterLabelCollection.NOTV)

    companion object{
        private val logger = LoggerFactory.getLogger(GetVariableFilter::class.java) as Logger

    }


    private fun filterText(itemText: String) : Boolean{
        val labels = FiltersUtils.getFilterLabelsTexts(itemText) ?: return true;
        labels.forEachIndexed(){ i, it ->
            val label = FilterLabelCollection.parse(it) ?: return@forEachIndexed
            if(label == FilterLabelCollection.GETV || label == FilterLabelCollection.NOTV) {
                val keysLabel = labels[i+1];
                if(!process(label, keysLabel)) return false
            }
        }
        return true
    }

    override fun filterText(itemText: String, count: Int): Boolean {
        return if(filterText(itemText)){
            logger.debug("TRUE : GETV = $itemText")
            true
        }else{
            logger.debug("FALSE: GETV = $itemText")
            false
        }
    }

    private fun process(label: FilterLabel, valuesLabel: String) : Boolean{
        val key = FiltersUtils.getParameterName(valuesLabel)
        val value = FiltersUtils.getParameterValue(valuesLabel)
        logger.info("${label.name}: $key=$value")
        return when (label) {
            FilterLabelCollection.NOTV -> parameters[key].toString() != value.toString()
            FilterLabelCollection.GETV -> parameters[key].toString() == value.toString()
            else -> throw IllegalArgumentException("$label is not recognised")
        }
    }


}