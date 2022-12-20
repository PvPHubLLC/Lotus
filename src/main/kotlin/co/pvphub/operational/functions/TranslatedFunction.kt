package co.pvphub.operational.functions

import co.pvphub.operational.objects.ParsedInstruction
import co.pvphub.operational.objects.RunnableFunction
import co.pvphub.operational.parserError
import co.pvphub.operational.parsers.InstructionParser
import co.pvphub.operational.parsers.ParserContext
import co.pvphub.operational.runtimeError
import co.pvphub.operational.util.anyVarName
import co.pvphub.operational.util.removeInitialWhitespace
import co.pvphub.operational.util.removeUnwanted
import co.pvphub.operational.util.typeRegex
import co.pvphub.operational.variables.SimpleVariableProvider
import co.pvphub.operational.variables.Types
import co.pvphub.operational.variables.VariableProvider
import co.pvphub.operational.variables.contexts.Context
import co.pvphub.operational.variables.varProvider
import java.lang.reflect.Method

class TranslatedFunction(regex: Regex, val method: Method, val objInstance: Any) : InstructionParser(regex),
    RunnableFunction {

    override fun invoke(context: Context, vararg args: Any) {
        // Check if the arguments to see if any are variable names
        val newArgs = arrayListOf<Any>()
        args.forEach {
            newArgs += if (it is SimpleVariableProvider<*>) {
                val value: Any = it.getOrThrow(context, runtimeError("Variable ${it.name} is not defined."))
                value
            } else it
        }
        try {
            method.invoke(objInstance, context, *newArgs.toTypedArray())
        } catch (e: IllegalArgumentException) {
            throw runtimeError("Argument type mismatch for method ${name()}" +
                    "\n  Expected: ${
                        method.parameterTypes.toList().subList(1, method.parameterCount)
                            .joinToString(", ") { it.typeName }
                    }" +
                    "\n  Provided: ${newArgs.joinToString(", ") { it::class.javaObjectType.typeName }}"
            )
        }
    }

    override fun parse(lines: Array<String>, parser: ParserContext): ParsedInstruction {
        // Presuming it's on one line for now

        val split = lines[0].split("${name()}(\\s*|\\s?\\()".toRegex())
        if (split.size <= 1)
            throw parserError(parser, "Can't split function call into name and arguments!")
        var argString = split[1]
        if (argString.startsWith("(")) argString = argString.replaceFirst("\\(+".toRegex(), "")
        if (argString.endsWith(")")) argString = argString.replace("\\)+".toRegex(), "")

        val args = arrayListOf<Any>()
        var counter = 0
        method.parameters.forEach { p ->
            // Always miss out arg 1, since it's the context.
            if (counter >= 1) {
                val typeRegex = "(${p.type.typeRegex()}|$anyVarName)".toRegex()
                val extracted =
                    typeRegex.find(argString) ?: throw parserError(parser, "Can't parse the function arguments!")
                argString = argString.replaceFirst(typeRegex, "")
                    .replaceFirst(",\\s*".toRegex(), "")
                if (anyVarName.matches(extracted.value.removeInitialWhitespace())) {
                    args += varProvider<Any>(extracted.value.removeInitialWhitespace())
                } else args += Types.parse(extracted.value.removeInitialWhitespace(), p.type) as Any
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