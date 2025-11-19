package com.example.app.exception

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond

suspend fun ApplicationCall.handleBadRequest(message: String) {
    respond(HttpStatusCode.BadRequest, mapOf("error" to message))
}

suspend fun ApplicationCall.handleNotFound(message: String) {
    this.respond(HttpStatusCode.NotFound, mapOf("error" to message))
}

suspend fun ApplicationCall.handleInternalError(message: String) {
    this.respond(HttpStatusCode.InternalServerError, mapOf("error" to message))
}

suspend fun ApplicationCall.handleUnauthorized(message: String) {
    this.respond(HttpStatusCode.Unauthorized, mapOf("error" to message))
}

suspend fun ApplicationCall.handleForbidden(message: String) {
    this.respond(HttpStatusCode.Forbidden, mapOf("error" to message))
}