package com.example.app.route.v2

import com.example.app.config.hasRole
import com.example.app.util.getParam
import com.example.domain.enums.UserRole
import com.example.domain.model.QueueModel
import com.example.domain.usecase.queue.CreateQueueUseCase
import com.example.domain.usecase.queue.DeleteQueueUseCase
import com.example.domain.usecase.queue.ReadQueueUseCase
import com.example.domain.usecase.queue.UpdateQueueUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import java.util.UUID

fun Route.queueRoutes() {
    val readQueueUseCase by inject<ReadQueueUseCase>()
    val createQueueUseCase by inject<CreateQueueUseCase>()
    val updateQueueUseCase by inject<UpdateQueueUseCase>()
    val deleteQueueUseCase by inject<DeleteQueueUseCase>()

    route("/queues") {
        authenticate("auth-jwt") {
            post {
                val principal = call.principal<JWTPrincipal>() ?: run {
                    call.respond(HttpStatusCode.Unauthorized, "Authentication required")
                    return@post
                }

                if (!principal.hasRole(UserRole.READER)) {
                    call.respond(HttpStatusCode.Forbidden, "Insufficient permissions")
                    return@post
                }

                val queue = call.receive<QueueModel>()
                val createdQueue = createQueueUseCase(queue)
                call.respond(HttpStatusCode.Created, createdQueue)
            }

            get {
                val bookId = call.getParam<UUID>("bookId") { UUID.fromString(it) }
                val userId = call.getParam<UUID>("userId") { UUID.fromString(it) }
                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 0
                val size = call.request.queryParameters["size"]?.toIntOrNull() ?: 20

                val result = readQueueUseCase(bookId, userId, page, size)
                val response = mapOf("content" to result)
                call.respond(HttpStatusCode.OK, response)
            }

            route("/{id}") {
                patch {
                    val principal = call.principal<JWTPrincipal>() ?: run {
                        call.respond(HttpStatusCode.Unauthorized, "Authentication required")
                        return@patch
                    }

                    if (!principal.hasRole(UserRole.MODERATOR)) {
                        call.respond(HttpStatusCode.Forbidden, "Insufficient permissions")
                        return@patch
                    }

                    val queueId = call.getParam<UUID>("id", true) { UUID.fromString(it) }!!
                    val queue = call.receive<QueueModel>()
                    updateQueueUseCase(queue.copy(id = queueId))
                    call.respond(HttpStatusCode.NoContent)
                }

                delete {
                    val principal = call.principal<JWTPrincipal>() ?: run {
                        call.respond(HttpStatusCode.Unauthorized, "Authentication required")
                        return@delete
                    }

                    if (!principal.hasRole(UserRole.MODERATOR)) {
                        call.respond(HttpStatusCode.Forbidden, "Insufficient permissions")
                        return@delete
                    }

                    val queueId = call.getParam<UUID>("id", true) { UUID.fromString(it) }!!
                    deleteQueueUseCase(queueId)
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}
