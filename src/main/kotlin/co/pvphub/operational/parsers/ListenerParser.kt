package co.pvphub.operational.parsers

import co.pvphub.operational.api.Events
import co.pvphub.operational.objects.ParsedListener
import co.pvphub.operational.util.extract

class ListenerParser : BlockParser<ParsedListener?>(
    "on\\<\\w+\\>\\s?\\{(.+|\\n)+}".toRegex()
) {

    override fun parse(lines: Array<String>): ParsedListener? {
        val valid = blockValid(lines)
        if (!valid) return null
        val eventName = lines[0].extract("on\\<", "\\>\\s?\\{") { this } ?: throw Error("Couldn't parse event name!")
        val eventClass = Events[eventName] ?: throw Error("Invalid event name '$eventName'")
        return ParsedListener(eventName, eventClass)
    }

}