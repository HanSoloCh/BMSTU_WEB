package com.example.app.route.v2

import com.example.app.config.hasRole
import com.example.app.util.getParam
import com.example.domain.enums.UserRole
import com.example.domain.model.PublisherModel
import com.example.domain.usecase.publisher.CreatePublisherUseCase
import com.example.domain.usecase.publisher.DeletePublisherUseCase
import com.example.domain.usecase.publisher.ReadPublisherByIdUseCase
import com.example.domain.usecase.publisher.ReadPublisherByNameUseCase
import com.example.domain.usecase.publisher.UpdatePublisherUseCase
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

fun Route.publisherRoutes() {
    val readPublisherByIdUseCase by inject<ReadPublisherByIdUseCase>()
    val createPublisherUseCase by inject<CreatePublisherUseCase>()
    val updatePublisherUseCase by inject<UpdatePublisherUseCase>()
    val deletePublisherUseCase by inject<DeletePublisherUseCase>()
    val readPublisherByNameUseCase by inject<ReadPublisherByNameUseCase>()

    route("/publishers") {
        authenticate("auth-jwt") {
            post {
                val principal = call.principal<JWTPrincipal>() ?: run {
                    call.respond(HttpStatusCode.Unauthorized, "Authentication required")
                    return@post
                }

                if (!principal.hasRole(UserRole.MODERATOR)) {
                    call.respond(HttpStatusCode.Forbidden, "Insufficient permissions")
                    return@post
                }

                val publisher = call.receive<PublisherModel>()
                val createdPublisher = createPublisherUseCase(publisher)
                call.respond(HttpStatusCode.Created, createdPublisher)
            }

            get {
                val q = call.request.queryParameters["q"] ?: ""
                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 0
                val size = call.request.queryParameters["size"]?.toIntOrNull() ?: 20

                val result = readPublisherByNameUseCase(q, page, size)
                val response = mapOf("content" to result)
                call.respond(HttpStatusCode.OK, response)
            }

            route("/{id}") {
                get {
                    val publisherId = call.getParam<UUID>("id", true) { UUID.fromString(it) }!!
                    val publisher = readPublisherByIdUseCase(publisherId)
                    if (publisher == null) {
                        call.respond(HttpStatusCode.NotFound, "Publisher not found")
                    } else {
                        call.respond(publisher)
                    }
                }

                patch {
                    val principal = call.principal<JWTPrincipal>() ?: run {
                        call.respond(HttpStatusCode.Unauthorized, "Authentication required")
                        return@patch
                    }

                    if (!principal.hasRole(UserRole.MODERATOR)) {
                        call.respond(HttpStatusCode.Forbidden, "Insufficient permissions")
                        return@patch
                    }

                    val publisherId = call.getParam<UUID>("id", true) { UUID.fromString(it) }!!
                    val publisher = call.receive<PublisherModel>()
                    updatePublisherUseCase(publisher.copy(id = publisherId))
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

                    val publisherId = call.getParam<UUID>("id", true) { UUID.fromString(it) }!!
                    deletePublisherUseCase(publisherId)
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}
