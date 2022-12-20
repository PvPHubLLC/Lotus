package co.pvphub.operational.objects

import co.pvphub.operational.variables.contexts.Context

open class ParsedInstruction(val exec: (context: Context) -> Unit) {

    operator fun invoke(context: Context) {
        exec(context)
    }

}