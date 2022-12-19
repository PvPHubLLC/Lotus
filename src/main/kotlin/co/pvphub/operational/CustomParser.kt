package co.pvphub.operational

import co.pvphub.operational.api.StringableFunction
import co.pvphub.operational.functions.LoggingFunctionParser
import co.pvphub.operational.functions.TranslatedFunction
import co.pvphub.operational.functions.VariableAssignmentParser
import co.pvphub.operational.objects.ParsedInstruction
import co.pvphub.operational.objects.ParsedListener
import co.pvphub.operational.objects.RunnableFunction
import co.pvphub.operational.parsers.FunctionParser
import co.pvphub.operational.parsers.ListenerParser
import co.pvphub.operational.parsers.Parser
import co.pvphub.operational.util.compile
import co.pvphub.operational.util.removeUnwanted
import co.pvphub.operational.util.typeRegex
import co.pvphub.operational.variables.contexts.GlobalContext
import co.pvphub.operational.variables.contexts.LocalContext

object CustomParser {

    val global = GlobalContext()
    val parsers = arrayListOf<Parser<*>>(
        FunctionParser(),
        VariableAssignmentParser(),
        LoggingFunctionParser(),
        ListenerParser(),
    )

    fun parse(lines: Array<String>) : GlobalContext {
        val items = parseLines(lines)
        items.forEach {
            if (it is ParsedInstruction) {
                // Add to initialize clause
                it(global)
            } else if (it is RunnableFunction) {
                global[it.name()] = it
            }
        }
        return global
    }

    fun fireEvent(name: String, vararg data: Any) {
        val context = LocalContext(global)
        context["event"] = data
        val listeners: List<ParsedListener> = global.match("listener::$name::.+".toRegex(), ParsedListener::class.java).toList()
        listeners.forEach {
            it.invoke(context)
        }
    }

    fun parseLines(lines: Array<String>) : List<Any> {
        val items = arrayListOf<Any>()
        val error = compile(lines.toMutableList(), { parsers.firstOrNull { p -> p.matches(it.removeUnwanted()) } }) { parser, args ->
            val parsed = parser.parse(args.toTypedArray())
            parsed?.let {
                items += it
            }
        }
        error?.let { throw it.smart() }
        return items
    }

    fun registerFunctions(clazz: Any) {
        clazz::class.java.declaredMethods.filter { it.annotations.any { a -> a is StringableFunction } }
            .forEach {
                val regex = (it.annotations.find { a -> a is StringableFunction } as StringableFunction).regex
                if (regex.isEmpty()) {
                    // We need to magically generate our own function regex
                    var strBuilder = it.name
                    strBuilder += when(it.parameterCount) {
                        1 -> "\\s?\\(\\)"
                        2 -> "\\s?\\(?"
                        else -> "\\s?\\("
                    }
                    // Now build arguments
                    var counter = 0
                    it.parameters.forEach { p ->
                        if (counter >= 1) {
                            strBuilder += "${if (counter != 1) "\\,\\s?" else ""}${p.type.typeRegex()}"
                        }
                        counter++
                    }
                    if (it.parameterCount == 2) strBuilder += "\\)?"
                    if (it.parameterCount > 2) strBuilder += "\\)"

                    parsers += TranslatedFunction(strBuilder.toRegex(), it, clazz)
                } else {
                    var builtRegex = regex.joinToString("|")
                    // We need to replace the regex's mentions of variable
                    var counter = 0
                    it.parameters.forEach { p ->
                        builtRegex = builtRegex.replace("\\(::(${p.name}|arg$counter)\\)".toRegex(), p.type.typeRegex())
                        counter++
                    }
                    // Somehow we need a way to extract the methods
                    parsers += TranslatedFunction(builtRegex.toRegex(), it, clazz)
                }
            }
    }

}