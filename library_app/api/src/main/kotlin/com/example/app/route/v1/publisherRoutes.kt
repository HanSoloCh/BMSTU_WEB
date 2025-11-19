package com.example.app.route.v1

import com.example.app.util.getParam
import com.example.domain.model.PublisherModel
import com.example.domain.usecase.publisher.CreatePublisherUseCase
import com.example.domain.usecase.publisher.DeletePublisherUseCase
import com.example.domain.usecase.publisher.ReadPublisherByIdUseCase
import com.example.domain.usecase.publisher.ReadPublisherByNameUseCase
import com.example.domain.usecase.publisher.UpdatePublisherUseCase
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

fun Route.publisherRoutes() {
    val readPublisherByIdUseCase by inject<ReadPublisherByIdUseCase>()
    val createPublisherUseCase by inject<CreatePublisherUseCase>()
    val updatePublisherUseCase by inject<UpdatePublisherUseCase>()
    val deletePublisherUseCase by inject<DeletePublisherUseCase>()

    route("/publisher") {
        post {
            val publisher = call.receive<PublisherModel>()
            val createdId = createPublisherUseCase(publisher)
            call.respond(HttpStatusCode.Created, createdId)
        }

        put {
            val publisher = call.receive<PublisherModel>()
            updatePublisherUseCase(publisher)
            call.respond(HttpStatusCode.NoContent)
        }

        route("/{id}") {
            get {
                val publisherId = call.getParam<UUID>("id", true) { UUID.fromString(it) }!!

                val author = readPublisherByIdUseCase(publisherId)
                if (author == null) {
                    call.respond(HttpStatusCode.NotFound, "Publisher not found")
                } else {
                    call.respond(author)
                }
            }
            delete {
                val publisherId = call.getParam<UUID>("id", true) { UUID.fromString(it) }!!
                deletePublisherUseCase(publisherId)
                call.respond(HttpStatusCode.NoContent)
            }
        }

        route("/by-name") {
            val readPublisherByNameUseCase: ReadPublisherByNameUseCase by inject()
            get {
                val name = call.getParam<String>("name", true) { it }!!
                val publisher = readPublisherByNameUseCase(name)
                call.respond(publisher)
            }
        }
    }
}