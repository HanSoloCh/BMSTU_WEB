package com.example.app.route.v2

import com.example.app.config.JwtService
import com.example.app.exception.handleInternalError
import com.example.app.exception.handleUnauthorized
import com.example.domain.model.UserModel
import com.example.domain.usecase.LoginUserUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

@Serializable
data class AuthRequest(
    val phoneNumber: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val token: String,
    val user: UserModel
)

fun Route.authRoutes(jwtService: JwtService) {
    val loginUserUseCase: LoginUserUseCase by inject()

    route("auth") {
        // Логин
        post("login") {
            try {
                val request = call.receive<AuthRequest>()

                val user = loginUserUseCase(request.phoneNumber, request.password);
                if (user == null) {
                    call.handleUnauthorized("Invalid email or password")
                    return@post
                }

                val token = jwtService.generateToken(user, user.role)
                val response = AuthResponse(
                    token = token,
                    user = UserModel(
                        id = user.id,
                        email = user.email,
                        name = user.name,
                        surname = user.surname,
                        secondName = user.secondName,
                        password = user.password,
                        phoneNumber = user.phoneNumber,
                        role = user.role
                    )
                )

                call.respond(HttpStatusCode.OK, response)
            } catch (e: Exception) {
                call.handleInternalError("Failed to login: ${e.message}")
            }
        }
    }
}