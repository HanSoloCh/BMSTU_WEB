package com.example.app.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.domain.enums.UserRole
import com.example.domain.model.UserModel
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.config.ApplicationConfig
import java.util.Date
import java.util.UUID

object JwtService {
    fun generateToken(user: UserModel, role: UserRole): String {
        val secret = "your-super-secret-key-change-this-in-production"
        val issuer = "your-app-issuer"
        val audience = "your-app-audience"

        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("userId", user.id.toString())
            .withClaim("email", user.email)
            .withClaim("role", role.name)
            .withExpiresAt(Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
            .sign(Algorithm.HMAC256(secret))
    }
}

fun JWTPrincipal.hasRole(requiredRole: UserRole): Boolean {
    return this.payload.getClaim("role").asString() == requiredRole.value
}

fun JWTPrincipal.hasAnyRole(vararg requiredRoles: UserRole): Boolean {
    val userRole = this.payload.getClaim("role").asString()
    return requiredRoles.any { it.value == userRole }
}

fun JWTPrincipal.getUserId(): UUID {
    return UUID.fromString(this.payload.getClaim("userId").asString())
}
