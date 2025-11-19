package com.example.app.plugin

import com.example.app.exception.ConvertFailureException
import com.example.app.exception.MissingParametersException
import com.example.app.logger.LogLevel
import com.example.app.logger.Logger
import com.example.domain.exception.BaseDomainException
import com.example.domain.exception.BookNoAvailableCopiesException
import com.example.domain.exception.ModelDuplicateException
import com.example.domain.exception.ModelNotFoundException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond

fun Application.configureStatusPages() {
    install(StatusPages) {
        lateinit var infoMessage: String
        exception<MissingParametersException> { call, exception ->
            infoMessage = exception.message ?: MissingParametersException::class.java.canonicalName
            Logger.logAction(infoMessage, LogLevel.ERROR)

            call.respond(
                HttpStatusCode.BadRequest,
                infoMessage
            )
        }
        exception<ConvertFailureException> { call, exception ->
            infoMessage = exception.message ?: ConvertFailureException::class.java.canonicalName
            Logger.logAction(infoMessage, LogLevel.ERROR)
            call.respond(
                HttpStatusCode.BadRequest,
                infoMessage
            )
        }

        exception<BaseDomainException> { call, exception ->
            infoMessage = exception.message ?: "Unknown domain error"
            Logger.logAction(infoMessage, LogLevel.ERROR)
            when (exception) {
                is ModelNotFoundException -> call.respond(HttpStatusCode.NotFound, infoMessage)
                is ModelDuplicateException -> call.respond(HttpStatusCode.Conflict, infoMessage)
                is BookNoAvailableCopiesException -> call.respond(
                    HttpStatusCode.Conflict,
                    infoMessage
                )

                else -> call.respond(HttpStatusCode.BadRequest, infoMessage)
            }
        }
        exception<Throwable> { call, exception ->
            infoMessage = exception.cause?.message ?: exception.javaClass.canonicalName
            Logger.logAction(infoMessage, LogLevel.ERROR)
            call.respond(HttpStatusCode.BadRequest, infoMessage)
        }
    }
}
