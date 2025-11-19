package com.example.app.route.v1

import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.Route
import io.ktor.server.routing.route

fun Route.version1() {
    route("/v1") {
        swaggerUI(path = "", swaggerFile = "openapi/api_v1.yaml")
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
        authRoutes()
    }
}