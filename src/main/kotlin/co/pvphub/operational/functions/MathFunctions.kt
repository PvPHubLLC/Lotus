package co.pvphub.operational.functions

import co.pvphub.operational.api.StringableFunction
import co.pvphub.operational.variables.contexts.Context

class MathFunctions {

    @StringableFunction(regex = ["(::first)\\s?\\+\\s?(::second)"])
    fun add(context: Context, first: Int, second: Int) = first + second

}