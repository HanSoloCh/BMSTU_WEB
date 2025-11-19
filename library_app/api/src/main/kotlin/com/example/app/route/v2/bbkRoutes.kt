package com.example.app.route.v2

import com.example.app.config.hasRole
import com.example.app.util.getParam
import com.example.domain.enums.UserRole
import com.example.domain.model.BbkModel
import com.example.domain.usecase.bbk.CreateBbkUseCase
import com.example.domain.usecase.bbk.DeleteBbkUseCase
import com.example.domain.usecase.bbk.ReadBbkByCodeUseCase
import com.example.domain.usecase.bbk.ReadBbkByIdUseCase
import com.example.domain.usecase.bbk.UpdateBbkUseCase
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

fun Route.bbkRoutes() {
    val readBbkByIdUseCase by inject<ReadBbkByIdUseCase>()
    val createBbkUseCase by inject<CreateBbkUseCase>()
    val updateBbkUseCase by inject<UpdateBbkUseCase>()
    val deleteBbkUseCase by inject<DeleteBbkUseCase>()
    val readBbkByCodeUseCase by inject<ReadBbkByCodeUseCase>()

    route("/bbks") {
        get {
            val q = call.request.queryParameters["q"] ?: ""
            val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 0
            val size = call.request.queryParameters["size"]?.toIntOrNull() ?: 20

            val result = readBbkByCodeUseCase(q, page, size)
            val response = mapOf("content" to result)
            call.respond(response)
        }

        route("/{id}") {
            get {
                val bbkId = call.getParam<UUID>("id", true) { UUID.fromString(it) }!!
                val bbk = readBbkByIdUseCase(bbkId)
                if (bbk == null) {
                    call.respond(HttpStatusCode.NotFound, "Bbk not found")
                } else {
                    call.respond(bbk)
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
                    call.respond(HttpStatusCode.Forbidden, "Only moderators can create BBK entries")
                    return@post
                }

                val bbk = call.receive<BbkModel>()
                val createdBbk = createBbkUseCase(bbk)
                call.respond(HttpStatusCode.Created, createdBbk)
            }

            patch {
                val principal = call.principal<JWTPrincipal>() ?: run {
                    call.respond(HttpStatusCode.Unauthorized, "Authentication required")
                    return@patch
                }

                if (!principal.hasRole(UserRole.MODERATOR)) {
                    call.respond(HttpStatusCode.Forbidden, "Only moderators can update BBK entries")
                    return@patch
                }

                val bbk = call.receive<BbkModel>()
                updateBbkUseCase(bbk)
                call.respond(HttpStatusCode.NoContent)
            }

            route("/{id}") {
                delete {
                    val principal = call.principal<JWTPrincipal>() ?: run {
                        call.respond(HttpStatusCode.Unauthorized, "Authentication required")
                        return@delete
                    }

                    if (!principal.hasRole(UserRole.MODERATOR)) {
                        call.respond(HttpStatusCode.Forbidden, "Only moderators can delete BBK entries")
                        return@delete
                    }

                    val bbkId = call.getParam<UUID>("id", true) { UUID.fromString(it) }!!
                    deleteBbkUseCase(bbkId)
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}
