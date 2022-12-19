package co.pvphub.operational.parsers

import co.pvphub.operational.objects.ParsedFunction
import co.pvphub.operational.smartError
import co.pvphub.operational.util.extract
import co.pvphub.operational.util.localVarName

class FunctionParser : BlockParser<ParsedFunction?>(
    "fu?n\\s\\w+\\(($localVarName:\\s?\\w+(,\\s?$localVarName:\\s?\\w+)*)?\\)\\s?\\{(.+|\\n)+}".toRegex()
) {

    override fun parse(lines: Array<String>): ParsedFunction? {
        val valid = blockValid(lines)
        if (!valid) return null
        // Now we need to parse the body
        val name = lines[0].extract("fu?n\\s", "\\(") { this } ?: "fn::null"
        val argsAsList = lines[0].extract("fu?n\\s\\w+\\(", "\\)\\s?\\{") {
            if (this.isEmpty()) return@extract null
            split(",\\s?".toRegex())
        } ?: listOf()
        val args =
            argsAsList.map { it.split(":\\s?".toRegex()) }.map {
                if (it.size < 2) {
                    throw smartError(
                        it.joinToString(": "),
                        "Seems to be an invalid function declaration. Check the docs under 'functions' for help."
                    )
                }
                Pair(it[0], it[1])
            }
        return ParsedFunction(name, args.toMap())
    }

    override fun matches(string: String): Boolean {
        return super.matches(string) && blockValid(string.split("\n").toTypedArray())
    }

}