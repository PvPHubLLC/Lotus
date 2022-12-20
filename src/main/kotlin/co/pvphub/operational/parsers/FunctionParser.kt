package co.pvphub.operational.parsers

import co.pvphub.operational.objects.ParsedFunction
import co.pvphub.operational.parserError
import co.pvphub.operational.smartError
import co.pvphub.operational.util.extract
import co.pvphub.operational.util.localVarName

class FunctionParser : BlockParser<ParsedFunction?>(
    "fu?n\\s\\w+\\(($localVarName:\\s?\\w+(,\\s?$localVarName:\\s?\\w+)*)?\\)\\s?\\{(.+|\\n)+}".toRegex()
) {

    override fun parse(lines: Array<String>, parser: ParserContext): ParsedFunction {
        val valid = blockValid(lines)
        if (!valid) throw parserError(parser, "Check your brackets, something isn't being closed")
        // Now we need to parse the body
        val name = lines[0].extract("fu?n\\s", "\\(") { this } ?: throw parserError(parser, "Unable to find valid function name.")
        val argsAsList = lines[0].extract("fu?n\\s\\w+\\(", "\\)\\s?\\{") {
            if (this.isEmpty()) return@extract null
            split(",\\s?".toRegex())
        } ?: listOf()
        val args =
            argsAsList.map { it.split(":\\s?".toRegex()) }.map {
                if (it.size < 2) {
                    throw parserError(parser, "Something is wrong with the argument list. Make sure it's in the format [varName]: Type")
                }
                Pair(it[0], it[1])
            }
        return ParsedFunction(name, args.toMap(), parser)
    }

    override fun matches(string: String): Boolean {
        return super.matches(string) && blockValid(string.split("\n").toTypedArray())
    }

}