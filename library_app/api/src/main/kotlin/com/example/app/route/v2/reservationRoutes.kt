package com.example.app.route.v2

import com.example.app.config.hasRole
import com.example.app.util.getParam
import com.example.domain.enums.UserRole
import com.example.domain.model.ReservationModel
import com.example.domain.usecase.reservation.CreateReservationUseCase
import com.example.domain.usecase.reservation.DeleteReservationUseCase
import com.example.domain.usecase.reservation.ReadReservationUseCase
import com.example.domain.usecase.reservation.UpdateReservationUseCase
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

fun Route.reservationRoutes() {
    val readReservationUseCase by inject<ReadReservationUseCase>()
    val createReservationUseCase by inject<CreateReservationUseCase>()
    val updateReservationUseCase by inject<UpdateReservationUseCase>()
    val deleteReservationUseCase by inject<DeleteReservationUseCase>()

    route("/reservations") {
        authenticate("auth-jwt") {
            post {
                val principal = call.principal<JWTPrincipal>() ?: run {
                    call.respond(HttpStatusCode.Unauthorized, "Authentication required")
                    return@post
                }

                if (!principal.hasRole(UserRole.READER)) {
                    call.respond(HttpStatusCode.Forbidden, "Insufficient permissions")
                    return@post
                }

                val reservation = call.receive<ReservationModel>()
                val createdReservation = createReservationUseCase(reservation)
                call.respond(HttpStatusCode.Created, createdReservation)
            }

            get {
                val bookId = call.getParam<UUID>("bookId") { UUID.fromString(it) }
                val userId = call.getParam<UUID>("userId") { UUID.fromString(it) }
                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 0
                val size = call.request.queryParameters["size"]?.toIntOrNull() ?: 20

                val result = readReservationUseCase(bookId, userId, page, size)
                val response = mapOf("content" to result)
                call.respond(HttpStatusCode.OK, response)
            }

            route("/{id}") {
                patch {
                    val principal = call.principal<JWTPrincipal>() ?: run {
                        call.respond(HttpStatusCode.Unauthorized, "Authentication required")
                        return@patch
                    }

                    if (!principal.hasRole(UserRole.LIBRARIAN)) {
                        call.respond(HttpStatusCode.Forbidden, "Insufficient permissions")
                        return@patch
                    }

                    val reservationId = call.getParam<UUID>("id", true) { UUID.fromString(it) }!!
                    val reservation = call.receive<ReservationModel>()
                    updateReservationUseCase(reservation.copy(id = reservationId))
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

                    val reservationId = call.getParam<UUID>("id", true) { UUID.fromString(it) }!!
                    deleteReservationUseCase(reservationId)
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}
