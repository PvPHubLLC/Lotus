package co.pvphub.operational.util

import co.pvphub.operational.variables.Types

fun <T : Any> Class<T>.typeRegex() = Types.translateToString(this) ?: "$parenthesis.*$parenthesis"