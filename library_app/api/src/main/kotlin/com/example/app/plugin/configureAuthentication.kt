package com.example.app.plugin

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.config.ApplicationConfig

fun Application.configureAuthentication() {
    val jwtSecret = "your-super-secret-key-change-this-in-production"
    val jwtIssuer = "your-app-issuer"
    val jwtAudience = "your-app-audience"

    install(Authentication) {
        jwt("auth-jwt") {
            realm = "Access to protected endpoints"
            verifier(
                JWT.require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtIssuer)
                    .build()
            )
            validate { credential ->
                try {
                    if (credential.payload.audience.contains(jwtAudience) && credential.payload.issuer == jwtIssuer) {
                        JWTPrincipal(credential.payload)
                    } else {
                        null
                    }
                } catch (_: Exception) {
                    null
                }
            }
        }
    }
}