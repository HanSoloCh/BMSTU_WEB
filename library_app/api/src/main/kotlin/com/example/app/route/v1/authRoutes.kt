package com.example.app.route.v1

import com.example.domain.usecase.LoginUserUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

@Serializable
data class LoginRequest(
    val phone: String,
    val password: String
)

fun Route.authRoutes() {
    val loginUserUseCase: LoginUserUseCase by inject()

    post("/login") {
        val credentials = call.receive<LoginRequest>()

        val user = loginUserUseCase(credentials.phone, credentials.password)

        if (user != null) {
            call.respond(user)
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Ошибка авторизации")
        }
    }
}