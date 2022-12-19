package co.pvphub.operational.objects

import co.pvphub.operational.parsers.Parser
import co.pvphub.operational.smartError
import co.pvphub.operational.util.typeRegex
import co.pvphub.operational.variables.Types
import co.pvphub.operational.variables.contexts.Context
import co.pvphub.operational.variables.contexts.GlobalContext
import co.pvphub.operational.variables.contexts.LocalContext

class ParsedFunction(val name: String, args: Map<String, String>) : RunnableFunction {
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
                if (it.second == null) throw smartError("${it.first}: [unknown type]", "Unknown type for ${it.first} in function $name")
                parameters[it.first] = it.second!!.clazz()
            }
        var counter = 0
        args.forEach { (k, v) ->
            val type = Types.byName(v) ?: throw smartError("$k: $v", "Unknown type '$v' in function $name")
            regexBuilder += "${if (counter != 0) "\\,\\s?" else ""}${type.regex()}"
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
        if (args.size != parameters.size) throw smartError("", "Expected ${parameters.size} parameters, got $args.")
        val context = LocalContext(if (context is LocalContext) context.global else context as GlobalContext)
        var index = 0
        parameters.forEach { (name, s) ->
            if (args[index]::class.java != s)
                throw smartError(
                    "${s.typeName} != ${args[index]::class.java.typeName}",
                    "Excepted type ${s.typeName} at param $index of $name, got ${args[index]::class.java.typeName} instead."
                )
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