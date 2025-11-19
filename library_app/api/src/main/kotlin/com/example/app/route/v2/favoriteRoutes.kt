package com.example.app.route.v2

import com.example.app.config.hasAnyRole
import com.example.app.util.getParam
import com.example.domain.enums.UserRole
import com.example.domain.usecase.favorite.CreateFavoriteUseCase
import com.example.domain.usecase.favorite.DeleteFavoriteUseCase
import com.example.domain.usecase.favorite.ReadFavoriteByUserIdUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject
import java.util.UUID

@Serializable
data class FavoriteResponse(
    val userId: @Contextual UUID,
    val bookId: @Contextual UUID
)

fun Route.favoriteRoutes() {
    val readFavoriteByUserIdUseCase by inject<ReadFavoriteByUserIdUseCase>()
    val createFavoriteUseCase by inject<CreateFavoriteUseCase>()
    val deleteFavoriteUseCase by inject<DeleteFavoriteUseCase>()

    route("/user/{userId}/favorites/{bookId}") {
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

                val userId = call.getParam<UUID>("userId", true) { UUID.fromString(it) }!!
                val bookId = call.getParam<UUID>("bookId", true) { UUID.fromString(it) }!!
                createFavoriteUseCase(userId, bookId)
                val response = FavoriteResponse(userId, bookId)
                call.respond(HttpStatusCode.Created, response)
            }

            delete {
                val principal = call.principal<JWTPrincipal>() ?: run {
                    call.respond(HttpStatusCode.Unauthorized, "Authentication required")
                    return@delete
                }

                if (!principal.hasAnyRole(UserRole.READER, UserRole.LIBRARIAN, UserRole.MODERATOR)) {
                    call.respond(HttpStatusCode.Forbidden, "Insufficient permissions")
                    return@delete
                }

                val userId = call.getParam<UUID>("userId", true) { UUID.fromString(it) }!!
                val bookId = call.getParam<UUID>("bookId", true) { UUID.fromString(it) }!!
                deleteFavoriteUseCase(userId, bookId)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }

    route("/user/{userId}/favorites") {
        authenticate("auth-jwt") {
            get {
                val principal = call.principal<JWTPrincipal>() ?: run {
                    call.respond(HttpStatusCode.Unauthorized, "Authentication required")
                    return@get
                }

                if (!principal.hasAnyRole(UserRole.READER, UserRole.LIBRARIAN, UserRole.MODERATOR)) {
                    call.respond(HttpStatusCode.Forbidden, "Insufficient permissions")
                    return@get
                }

                val userId = call.getParam<UUID>("userId", true) { UUID.fromString(it) }!!
                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 0
                val size = call.request.queryParameters["size"]?.toIntOrNull() ?: 20

                val result = readFavoriteByUserIdUseCase(userId, page, size)
                val response = mapOf("content" to result)
                call.respond(HttpStatusCode.OK, response)
            }
        }
    }
}
