package co.pvphub.operational.variables

import co.pvphub.operational.variables.contexts.Context

open class VariableProvider<T : Any>(val provider: (Context) -> T?) {

    open fun get(context: Context) = provider(context)

    open fun getOrElse(context: Context, otherwise: T) = get(context) ?: otherwise

    open fun getOrThrow(context: Context, throwable: Throwable) = get(context) ?: throw throwable

}

class SimpleVariableProvider<T : Any>(
    val name: String
) : VariableProvider<T>({ it[name] }) {
    fun name() = name
}

fun <T : Any> varProvider(name: String) = SimpleVariableProvider<T>(name)