
fun main() {
    val regex = "([\"'])(?:(?=(\\\\?))\\2.)*?\\1".toRegex()
    val string1 = "\"this is in a string\\\" {\""
    val foundString1 = regex.findAll(string1)
    foundString1.forEach { println(it.value) }
}