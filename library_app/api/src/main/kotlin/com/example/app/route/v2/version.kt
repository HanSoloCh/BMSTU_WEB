package com.example.app.route.v2

import com.example.app.config.JwtService
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.Route
import io.ktor.server.routing.route

fun Route.version2() {
    route("/v2") {
        swaggerUI(path = "", swaggerFile = "openapi/api_v2.yaml")
        apuRoutes()
        authorRoutes()
        bbkRoutes()
        bookRoutes()
        publisherRoutes()
        userRoutes()
        reservationRoutes()
        queueRoutes()
        issuanceRoutes()
        favoriteRoutes()
        authRoutes(JwtService)
    }
}