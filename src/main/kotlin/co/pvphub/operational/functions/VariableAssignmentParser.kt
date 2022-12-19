package co.pvphub.operational.functions

import co.pvphub.operational.objects.ParsedInstruction
import co.pvphub.operational.parsers.InstructionParser
import co.pvphub.operational.smartError
import co.pvphub.operational.util.anyVarName
import co.pvphub.operational.variables.Types

class VariableAssignmentParser : InstructionParser(
    "$anyVarName(:\\s?\\w+)?\\s?=\\s?.+".toRegex()
) {

    override fun parse(lines: Array<String>): ParsedInstruction {
        val split = lines[0].split("\\s?=\\s?".toRegex())
        if (split.size < 2) throw smartError(
            lines[0],
            "Only got ${split.size} halves of assignment, are you using 'varName = value'?"
        )
        val name = split[0].replace("(:\\s?\\w+)".toRegex(), "")
        val strType = "(:\\s?\\w+)".toRegex().find(lines[0])?.value?.replace(":\\s?".toRegex(), "")
        val valueType = (if (strType != null) Types.byName(strType) else Types.byMatch(split[1]))

        // TODO we need to actually do this properly lolz
        val value = valueType?.translate(split[1]) ?: Types.match(split[1]) ?: throw smartError(lines[0], "Unknown type for '${split[1]}', please register it with the api!")
        if (valueType != null && value::class.java != valueType.clazz())
            throw smartError(
                lines[0],
                "Type mismatch, $name is declared as ${
                    valueType.clazz().simpleName
                }, but is initialized as a ${value::class.java.simpleName}."
            )
        return ParsedInstruction { it[name] = value }
    }

    fun <T, O> canCast(clazz: Class<T>, obj: O): Boolean {
        return try {
            clazz.cast(obj)
            true
        } catch (e: ClassCastException) {
            false
        }
    }

}