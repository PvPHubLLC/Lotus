package co.pvphub.operational.functions

import co.pvphub.operational.objects.ParsedInstruction
import co.pvphub.operational.objects.RunnableFunction
import co.pvphub.operational.parsers.InstructionParser
import co.pvphub.operational.util.typeRegex
import co.pvphub.operational.variables.Types
import co.pvphub.operational.variables.contexts.Context
import java.lang.reflect.Method

class TranslatedFunction(regex: Regex, val method: Method, val objInstance: Any) : InstructionParser(regex),
    RunnableFunction {

    override fun invoke(context: Context, vararg args: Any) {
        method.invoke(objInstance, context, *args)
    }

    override fun parse(lines: Array<String>): ParsedInstruction {
        // Presuming it's on one line for now

        val split = lines[0].split("\\s|\\(".toRegex())
        if (split.size <= 1) throw Error("Can't split function call into call and args!")
        var afterCall = split.subList(1, split.size).joinToString(" ")

        val args = arrayListOf<Any>()
        var counter = 0
        method.parameters.forEach { p ->
            // Always miss out arg 1, since its the context.
            if (counter >= 1) {
                val typeRegex = p.type.typeRegex().toRegex()
                val extracted =
                    typeRegex.find(afterCall) ?: throw Error("Can't parse the function arguments!")
                afterCall = afterCall.replaceFirst(typeRegex, "")
                args += Types.parse(extracted.value, p.type) as Any
            }
            counter++
        }

        return ParsedInstruction { this(it, *args.toTypedArray()) }
    }

    override fun args(): Map<String, Class<*>> =
        method.parameters.toMutableList().subList(1, method.parameterCount).associate { it.name to it.type }

    override fun name(): String = method.name
    override fun regex(): Regex = this.regex.joinToString("|").toRegex()
}