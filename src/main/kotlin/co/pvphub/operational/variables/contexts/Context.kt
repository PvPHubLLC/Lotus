package co.pvphub.operational.variables.contexts

import co.pvphub.operational.CustomParser

abstract class Context {
    private val variables = hashMapOf<String, Any>()

    open operator fun <T : Any> get(name: String) =
        try {
            variables[formatVarName(name)] as T
        } catch (e: ClassCastException) {
            null
        }

    open operator fun <T : Any> set(name: String, value: T) {
        variables[formatVarName(name)] = value
    }

    open fun formatVarName(initial: String) = initial

    open fun format(string: String) : String {
        var m = string
        variables.forEach { m = m.replace("%${it.key}%", it.value.toString()) }
        return m
    }

    inline fun <reified T : Any> all() : List<T> {
        val items = (if (this is GlobalContext) items() else (this as LocalContext).global.items() + items())
            .values.filterIsInstance<T>().toMutableList()
        items += CustomParser.parsers.filterIsInstance<T>()
        return items
    }

    fun items() = variables.toMutableMap()
}