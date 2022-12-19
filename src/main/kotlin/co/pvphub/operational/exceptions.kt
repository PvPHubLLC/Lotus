package co.pvphub.operational

import kotlin.math.min

class ParsingException(val lines: List<String>, val line: Int) : Error("Error near line $line, near '${lines[0]}'.") {
    fun smart() : Error {
        // Need to look up a knowledgebase of common shit that goes wrong, might be best to use a web request?
        if (lines[0].matches("fu?n(ction)?.*".toRegex())) {
            return SmartError("", lines[0], line, "To declare a function: 'fu?n [name](param: Type...) { [...] }'")
        }

        return smartError(lines[0], "Something's wrong here, I don't think this method exists. Run methods() to see a list of methods.", line)
    }
}

class SmartError(path: String? = "unknown", val errorLine: String, line: Int = 0, message: String)
    : Error("\n" +
        "Whoops! We've got ourselves an error! (path: $path)" +
        "\n" +
        "\n $line| $errorLine" +
        "\n      ↳ $message" +
        "\n" +
        "\n" +
        "    " + "▁".repeat(min(320, message.length)) +
        "\n"
)

fun smartError(errorLine: String, message: String, line: Int = 0, path: String? = null) =
    SmartError(path, errorLine, line, message)