package co.pvphub.operational.objects

import co.pvphub.operational.variables.contexts.Context
import java.util.UUID

class ParsedListener(val name: String, val clazz: Class<*>) : RunnableFunction {
    private val funcName = "listener::$name::${UUID.randomUUID().toString().replace("-", "")}"

    override fun invoke(context: Context, vararg args: Any) {
        println("Event received: '$name'")
    }

    override fun args(): Map<String, Class<*>> = hashMapOf("event" to clazz)

    override fun name() = funcName

    override fun regex(): Regex = "fireEvent\\($name\\)".toRegex()
}