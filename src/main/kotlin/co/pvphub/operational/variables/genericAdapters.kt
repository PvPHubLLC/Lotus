package co.pvphub.operational.variables

import co.pvphub.operational.util.parenthesis

class StringAdapter : TypeAdapter<String> {
    override fun name() = listOf("String")
    override fun clazz() = String::class.java
    override fun regex() = "$parenthesis.*$parenthesis"
    override fun translate(str: String) = str.substring(1, str.length - 1)
}
class CharAdapter : TypeAdapter<Char> {
    override fun name() = listOf("Char")
    override fun clazz() = Char::class.javaObjectType
    override fun regex() = "\\'.\\'"
    override fun translate(str: String) : Char? {
        val arr = str.substring(1, str.length - 1).toCharArray()
        if (arr.size > 1) return null
        return arr[0]
    }
}
class IntAdapter : TypeAdapter<Int> {
    override fun name() = listOf("Int")
    override fun clazz() = Int::class.javaObjectType
    override fun regex() = "-?\\d+"
    override fun translate(str: String) = str.toIntOrNull()
}
class LongAdapter : TypeAdapter<Long> {
    override fun name() = listOf("Long")
    override fun clazz() = Long::class.javaObjectType
    override fun regex() = "-?\\d{1,19}L?"
    override fun translate(str: String) = str.replace("L?".toRegex(), "").toLongOrNull()
}
class DoubleAdapter : TypeAdapter<Double> {
    override fun name() = listOf("Double")
    override fun clazz() = Double::class.javaObjectType
    override fun regex() = "-?\\d*\\.\\d+"
    override fun translate(str: String) = str.toDoubleOrNull()
}