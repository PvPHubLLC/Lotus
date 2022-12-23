
fun main() {
    val regex = "((?!(\\\"))[^\"\\{]*\\{[^\"\\{]*(?!(\\\")))".toRegex()
    val string1 = "\"this is in a string {\""
    val string2 = "{{\"{\""
    val foundString1 = regex.findAll(string1)
    val foundString2 = regex.findAll(string2)
    println("${foundString1.count()} ${foundString2.count()}")
}