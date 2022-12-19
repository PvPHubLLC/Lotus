package co.pvphub.operational.api

object Events {
    private val events = hashMapOf<String, Class<*>>()

    operator fun set(name: String, value: Class<*>) {
        events[name] = value
    }

    operator fun get(name: String) = events[name]

    fun all() = events.toMutableMap()
}