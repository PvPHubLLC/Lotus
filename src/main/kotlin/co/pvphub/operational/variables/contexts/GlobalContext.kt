package co.pvphub.operational.variables.contexts

import co.pvphub.operational.objects.ParsedFunction

class GlobalContext : Context() {

    fun func(name: String) = super.get<ParsedFunction>(name)

    fun findFunc(call: String) = super.all<ParsedFunction>()
        .firstOrNull { it.matches(call) }

    fun <T> match(regex: Regex, clazz: Class<T>) = items()
        .filter { it.key.matches(regex) }
        .values
        .filter { it::class.java == clazz }
        .map { it as T }
}