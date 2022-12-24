package co.pvphub.operational.parsers

import co.pvphub.operational.CustomParser
import co.pvphub.operational.smartError
import co.pvphub.operational.util.stringRegex

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

    fun extractInner(lines: Array<String>): List<String> {
        // Need to extract ONLY what is inside the first brackets
        val joint = lines.joinToString("\n")
        var firstLine = -1
        var lastLine = -1
        var open = 0
        lines.forEachIndexed { index, line ->
            // Remove all string occurrences in this line
            val lineNoStr = line.replace(stringRegex, "")
            if (lineNoStr.contains("{") && firstLine == -1)
                firstLine = index
            open += lineNoStr.count { it == '{' }
            open -= lineNoStr.count { it == '}' }
            if (lineNoStr.contains("}") && index > lastLine)
                lastLine = index
        }
        if (open != 0) throw smartError(lines[0], "Check your brackets, counted $open closed brackets.")
        if (firstLine == -1 || lastLine == -1)
            throw smartError(lines[0], "Starting line or last line of code block not found. $firstLine, $lastLine")
        // Get the first line of the block
        return lines.toList().subList(firstLine, lastLine)
    }

    fun parseInner(lines: Array<String>) = CustomParser.parseLines(extractInner(lines).toTypedArray())

    abstract override fun parse(lines: Array<String>, parser: ParserContext): E

    override fun matches(string: String) = super.matches(string) && blockValid(string.split("\n").toTypedArray())

}