package co.pvphub.operational.variables

object Types {
    val types = hashMapOf<Class<*>, TypeAdapter<*>>(
        Boolean::class.java to BooleanAdapter(),
        String::class.java to StringAdapter(),
        Int::class.java to IntAdapter(),
        Long::class.java to LongAdapter(),
        Double::class.java to DoubleAdapter(),
        Char::class.java to CharAdapter(),
        arrayListOf<Any>()::class.java as Class<ArrayList<Any>> to ArrayListAdapter()
    )

    fun <T : Any> register(adapter: TypeAdapter<T>) {
        types[adapter.clazz()] = adapter
    }

    operator fun get(type: String) : Class<*>? {
        return types.values.firstOrNull { it.regex().toRegex().matches(type) }?.clazz()
    }

    inline fun <reified T> of() = types[T::class.java]

    fun byMatch(name: String) = types.values.firstOrNull { it.regex().toRegex().matches(name) }

    fun byName(name: String) = types.values.firstOrNull { it.name().contains(name) }

    fun <T : Any> translateToString(clazz: Class<T>) = types[clazz]?.regex()

    fun <T : Any> parse(value: String, clazz: Class<T>) = types[clazz]?.translate(value)

    fun match(value: String) : Any? {
        for (adapter in types.values) {
            if (value.matches(adapter.regex().toRegex())) {
                return adapter.translate(value)
            }
        }
        return null
    }

}