package co.pvphub.operational.functions

import co.pvphub.operational.api.StringableFunction
import co.pvphub.operational.objects.RunnableFunction
import co.pvphub.operational.util.similar
import co.pvphub.operational.variables.Types
import co.pvphub.operational.variables.contexts.Context
import co.pvphub.operational.variables.contexts.GlobalContext
import co.pvphub.operational.variables.contexts.LocalContext

class DebugFunctions {

    @StringableFunction
    fun methods(context: Context) {
        methods(context, "%name%(%args%) | %regex%")
    }

    @StringableFunction
    fun methods(context: Context, format: String) {
        println("█ Debug/methods")
        context.all<RunnableFunction>().forEach { println("▏ " + it.toString(format)) }
    }

    @StringableFunction
    fun types(context: Context) {
        println("█ Debug/types")
        Types.types.forEach { (t, u) -> println("▏ " + u.name() + ": " + t.typeName) }
    }

    @StringableFunction
    fun similarMethods(context: Context, search: String) {
        similarMethods(context, search, "%name%(%args%) | %regex%")
    }

    @StringableFunction
    fun similarMethods(context: Context, search: String, format: String) {
        val allMethods = context.all<RunnableFunction>()
            .filter { it.name().similar(search) >= (-search.length * 0.5) }
        println("█ Debug/similarMethods")
        allMethods.forEach { println("▏ " + it.toString(format)) }
    }

    @StringableFunction
    fun vars(context: Context) {
        val items = (if (context is GlobalContext) context.items() else (context as LocalContext).items())
            .filter { it.value !is RunnableFunction }
        println("█ Debug/vars")
        items.forEach { (t, u) -> println("▏ $t: ${u::class.java.simpleName} = $u") }
    }

    @StringableFunction
    fun welcome(context: Context) {
        println("\n" +
                "  ███████████████████████████████████████\n" +
                "  █                                     █\n" +
                "  █          Welcome to Lotus!          █\n" +
                "  █                                     █\n" +
                "\n" +
                "  ▏ Run the function methods()           ▏\n" +
                "  ▏ to view all available methods.       ▏\n" +
                "\n" +
                "  ▏ Stuck? Go to https://lotuslang.com   ▏\n" +
                "\n"
        )
    }

}