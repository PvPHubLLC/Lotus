package co.pvphub.operational.parsers

import co.pvphub.operational.CustomParser
import co.pvphub.operational.objects.ParsedInstruction
import co.pvphub.operational.objects.RunnableFunction
import co.pvphub.operational.util.compile

abstract class BlockParser<E>(vararg regex: Regex) : Parser<E>(*regex) {

    fun blockValid(lines: Array<String>) : Boolean {
        var open = 0
        for (line in lines) {
            // TODO Need to make sure isn't in a string or anything
            open += line.count { it == '{' }
            open -= line.count { it == '}' }
        }
        return open == 0
    }

    fun extractInner(lines: Array<String>) : List<String> {
        // Need to extract ONLY what is inside the first brackets
        val joint = lines.joinToString("\n")
        var open = 0
        return listOf()
    }

    fun parseInner(lines: Array<String>, parser: ParserContext) = CustomParser.parseLines(lines)

    abstract override fun parse(lines: Array<String>, parser: ParserContext): E

    override fun matches(string: String) = super.matches(string) && blockValid(string.split("\n").toTypedArray())

}