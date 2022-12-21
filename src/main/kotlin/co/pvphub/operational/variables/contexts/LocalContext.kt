package co.pvphub.operational.variables.contexts

import co.pvphub.operational.util.isGlobalVarName

class LocalContext(val global: GlobalContext) : Context() {

    override fun <T : Any> get(name: String): T? {
        return if (name.isGlobalVarName()) {
            global[name]
        } else {
            super.get(name)
        }
    }

    override operator fun <T : Any> set(name: String, value: T?) {
        if (name.isGlobalVarName()) {
            global[name] = value
        } else {
            super.set(name, value)
        }
    }

    override fun format(string: String) = global.format(super.format(string))

}