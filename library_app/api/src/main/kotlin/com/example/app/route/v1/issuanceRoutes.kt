package com.example.app.route.v1

import com.example.app.util.getParam
import com.example.domain.model.IssuanceModel
import com.example.domain.usecase.issuance.CreateIssuanceUseCase
import com.example.domain.usecase.issuance.DeleteIssuanceUseCase
import com.example.domain.usecase.issuance.ReadIssuanceUseCase
import com.example.domain.usecase.issuance.UpdateIssuanceUseCase
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

fun Route.issuanceRoutes() {
    val readIssuanceUseCase by inject<ReadIssuanceUseCase>()
    val createIssuanceUseCase by inject<CreateIssuanceUseCase>()
    val updateIssuanceUseCase by inject<UpdateIssuanceUseCase>()
    val deleteIssuanceUseCase by inject<DeleteIssuanceUseCase>()

    route("/issuance") {
        post {
            val issuance = call.receive<IssuanceModel>()
            val createdId = createIssuanceUseCase(issuance)
            call.respond(HttpStatusCode.Created, createdId)
        }

        put {
            val issuance = call.receive<IssuanceModel>()
            updateIssuanceUseCase(issuance)
            call.respond(HttpStatusCode.NoContent)
        }

        get {
            val bookId = call.getParam<UUID>("bookId") { UUID.fromString(it) }
            val userId = call.getParam<UUID>("userId") { UUID.fromString(it) }

            val result = readIssuanceUseCase(bookId, userId)
            call.respond(HttpStatusCode.OK, result)
        }

        route("/{id}") {
            delete {
                val issuanceId = call.getParam<UUID>("id", true) { UUID.fromString(it) }!!
                deleteIssuanceUseCase(issuanceId)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}