package co.pvphub.operational.variables.contexts

import co.pvphub.operational.CustomParser

abstract class Context {
    private val variables = hashMapOf<String, Any>()

    open operator fun <T : Any> get(name: String) =
        try {
            val value = variables[formatVarName(name)]
            if (value != null) value as T
            else value
        } catch (e: ClassCastException) {
            null
        }

    fun any(name: String): Any? = get(name)

    open operator fun <T : Any> set(name: String, value: T?) {
        if (value == null) {
            variables.remove(name)
            return
        }
        variables[formatVarName(name)] = value
    }

    open fun formatVarName(initial: String) = initial

    open fun format(string: String) : String {
        var m = string
        // todo we need to call any possible funcs
        variables.forEach {
            m = m.replace("%${it.key}%", it.value.toString())
                .replace("%${it.key}(\\.type\\(\\)|::type)%".toRegex(), it.value::class.java.typeName)
        }
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