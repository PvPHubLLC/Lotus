package co.pvphub.operational.functions

import co.pvphub.operational.api.StringableFunction
import co.pvphub.operational.variables.contexts.Context
import java.util.*

class LoggingUtils {

    @StringableFunction
    fun println(context: Context) = println()

    @StringableFunction
    fun println(context: Context, message: String) = println(context.format(message))

    @StringableFunction
    fun print(context: Context, message: String) = print(context.format(message))

    @StringableFunction
    fun date(context: Context) = println(Date())

    @StringableFunction
    fun add(context: Context, first: Int, second: Int) : Int = first + second

    @StringableFunction
    fun printAdd(context: Context, first: Int, second: Int) = println(first + second)

}