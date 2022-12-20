package co.pvphub.operational.util

import co.pvphub.operational.ParsingException
import co.pvphub.operational.parserError
import co.pvphub.operational.parsers.ParserContext
import co.pvphub.operational.variables.Types
import kotlin.math.abs
import kotlin.math.min

inline fun <T : Any> String.extract(startReg: String, endReg: String? = null, parse: String.() -> T? = { null }): T? {
    var str = replace(".*$startReg".toRegex(), "")
    endReg?.let { str = str.replace("${it}.*".toRegex(), "") }
    return parse(str)
}

fun String.removeInitialWhitespace(): String {
    var str = this
    while (str.startsWith(" ") && str.isNotEmpty())
        str = str.substring(1, str.length)
    return str
}

fun <T> compile(
    list: MutableList<String>,
    match: (String) -> T?,
    onMatch: (T, List<String>, Int) -> Unit
) {
    var stringBuilder = ""
    val initialSize = list.size
    var line = 0
    while (list.isNotEmpty()) {
        stringBuilder += "${if (stringBuilder.isEmpty()) "" else "\n"}${list[0].removeUnwanted()}"
        list.removeAt(0)
        match(stringBuilder)?.let {
            onMatch(it, stringBuilder.split("\n"), line)
            stringBuilder = ""
            line = initialSize - list.size + 2
        }
    }
    if (stringBuilder.isNotEmpty()) {
        throw parserError(ParserContext(line, stringBuilder.split("\n").toTypedArray()), "Unrecognized method or call.")
    }
}

fun String.type() = Types[this]

fun String.isGlobalVarName() = startsWith('[') && endsWith(']')

fun String.isValidVarName() = matches("\\[(\\w|\\d|\\.|::)+]|(\\w|\\d|\\.|::)+".toRegex())

fun String.removeComments() = replace("\\#.*".toRegex(), "")

fun String.removeEmptyNewLines() = replace("\n\n", "\n")

infix fun <T> String.typeOf(v: Class<T>) = this to v
infix fun <T> Pair<String, Class<T>>.withRegex(r: String) = Triple(first, second, r.toRegex())

fun String.removeUnwanted() = removeInitialWhitespace()
    .removeComments()
    .removeEmptyNewLines()

fun String.removeExtraWhitespace() = replace("  ", " ")

infix fun String.similar(other: String): Int {
    var points = 0
    val thisNoWhitespace = removeExtraWhitespace()
    val otherNoWhitespace = other.removeExtraWhitespace()
    val dif = abs(thisNoWhitespace.length - otherNoWhitespace.length)
    points -= dif
    repeat(min(thisNoWhitespace.length, otherNoWhitespace.length)) {
        val thisChar = thisNoWhitespace[it]
        val otherChar = otherNoWhitespace[it]
        if (thisChar.lowercase() != otherChar.lowercase()) {
            points -= 1
        }
    }
    return points
}