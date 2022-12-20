import co.pvphub.operational.CustomParser
import co.pvphub.operational.api.Events
import co.pvphub.operational.functions.DebugFunctions
import co.pvphub.operational.functions.GeneralFunctions
import co.pvphub.operational.functions.LoggingUtils
import co.pvphub.operational.functions.MathFunctions
import co.pvphub.operational.util.removeUnwanted
import co.pvphub.operational.variables.Types
import java.io.File

fun main() {
    CustomParser.registerFunctions(LoggingUtils())
    CustomParser.registerFunctions(DebugFunctions())
    CustomParser.registerFunctions(GeneralFunctions())
    CustomParser.registerFunctions(MathFunctions())

    Events["joinEvent"] = ""::class.java
    Events["randomEvent"] = ""::class.java

    val lines = File("./first.op").readLines()
    CustomParser.parse(lines.toTypedArray())
//    println(items.items().map { it.value }.filterIsInstance<ParsedFunction>().map { it.name })
}