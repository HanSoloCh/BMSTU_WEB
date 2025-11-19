package com.example.app.util

import com.example.app.exception.ConvertFailureException
import com.example.app.exception.MissingParametersException
import io.ktor.server.application.ApplicationCall

suspend inline fun <reified T> ApplicationCall.getParam(
    param: String,
    required: Boolean = false,
    crossinline parser: (String) -> T
): T? {
    val value = parameters[param]
    return when {
        value == null && required -> throw MissingParametersException(param)
        value == null -> null
        else -> try {
            parser(value)
        } catch (e: IllegalArgumentException) {
            throw ConvertFailureException(param)
        }
    }
}