package com.example.app.route.v2

import com.example.app.config.hasAnyRole
import com.example.app.util.getParam
import com.example.domain.enums.UserRole
import com.example.domain.model.IssuanceModel
import com.example.domain.usecase.issuance.CreateIssuanceUseCase
import com.example.domain.usecase.issuance.DeleteIssuanceUseCase
import com.example.domain.usecase.issuance.ReadIssuanceUseCase
import com.example.domain.usecase.issuance.UpdateIssuanceUseCase
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

fun Route.issuanceRoutes() {
    val readIssuanceUseCase by inject<ReadIssuanceUseCase>()
    val createIssuanceUseCase by inject<CreateIssuanceUseCase>()
    val updateIssuanceUseCase by inject<UpdateIssuanceUseCase>()
    val deleteIssuanceUseCase by inject<DeleteIssuanceUseCase>()

    route("/issuances") {
        authenticate("auth-jwt") {
            post {
                val principal = call.principal<JWTPrincipal>() ?: run {
                    call.respond(HttpStatusCode.Unauthorized, "Authentication required")
                    return@post
                }

                if (!principal.hasAnyRole(UserRole.READER, UserRole.LIBRARIAN, UserRole.MODERATOR)) {
                    call.respond(HttpStatusCode.Forbidden, "Insufficient permissions")
                    return@post
                }

                val issuance = call.receive<IssuanceModel>()
                val createdIssuance = createIssuanceUseCase(issuance)
                call.respond(HttpStatusCode.Created, createdIssuance)
            }

            get {
                val principal = call.principal<JWTPrincipal>() ?: run {
                    call.respond(HttpStatusCode.Unauthorized, "Authentication required")
                    return@get
                }

                if (!principal.hasAnyRole(UserRole.READER, UserRole.LIBRARIAN, UserRole.MODERATOR)) {
                    call.respond(HttpStatusCode.Forbidden, "Insufficient permissions")
                    return@get
                }

                val bookId = call.getParam<UUID>("bookId") { UUID.fromString(it) }
                val userId = call.getParam<UUID>("userId") { UUID.fromString(it) }
                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 0
                val size = call.request.queryParameters["size"]?.toIntOrNull() ?: 20

                val result = readIssuanceUseCase(bookId, userId, page, size)
                val response = mapOf("content" to result)
                call.respond(HttpStatusCode.OK, response)
            }

            route("/{id}") {
                patch {
                    val principal = call.principal<JWTPrincipal>() ?: run {
                        call.respond(HttpStatusCode.Unauthorized, "Authentication required")
                        return@patch
                    }

                    if (!principal.hasAnyRole(UserRole.LIBRARIAN, UserRole.MODERATOR)) {
                        call.respond(HttpStatusCode.Forbidden, "Insufficient permissions")
                        return@patch
                    }

                    val issuanceId = call.getParam<UUID>("id", true) { UUID.fromString(it) }!!
                    val issuance = call.receive<IssuanceModel>()
                    updateIssuanceUseCase(issuance.copy(id = issuanceId))
                    call.respond(HttpStatusCode.NoContent)
                }

                delete {
                    val principal = call.principal<JWTPrincipal>() ?: run {
                        call.respond(HttpStatusCode.Unauthorized, "Authentication required")
                        return@delete
                    }

                    if (!principal.hasAnyRole(UserRole.LIBRARIAN, UserRole.MODERATOR)) {
                        call.respond(HttpStatusCode.Forbidden, "Insufficient permissions")
                        return@delete
                    }

                    val issuanceId = call.getParam<UUID>("id", true) { UUID.fromString(it) }!!
                    deleteIssuanceUseCase(issuanceId)
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}
