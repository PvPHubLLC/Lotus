package co.pvphub.operational.parsers

import co.pvphub.operational.objects.ParsedLoop
import co.pvphub.operational.parserError
import co.pvphub.operational.runtimeError
import co.pvphub.operational.util.anyVarName
import co.pvphub.operational.util.extract
import co.pvphub.operational.util.removeInitialWhitespace
import co.pvphub.operational.variables.SimpleVariableProvider
import co.pvphub.operational.variables.VariableProvider

class RepeatLoopParser : BlockParser<ParsedLoop>(
    "(rep(eat)?)\\s*\\(\\d+\\)\\s*\\n?\\{(.+|\\n)+}".toRegex()
) {

    override fun parse(lines: Array<String>, parser: ParserContext): ParsedLoop {
        val value: String = lines[0].extract("(rep(eat)?)\\s*\\(", "\\)?\\s*\\n?\\{") {
            this
        } ?: throw parserError(parser, "Please specify the amount of times you want to loop.")
        // Check if it's a variable that was specified
        val timesToLoop: VariableProvider<Int> = if (anyVarName.matches(value.removeInitialWhitespace())) {
            SimpleVariableProvider(value.removeInitialWhitespace())
        } else VariableProvider { value.removeInitialWhitespace().toInt() }

        return ParsedLoop {
            val times = timesToLoop.getOrThrow(it, runtimeError("Invalid integer provided for repeat loop, $value"))
            repeat(times) { i ->
                // todo exec body
                println(i)
            }
        }
    }

}