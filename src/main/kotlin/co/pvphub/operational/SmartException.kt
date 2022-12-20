package co.pvphub.operational

import co.pvphub.operational.parsers.ParserContext
import kotlin.math.min

class SmartException(message: String?) : Throwable(message)

fun runtimeError(message: String) = SmartException(buildError(message))

fun parserError(context: ParserContext) = parserError(context, findProblem(context.lines))
fun parserError(context: ParserContext, msg: String) : SmartException {
    return SmartException(buildError(msg, context.line, context.lines.getOrNull(0)))
}

fun buildError(error: String, line: Int = -1, errorLine: String? = null) : String {
    var message = "\n Whoops! That wasn't meant to happen, looks like we've got an error!\n"
    message += "\n"
    if (line != -1 && errorLine != null) {
        message += " ${line}| ${errorLine}\n"
        message += "      ↳ $error"
        message += "\n"
    } else message += " $error"
    message += "\n"
    message += " " + "▁".repeat(min(100, message.length)) + "\n"
    return message
}

fun findProblem(lines: Array<String>) : String {
    val first = lines.getOrNull(0) ?: return "Unknown error! This is strange"
    if (first.contains("func(tion)?".toRegex())) {
        if (first.contains(".+\\s*=\\s*\\(.+\\)\\s*=".toRegex()))
            return "We don't support creating functions that way yet. Please use the fun keyword"
        return "To declare a function use the syntax: fu?n [name](param: Type...) { }"
    }
    return "Parsing error here, whatever could be the problem?"
}