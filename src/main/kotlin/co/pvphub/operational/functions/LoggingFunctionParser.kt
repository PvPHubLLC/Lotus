package co.pvphub.operational.functions

import co.pvphub.operational.objects.ParsedInstruction
import co.pvphub.operational.parsers.InstructionParser
import co.pvphub.operational.util.extract
import co.pvphub.operational.util.parenthesis

class LoggingFunctionParser : InstructionParser(
    "log\\s?($parenthesis.+$parenthesis|\\($parenthesis.+$parenthesis\\))".toRegex()
) {
    override fun parse(lines: Array<String>): ParsedInstruction {
        var message = lines[0].extract("log\\s?\\(?$parenthesis", "$parenthesis\\)?") { this }
        return ParsedInstruction {
            it.items().forEach { (t, u) -> message = message?.replace("%$t%", u.toString()) }
            println(message)
        }
    }
}