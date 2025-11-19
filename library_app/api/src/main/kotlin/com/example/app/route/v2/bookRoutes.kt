package com.example.app.route.v2

import com.example.app.config.hasRole
import com.example.app.util.getParam
import com.example.domain.enums.UserRole
import com.example.domain.model.BookModel
import com.example.domain.usecase.book.CreateBookUseCase
import com.example.domain.usecase.book.DeleteBookUseCase
import com.example.domain.usecase.book.ReadBookByAuthorUseCase
import com.example.domain.usecase.book.ReadBookByBbkUseCase
import com.example.domain.usecase.book.ReadBookByIdUseCase
import com.example.domain.usecase.book.ReadBookByPublisherUseCase
import com.example.domain.usecase.book.ReadBookBySentenceUseCase
import com.example.domain.usecase.book.ReadBooksUseCase
import com.example.domain.usecase.book.UpdateBookUseCase
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
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import kotlinx.coroutines.flow.emptyFlow
import org.koin.ktor.ext.inject
import java.util.UUID

fun Route.bookRoutes() {
    val readBookByIdUseCase by inject<ReadBookByIdUseCase>()
    val createBookUseCase by inject<CreateBookUseCase>()
    val updateBookUseCase by inject<UpdateBookUseCase>()
    val deleteBookUseCase by inject<DeleteBookUseCase>()
    val readAllBooksUseCase by inject<ReadBooksUseCase>()

    route("/book") {
        authenticate("auth-jwt") {
            post {
                val principal = call.principal<JWTPrincipal>() ?: run {
                    call.respond(HttpStatusCode.Unauthorized, "Authentication required")
                    return@post
                }

                if (!principal.hasRole(UserRole.MODERATOR)) {
                    call.respond(HttpStatusCode.Forbidden, "Only moderators can create books")
                    return@post
                }

                val book = call.receive<BookModel>()
                val createdId = createBookUseCase(book)
                call.respond(HttpStatusCode.Created, createdId)
            }

            put {
                val principal = call.principal<JWTPrincipal>() ?: run {
                    call.respond(HttpStatusCode.Unauthorized, "Authentication required")
                    return@put
                }

                if (!principal.hasRole(UserRole.MODERATOR)) {
                    call.respond(HttpStatusCode.Forbidden, "Only moderators can update books")
                    return@put
                }

                val book = call.receive<BookModel>()
                updateBookUseCase(book)
                call.respond(HttpStatusCode.NoContent)
            }

            get {
                val page = call.getParam<Int>("page") { it.toInt() } ?: 1
                val pageSize = call.getParam<Int>("pageSize") { it.toInt() } ?: 10
                val books = readAllBooksUseCase(page, pageSize)
                call.respond(HttpStatusCode.OK, books)
            }

            route("/{id}") {
                get {
                    val bookId = call.getParam<UUID>("id", true) { UUID.fromString(it) }!!
                    val book = readBookByIdUseCase(bookId)
                    if (book == null) {
                        call.respond(HttpStatusCode.NotFound, "Book not found")
                    } else {
                        call.respond(book)
                    }
                }

                delete {
                    val principal = call.principal<JWTPrincipal>() ?: run {
                        call.respond(HttpStatusCode.Unauthorized, "Authentication required")
                        return@delete
                    }

                    if (!principal.hasRole(UserRole.MODERATOR)) {
                        call.respond(HttpStatusCode.Forbidden, "Only moderators can delete books")
                        return@delete
                    }

                    val bookId = call.getParam<UUID>("id", true) { UUID.fromString(it) }!!
                    deleteBookUseCase(bookId)
                    call.respond(HttpStatusCode.NoContent)
                }
            }

            route("/search") {
                val readBookByAuthorUseCase by inject<ReadBookByAuthorUseCase>()
                val readBookByBbkUseCase by inject<ReadBookByBbkUseCase>()
                val readBookByPublisherUseCase by inject<ReadBookByPublisherUseCase>()
                val readBookBySentenceUseCase by inject<ReadBookBySentenceUseCase>()

                get {
                    val authorId = call.getParam<UUID>("authorId") { UUID.fromString(it) }
                    val bbkId = call.getParam<UUID>("bbkId") { UUID.fromString(it) }
                    val publisherId = call.getParam<UUID>("publisherId") { UUID.fromString(it) }
                    val query = call.request.queryParameters["q"]

                    val activeFilters = listOfNotNull(authorId, bbkId, publisherId, query)

                    if (activeFilters.isEmpty()) {
                        call.respond(HttpStatusCode.BadRequest, "Filter query is required")
                        return@get
                    }
                    if (activeFilters.size > 1) {
                        call.respond(HttpStatusCode.BadRequest, "Filter query is more than one filter")
                        return@get
                    }

                    val result = when {
                        authorId != null -> readBookByAuthorUseCase(authorId)
                        bbkId != null -> readBookByBbkUseCase(bbkId)
                        publisherId != null -> readBookByPublisherUseCase(publisherId)
                        query != null -> readBookBySentenceUseCase(query)
                        else -> emptyFlow<BookModel>()
                    }
                    call.respond(result)
                }
            }
        }
    }
}
