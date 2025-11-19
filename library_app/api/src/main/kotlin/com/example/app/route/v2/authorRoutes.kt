package com.example.app.route.v2

import com.example.app.config.hasRole
import com.example.app.util.getParam
import com.example.domain.enums.UserRole
import com.example.domain.model.AuthorModel
import com.example.domain.usecase.author.CreateAuthorUseCase
import com.example.domain.usecase.author.DeleteAuthorUseCase
import com.example.domain.usecase.author.ReadAuthorByIdUseCase
import com.example.domain.usecase.author.ReadAuthorByNameUseCase
import com.example.domain.usecase.author.UpdateAuthorUseCase
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

fun Route.authorRoutes() {
    val readAuthorByIdUseCase: ReadAuthorByIdUseCase by inject()
    val createAuthorUseCase: CreateAuthorUseCase by inject()
    val updateAuthorUseCase: UpdateAuthorUseCase by inject()
    val deleteAuthorUseCase: DeleteAuthorUseCase by inject()
    val readAuthorByNameUseCase: ReadAuthorByNameUseCase by inject()

    route("/authors") {
        get {
            val q = call.request.queryParameters["q"] ?: ""
            val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 0
            val size = call.request.queryParameters["size"]?.toIntOrNull() ?: 20

            val result = readAuthorByNameUseCase(q, page, size)
            val response = mapOf("content" to result)
            call.respond(response)
        }

        route("/{id}") {
            get {
                val authorId = call.getParam<UUID>("id", true) { UUID.fromString(it) }!!
                val author = readAuthorByIdUseCase(authorId)
                if (author == null) {
                    call.respond(HttpStatusCode.NotFound, "Author not found")
                } else {
                    call.respond(author)
                }
            }
        }

        authenticate("auth-jwt") {
            post {
                val principal = call.principal<JWTPrincipal>() ?: run {
                    call.respond(HttpStatusCode.Unauthorized, "Authentication required")
                    return@post
                }

                if (!principal.hasRole(UserRole.MODERATOR)) {
                    call.respond(HttpStatusCode.Forbidden, "Only moderators can create authors")
                    return@post
                }

                val author = call.receive<AuthorModel>()
                val createdAuthor = createAuthorUseCase(author)
                call.respond(HttpStatusCode.Created, createdAuthor)
            }

            route("/{id}") {
                patch {
                    val principal = call.principal<JWTPrincipal>() ?: run {
                        call.respond(HttpStatusCode.Unauthorized, "Authentication required")
                        return@patch
                    }

                    if (!principal.hasRole(UserRole.MODERATOR)) {
                        call.respond(HttpStatusCode.Forbidden, "Only moderators can update authors")
                        return@patch
                    }

                    val authorId = call.getParam<UUID>("id", true) { UUID.fromString(it) }!!
                    val authorUpdate = call.receive<AuthorModel>()
                    val updatedAuthor = authorUpdate.copy(id = authorId)
                    val result = updateAuthorUseCase(updatedAuthor)
                    call.respond(HttpStatusCode.OK, result)
                }

                delete {
                    val principal = call.principal<JWTPrincipal>() ?: run {
                        call.respond(HttpStatusCode.Unauthorized, "Authentication required")
                        return@delete
                    }

                    if (!principal.hasRole(UserRole.MODERATOR)) {
                        call.respond(HttpStatusCode.Forbidden, "Only moderators can delete authors")
                        return@delete
                    }

                    val authorId = call.getParam<UUID>("id", true) { UUID.fromString(it) }!!
                    deleteAuthorUseCase(authorId)
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}
