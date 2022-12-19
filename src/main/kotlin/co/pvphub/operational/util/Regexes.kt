package co.pvphub.operational.util

val parenthesis = "[\"\'`]".toRegex()
val localVarName = "\\w+".toRegex()
val globalVarName = "\\[$localVarName]".toRegex()
val anyVarName = "($localVarName|$globalVarName)".toRegex()