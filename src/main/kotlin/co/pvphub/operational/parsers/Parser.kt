package co.pvphub.operational.parsers

abstract class Parser<out T>(vararg regex: Regex) {
    protected val regex = arrayListOf(*regex)

    abstract fun parse(lines: Array<String>) : T

    open fun matches(string: String) = regex.any { string.matches(it) }

}