package co.pvphub.operational.util

val parenthesis = "[\"\'`]".toRegex()
val blockStart = "(\\s|\\n)*\\{(.+|\\n)+}".toRegex()
val stringRegex = "($parenthesis)(?:(?=(\\\\?))\\2.)*?\\1".toRegex()
val localVarName = "[^\\-\\.]\\D(\\w)+".toRegex()
val globalVarName = "\\[$localVarName]".toRegex()
val anyVarName = "($localVarName|$globalVarName)".toRegex()