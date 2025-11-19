package com.example.app.route.v2

import com.example.app.config.hasRole
import com.example.app.util.getParam
import com.example.domain.enums.UserRole
import com.example.domain.model.ApuModel
import com.example.domain.usecase.apu.CreateApuUseCase
import com.example.domain.usecase.apu.DeleteApuUseCase
import com.example.domain.usecase.apu.ReadApuByIdUseCase
import com.example.domain.usecase.apu.ReadApuByTermUseCase
import com.example.domain.usecase.apu.UpdateApuUseCase
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

fun Route.apuRoutes() {
    val readApuByIdUseCase by inject<ReadApuByIdUseCase>()
    val createApuUseCase by inject<CreateApuUseCase>()
    val updateApuUseCase by inject<UpdateApuUseCase>()
    val deleteApuUseCase by inject<DeleteApuUseCase>()
    val readApuByTermUseCase by inject<ReadApuByTermUseCase>()

    route("/apus") {
        get {
            val q = call.request.queryParameters["q"] ?: ""
            val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 0
            val size = call.request.queryParameters["size"]?.toIntOrNull() ?: 20

            val result = readApuByTermUseCase(q, page, size)
            val response = mapOf("content" to result)
            call.respond(response)
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
        }

        authenticate("auth-jwt") {
            post {
                val principal = call.principal<JWTPrincipal>() ?: run {
                    call.respond(HttpStatusCode.Unauthorized, "Authentication required")
                    return@post
                }

                if (!principal.hasRole(UserRole.MODERATOR)) {
                    call.respond(HttpStatusCode.Forbidden, "Only moderators can create APU entries")
                    return@post
                }

                val apu = call.receive<ApuModel>()
                val createdApu = createApuUseCase(apu)
                call.respond(HttpStatusCode.Created, createdApu)
            }

            route("/{id}") {
                patch {
                    val principal = call.principal<JWTPrincipal>() ?: run {
                        call.respond(HttpStatusCode.Unauthorized, "Authentication required")
                        return@patch
                    }

                    if (!principal.hasRole(UserRole.MODERATOR)) {
                        call.respond(HttpStatusCode.Forbidden, "Only moderators can update APU entries")
                        return@patch
                    }

                    val apuId = call.getParam<UUID>("id", true) { UUID.fromString(it) }!!
                    val apuUpdate = call.receive<ApuModel>()
                    val updatedApu = apuUpdate.copy(id = apuId)
                    val result = updateApuUseCase(updatedApu)
                    call.respond(HttpStatusCode.OK, result)
                }

                delete {
                    val principal = call.principal<JWTPrincipal>() ?: run {
                        call.respond(HttpStatusCode.Unauthorized, "Authentication required")
                        return@delete
                    }

                    if (!principal.hasRole(UserRole.MODERATOR)) {
                        call.respond(HttpStatusCode.Forbidden, "Only moderators can delete APU entries")
                        return@delete
                    }

                    val apuId = call.getParam<UUID>("id", true) { UUID.fromString(it) }!!
                    deleteApuUseCase(apuId)
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}
