package dialog.game.debug.record.phrase

import dialog.game.debug.record.service.GameRecorder
import dialog.game.debug.record.answer.RecordAnswers
import dialog.game.debug.record.service.RecordFileIO
import dialog.system.models.answer.Answer
import dialog.system.models.items.ADialogItem
import dialog.system.models.items.phrase.DebugFilteredPhrase
import dialog.system.models.items.runner.RunnerConfigurator
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import dialog.system.models.items.phrase.PhrasePrinter
import dialog.system.models.items.phrase.AnswerChooser
import java.util.Scanner


class GameRecordPhrase(id: String, phrases: Array<String>,  answers: Array<Answer>) : DebugFilteredPhrase(id, phrases, answers) {

    companion object {
        private val logger = LoggerFactory.getLogger(GameRecordPhrase::class.java) as Logger
    }

    override fun initFrom(source: ADialogItem) {
        super.initFrom(source)
        initialisirung()
    }

    init {
       initialisirung()
    }

    private fun readDescription(): String?{
        println("Enter the description:\n>")
        val input = Scanner(System.`in`)
        val stringInput = input.nextLine()
        if (stringInput == null || stringInput == "") {
            return null
        }
        return stringInput;
    }


    private fun initialisirung(){
        val runner = RunnerConfigurator.setDebugRunner(this);

        afterFilter = { a, p, _ ->
            val tmp = a.toMutableList()
            if (GameRecorder.isRecorded) {
                tmp.add(RecordAnswers.RECORD_STOP)
                tmp.add(RecordAnswers.RECORD_BREAK)
            } else {
                tmp.add(RecordAnswers.RECORD_START)
            }
            FilterResult(
                tmp.toTypedArray(),
                p
            )
        }

        updatePrinter {
            object : PhrasePrinter {
                override fun printTextDialog(text: String, answer: Array<Answer>) {
                    val state = if (GameRecorder.isRecorded) "Rec" else "Stop"
                    println("-----------------------------------")
                    println("RECORD STATE: " + GameRecorder.isRecorded)
                    println("-----------------------------------")

                    val tmp = answer.toMutableList()

                    tmp.removeAll(RecordAnswers.array())

                    it.printTextDialog(text, tmp.toTypedArray());

                    println("-----------------------------------")

                    for (i in answer.indices) {
                        if (answer[i].id.startsWith("dialog.game.debug.record"))
                            println("[${i + 1}] ${answer[i].text}\n")
                    }

                }
            }
        }

        updateAnswerChooser {
            object : AnswerChooser {
                override fun chooseAnswer(answers: Array<Answer>): Answer {
                    val res = it.chooseAnswer(answers);
                    if (res == RecordAnswers.RECORD_START) {
                        GameRecorder.startRecord()
                        runner.restart()
                    } else if (res == RecordAnswers.RECORD_STOP) {
                        val record =  GameRecorder.stopRecord()
                        if(record != null) {
                            record.description = readDescription() ?: record.id
                            RecordFileIO.safe(record)
                        }
                        else logger.error("Get Empty Record!")
                        runner.restart()
                    } else if (res == RecordAnswers.RECORD_BREAK) {
                        GameRecorder.breakRecord()
                        runner.restart()
                    } else {
                        if (GameRecorder.isRecorded) {
                            GameRecorder.recordStep(res)
                        }
                    }
                    return res;
                }
            }
        }
    }


}

