package co.pvphub.operational.parsers

import co.pvphub.operational.api.Events
import co.pvphub.operational.objects.ParsedListener
import co.pvphub.operational.parserError
import co.pvphub.operational.util.extract

class ListenerParser : BlockParser<ParsedListener?>(
    "on\\<\\w+\\>\\s?\\{(.+|\\n)+}".toRegex()
) {

    override fun parse(lines: Array<String>, parser: ParserContext): ParsedListener? {
        val valid = blockValid(lines)
        if (!valid) return null
        val eventName = lines[0].extract("on\\<", "\\>\\s?\\{") { this } ?: throw parserError(parser, "Unable to parse event name.")
        val eventClass = Events[eventName] ?: throw parserError(parser, "Unknown or unregistered event name '$eventName' (Doesn't point to a class)")
        return ParsedListener(eventName, eventClass)
    }

}