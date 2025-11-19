package com.example.app.route.v1

import com.example.app.util.getParam
import com.example.domain.model.ApuModel
import com.example.domain.usecase.apu.CreateApuUseCase
import com.example.domain.usecase.apu.DeleteApuUseCase
import com.example.domain.usecase.apu.ReadApuByIdUseCase
import com.example.domain.usecase.apu.ReadApuByTermUseCase
import com.example.domain.usecase.apu.UpdateApuUseCase
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

fun Route.apuRoutes() {
    val readApuByIdUseCase by inject<ReadApuByIdUseCase>()
    val createApuUseCase by inject<CreateApuUseCase>()
    val updateApuUseCase by inject<UpdateApuUseCase>()
    val deleteApuUseCase by inject<DeleteApuUseCase>()
    val readApuByTermUseCase by inject<ReadApuByTermUseCase>()

    route("/apu") {
        post {
            val apu = call.receive<ApuModel>()
            val createdId = createApuUseCase(apu)
            call.respond(HttpStatusCode.Created, createdId)
        }

        put {
            val apu = call.receive<ApuModel>()
            updateApuUseCase(apu)
            call.respond(HttpStatusCode.NoContent)
        }

        route("/{id}") {
            get {
                val apuId = call.getParam<UUID>("id", true) { UUID.fromString(it) }!!

                val apu = readApuByIdUseCase(apuId)
                if (apu == null) {
                    call.respond(HttpStatusCode.NotFound, "Apu not found")
                } else {
                    call.respond(apu)
                }
            }
            delete {
                val apuId = call.getParam<UUID>("id", true) { UUID.fromString(it) }!!
                deleteApuUseCase(apuId)
                call.respond(HttpStatusCode.NoContent)
            }
        }

        route("/by-term") {
            get {
                val name = call.getParam<String>("term", true) { it }!!
                val apu = readApuByTermUseCase(name)
                call.respond(apu)
            }
        }
    }
}