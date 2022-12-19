package co.pvphub.operational.api

@Target(AnnotationTarget.FUNCTION)
annotation class StringableFunction(
    val regex: Array<String> = []
)
