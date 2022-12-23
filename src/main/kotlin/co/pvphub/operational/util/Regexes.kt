package co.pvphub.operational.util

val parenthesis = "[\"\'`]".toRegex()
val blockStart = "(\\s|\\n)*\\{(.+|\\n)+}".toRegex()
val localVarName = "[^\\-\\.]\\D(\\w)+".toRegex()
val globalVarName = "\\[$localVarName]".toRegex()
val anyVarName = "($localVarName|$globalVarName)".toRegex()