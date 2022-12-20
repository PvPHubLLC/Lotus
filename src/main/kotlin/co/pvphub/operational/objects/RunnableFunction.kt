package co.pvphub.operational.objects

import co.pvphub.operational.functions.TranslatedFunction
import co.pvphub.operational.variables.contexts.Context

interface RunnableFunction {
    operator fun invoke(context: Context, vararg args: Any)
    fun args(): Map<String, Class<*>> = hashMapOf()
    fun name(): String
    fun regex(): Regex
    fun returnType() : Class<*>? = null
    fun toString(format: String) =
        format.replace("%name%", name())
            .replace("%args%", args().map { a -> "${a.key}: ${a.value.simpleName}" }.joinToString(", "))
            .replace("%regex%", regex().toString())
}