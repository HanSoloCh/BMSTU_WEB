package com.example.app.route.v1

import com.example.app.util.getParam
import com.example.domain.model.ReservationModel
import com.example.domain.usecase.reservation.CreateReservationUseCase
import com.example.domain.usecase.reservation.DeleteReservationUseCase
import com.example.domain.usecase.reservation.ReadReservationUseCase
import com.example.domain.usecase.reservation.UpdateReservationUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import java.util.UUID

fun Route.reservationRoutes() {
    val readReservationUseCase by inject<ReadReservationUseCase>()
    val createReservationUseCase by inject<CreateReservationUseCase>()
    val updateReservationUseCase by inject<UpdateReservationUseCase>()
    val deleteReservationUseCase by inject<DeleteReservationUseCase>()

    route("/reservation") {
        post {
            val reservation = call.receive<ReservationModel>()
            val createdId = createReservationUseCase(reservation)
            call.respond(HttpStatusCode.Created, createdId)
        }

        put {
            val reservation = call.receive<ReservationModel>()
            updateReservationUseCase(reservation)
            call.respond(HttpStatusCode.NoContent)
        }

        get {
            val bookId = call.getParam<UUID>("bookId") { UUID.fromString(it) }
            val userId = call.getParam<UUID>("userId") { UUID.fromString(it) }

            val result = readReservationUseCase(bookId, userId)
            call.respond(HttpStatusCode.OK, result)
        }

        route("/{id}") {
            delete {
                val reservationId = call.getParam<UUID>("id", true) { UUID.fromString(it) }!!
                deleteReservationUseCase(reservationId)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}