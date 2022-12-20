package co.pvphub.operational.parsers

import co.pvphub.operational.variables.contexts.Context

abstract class Parser<out T>(vararg regex: Regex) {
    protected val regex = arrayListOf(*regex)

    abstract fun parse(lines: Array<String>, parser: ParserContext) : T

    open fun matches(string: String) = regex.any { string.matches(it) }

}