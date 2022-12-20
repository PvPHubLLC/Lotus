package co.pvphub.operational.objects

import co.pvphub.operational.parserError
import co.pvphub.operational.parsers.Parser
import co.pvphub.operational.parsers.ParserContext
import co.pvphub.operational.runtimeError
import co.pvphub.operational.smartError
import co.pvphub.operational.util.anyVarName
import co.pvphub.operational.util.typeRegex
import co.pvphub.operational.variables.Types
import co.pvphub.operational.variables.contexts.Context
import co.pvphub.operational.variables.contexts.GlobalContext
import co.pvphub.operational.variables.contexts.LocalContext

class ParsedFunction(val name: String, args: Map<String, String>, parser: ParserContext) : RunnableFunction {
    private val invocationRegex: Regex
    private val parameters = linkedMapOf<String, Class<*>>()
    private val instructions = arrayListOf<ParsedInstruction>()

    init {
        var regexBuilder = name
        regexBuilder += when (args.size) {
            0 -> "\\s?\\(\\)"
            1 -> "\\s?\\(?"
            else -> "\\s?\\("
        }
        // Now build arguments
        args.map { it.key to Types.byName(it.value) }
            .forEach {
                if (it.second == null)
                    throw parserError(parser, "Unknown type for ${it.first}, read as null")
                parameters[it.first] = it.second!!.clazz()
            }
        var counter = 0
        args.forEach { (k, v) ->
            val type = Types.byName(v) ?:
            throw parserError(parser, "Unknown type for parameter $k: $v. Run types() for a list of valid types.")
            regexBuilder += "${if (counter != 0) "\\,\\s?" else ""}(${type.regex()}|$anyVarName)"
            counter++
        }
        if (args.size == 1) regexBuilder += "\\)?"
        if (args.size > 1) regexBuilder += "\\)"
        invocationRegex = regexBuilder.toRegex()
    }

    fun matches(invocationString: String): Boolean {
        // Just need to make sure this string matches this number of args
        return invocationString.matches(invocationRegex)
    }

    override operator fun invoke(context: Context, vararg args: Any) {
        if (args.size != parameters.size)
            throw runtimeError("Expected ${parameters.size} parameters, only provided $args")
        val context = LocalContext(if (context is LocalContext) context.global else context as GlobalContext)
        var index = 0
        parameters.forEach { (name, s) ->
            if (args[index]::class.java != s)
                throw runtimeError("Expected type ${s.typeName} at param $index of $name, instead received ${args[index]::class.java.typeName}")
            // Add variable to context with name as identifier
            context[name] = args[index]
            index++
        }
        // Invoke the function
        println("invokin")
        instructions.forEach { it.invoke(context) }
    }

    override fun args(): Map<String, Class<*>> = parameters.toMutableMap()

    override fun regex() = invocationRegex
    override fun name() = name

}