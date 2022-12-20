package co.pvphub.operational.functions

import co.pvphub.operational.api.StringableFunction
import co.pvphub.operational.variables.contexts.Context
import kotlin.math.pow

class MathFunctions {

    @StringableFunction(regex = ["(::first)\\s?\\+\\s?(::second)"])
    fun plus(context: Context, first: Int, second: Int) = first + second

    @StringableFunction(regex = ["(::first)\\s?\\+\\s?(::second)"])
    fun plusAssign(context: Context, first: Int, second: Int) {
        // Todo need to be able to get first in `context`
    }

    @StringableFunction(regex = ["(::first)\\s?\\-\\s?(::second)"])
    fun minus(context: Context, first: Int, second: Int) = first - second

    @StringableFunction(regex = ["(::first)\\s?\\*\\s?(::second)"])
    fun mult(context: Context, first: Int, second: Int) = first * second

    @StringableFunction(regex = ["(::first)\\s?\\/\\s?(::second)"])
    fun div(context: Context, first: Int, second: Int) = first / second

    @StringableFunction(regex = ["(::first)\\s?\\%\\s?(::second)"])
    fun mod(context: Context, first: Int, second: Int) = first % second

    @StringableFunction(regex = ["(::num)\\^(::pow)"])
    fun pow(context: Context, num: Int, pow: Int) = num.toDouble().pow(pow)

    @StringableFunction
    fun add(context: Context, num: Int, other: Int) = num + other

}