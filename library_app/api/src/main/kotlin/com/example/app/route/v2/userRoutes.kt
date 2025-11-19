package com.example.app.route.v2

import com.example.app.config.hasRole
import com.example.app.util.getParam
import com.example.domain.enums.UserRole
import com.example.domain.model.UserModel
import com.example.domain.usecase.user.CreateUserUseCase
import com.example.domain.usecase.user.DeleteUserUseCase
import com.example.domain.usecase.user.ReadUserByIdUseCase
import com.example.domain.usecase.user.ReadUserByPhoneUseCase
import com.example.domain.usecase.user.UpdateUserUseCase
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

fun Route.userRoutes() {
    val readUserByIdUseCase by inject<ReadUserByIdUseCase>()
    val createUserUseCase by inject<CreateUserUseCase>()
    val updateUserUseCase by inject<UpdateUserUseCase>()
    val deleteUserUseCase by inject<DeleteUserUseCase>()
    val readUserByPhoneUseCase by inject<ReadUserByPhoneUseCase>()

    route("/users") {
        get {
            val q = call.request.queryParameters["q"] ?: ""
            val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 0
            val size = call.request.queryParameters["size"]?.toIntOrNull() ?: 20

            val result = readUserByPhoneUseCase(q, page, size)
            val response = mapOf("content" to result)
            call.respond(HttpStatusCode.OK, response)
        }

        route("/{id}") {
            get {
                val userId = call.getParam<UUID>("id", true) { UUID.fromString(it) }!!
                val user = readUserByIdUseCase(userId)
                if (user == null) {
                    call.respond(HttpStatusCode.NotFound, "User not found")
                } else {
                    call.respond(user)
                }
            }
        }
        authenticate("auth-jwt") {
            post {
                val user = call.receive<UserModel>()
                val createdUser = createUserUseCase(user)
                call.respond(HttpStatusCode.Created, createdUser)
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

                    val userId = call.getParam<UUID>("id", true) { UUID.fromString(it) }!!
                    val user = call.receive<UserModel>()
                    updateUserUseCase(user.copy(id = userId))
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

                    val userId = call.getParam<UUID>("id", true) { UUID.fromString(it) }!!
                    deleteUserUseCase(userId)
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}
