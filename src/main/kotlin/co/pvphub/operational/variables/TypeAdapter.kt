package co.pvphub.operational.variables

interface TypeAdapter<T : Any> {
    fun name() : List<String>
    fun clazz() : Class<T>
    fun regex() : String
    fun translate(str: String) : T?
}