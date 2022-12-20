package co.pvphub.operational.parsers

import co.pvphub.operational.objects.ParsedInstruction

abstract class InstructionParser(vararg regex: Regex) : Parser<Any>(*regex) {

    abstract override fun parse(lines: Array<String>, parser: ParserContext): ParsedInstruction

}