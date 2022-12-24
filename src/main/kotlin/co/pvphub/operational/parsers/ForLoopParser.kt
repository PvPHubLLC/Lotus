package co.pvphub.operational.parsers

import co.pvphub.operational.objects.ParsedFunction
import co.pvphub.operational.objects.ParsedInstruction
import co.pvphub.operational.objects.ParsedLoop
import co.pvphub.operational.parserError
import co.pvphub.operational.runtimeError
import co.pvphub.operational.util.anyVarName
import co.pvphub.operational.util.extract
import co.pvphub.operational.util.parenthesis
import co.pvphub.operational.util.removeInitialWhitespace
import co.pvphub.operational.variables.SimpleVariableProvider
import co.pvphub.operational.variables.StringAdapter
import co.pvphub.operational.variables.Types
import co.pvphub.operational.variables.VariableProvider

class ForLoopParser : BlockParser<ParsedLoop>(
    "fo?r\\s?\\((${anyVarName}|$parenthesis.+$parenthesis)\\)\\s*\\n?\\{(.+|\\n)+}".toRegex()
) {

    override fun parse(lines: Array<String>, parser: ParserContext): ParsedLoop {
        val value: String = lines[0].extract("fo?r\\s\\(?", "\\)?\\s*\\n?\\{") {
            this
        } ?: throw parserError(parser, "Unable to extract array/list variable to loop through.")
        // Check if it's a variable that was specified
        val timesToLoop: VariableProvider<Any> =
            if (anyVarName.matches(value.removeInitialWhitespace())) {
                VariableProvider { it.any(value.removeInitialWhitespace()) }
            } else VariableProvider { Types.of<String>()?.translate(value).toString().asIterable() }
        val block = parseInner(lines.toList().subList(1, lines.size - 1).toTypedArray())
        return ParsedLoop {
            val contextValue =
                timesToLoop.getOrThrow(it, parserError(parser, "The variable/value provided must be iterable."))
            if (contextValue !is Iterable<*>)
                throw parserError(parser, "The variable/value provided must be iterable. (${contextValue::class.java} provided)")
            var setName = "loopVal"
            for (loopValue in contextValue) {
                if (it.any(setName) != null)
                    setName = "loopVal::${value}"
                it[setName] = loopValue as Any
                block.forEach { i ->
                    if (i is ParsedInstruction) {
                        i.invoke(it)
                    }
                }
                it[setName] = null
            }
        }
    }

    override fun matches(string: String): Boolean {
        return super.matches(string) && blockValid(string.split("\n").toTypedArray())
    }

}