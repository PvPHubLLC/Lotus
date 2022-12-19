package co.pvphub.operational.functions

import co.pvphub.operational.CustomParser
import co.pvphub.operational.api.StringableFunction
import co.pvphub.operational.variables.contexts.Context

class GeneralFunctions {

    @StringableFunction
    fun fireEvent(context: Context, name: String) {
        CustomParser.fireEvent(name)
    }

    @StringableFunction
    fun fireEvent(context: Context, name: String, data: List<Any>) {
        CustomParser.fireEvent(name, *data.toTypedArray())
    }

}