package com.example.app.route.v1

import com.example.app.util.getParam
import com.example.domain.model.QueueModel
import com.example.domain.usecase.queue.CreateQueueUseCase
import com.example.domain.usecase.queue.DeleteQueueUseCase
import com.example.domain.usecase.queue.GetQueueUseCase
import com.example.domain.usecase.queue.ReadQueueUseCase
import com.example.domain.usecase.queue.UpdateQueueUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import java.util.UUID

fun Route.queueRoutes() {
    val readQueueUseCase by inject<ReadQueueUseCase>()
    val createQueueUseCase by inject<CreateQueueUseCase>()
    val updateQueueUseCase by inject<UpdateQueueUseCase>()
    val deleteQueueUseCase by inject<DeleteQueueUseCase>()
    val getQueueUseCase by inject<GetQueueUseCase>()

    route("/queue") {
        post {
            val queue = call.receive<QueueModel>()
            val createdId = createQueueUseCase(queue)
            call.respond(HttpStatusCode.Created, createdId)
        }

        put {
            val queue = call.receive<QueueModel>()
            updateQueueUseCase(queue)
            call.respond(HttpStatusCode.NoContent)
        }

        get {
            val bookId = call.getParam<UUID>("bookId") { UUID.fromString(it) }
            val userId = call.getParam<UUID>("userId") { UUID.fromString(it) }

            val result = readQueueUseCase(bookId, userId)
            call.respond(HttpStatusCode.OK, result)
        }

        route("/position/{bookId}/{userId}") {
            get {
                val bookId = call.getParam<UUID>("bookId", true) { UUID.fromString(it) }!!
                val userId = call.getParam<UUID>("userId", true) { UUID.fromString(it) }!!
                val result = getQueueUseCase(bookId, userId)
                call.respondText(result?.toString() ?: "null", status = HttpStatusCode.OK)
            }
        }

        route("/{id}") {
            delete {
                val queueId = call.getParam<UUID>("id", true) { UUID.fromString(it) }!!
                deleteQueueUseCase(queueId)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}