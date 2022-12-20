package co.pvphub.operational.objects

import co.pvphub.operational.variables.contexts.Context

class ParsedLoop(run: (Context) -> Unit) : ParsedInstruction({ run(it) })