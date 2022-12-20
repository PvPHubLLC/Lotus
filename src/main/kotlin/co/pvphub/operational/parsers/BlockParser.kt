package co.pvphub.operational.parsers

abstract class BlockParser<E>(vararg regex: Regex) : Parser<E>(*regex) {

    fun blockValid(lines: Array<String>) : Boolean {
        var open = 0
        for (line in lines) {
            // TODO Need to make sure isn't in a string or anything
            open += line.count { it == '{' }
            open -= line.count { it == '}' }
        }
        return open == 0
    }

    abstract override fun parse(lines: Array<String>, parser: ParserContext): E

    override fun matches(string: String) = super.matches(string) && blockValid(string.split("\n").toTypedArray())

}