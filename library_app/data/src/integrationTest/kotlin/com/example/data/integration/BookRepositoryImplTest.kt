package com.example.data.integration

import com.example.data.local.entity.AuthorEntity
import com.example.data.local.entity.BbkEntity
import com.example.data.local.entity.PublisherEntity
import com.example.data.local.repository.BookRepositoryImpl
import com.example.domain.model.BookModel
import kotlinx.coroutines.test.runTest
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.UUID

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookRepositoryImplTest : BasePostgresIntegrationTest() {

    // Ленивый repository, чтобы db была уже инициализирована
    private val repository: BookRepositoryImpl by lazy { BookRepositoryImpl(db) }

    private lateinit var authorId: UUID
    private lateinit var publisherId: UUID
    private lateinit var bbkId: UUID

    @BeforeEach
    fun setupEntities() {
        transaction(db) {
            authorId = AuthorEntity.insertAndGetId { it[name] = "Test Author" }.value
            publisherId = PublisherEntity.insertAndGetId { it[name] = "Test Publisher" }.value
            bbkId = BbkEntity.insertAndGetId {
                it[code] = "Test code bbk"
                it[description] = "Test desc"
            }.value
        }
    }

    @Test
    fun `simple create book test`() = runTest {
        val book = BookModel(
            id = UUID.randomUUID(),
            title = "Test Book",
            authors = listOf(authorId),
            publisherId = publisherId,
            bbkId = bbkId
        )

        val createdId = repository.create(book)
        Assertions.assertEquals(book.id, createdId)

        val found = repository.readById(createdId)
        Assertions.assertNotNull(found)
        Assertions.assertEquals(book, found)
    }

    @Test
    fun `update book test`() = runTest {
        val originalBook = BookModel(
            id = UUID.randomUUID(),
            title = "Original Title",
            authors = listOf(authorId),
            publisherId = publisherId,
            bbkId = bbkId,
        )
        repository.create(originalBook)

        val newAuthorId = transaction(db) {
            AuthorEntity.insertAndGetId { it[name] = "Second Author" }.value
        }

        val updatedBook = originalBook.copy(
            title = "Updated Title",
            authors = listOf(newAuthorId)
        )

        repository.update(updatedBook)

        val result = repository.readById(originalBook.id)!!
        Assertions.assertEquals(updatedBook, result)
    }

    @Test
    fun `delete book test`() = runTest {
        val book = BookModel(
            id = UUID.randomUUID(),
            title = "Test Book",
            authors = listOf(authorId),
            publisherId = publisherId,
            bbkId = bbkId
        )
        repository.create(book)

        repository.deleteById(book.id)
        val result = repository.readById(book.id)
        Assertions.assertNull(result)
    }

    @Test
    fun `get books by author`() = runTest {
        val book = BookModel(
            id = UUID.randomUUID(),
            title = "Test Book",
            authors = listOf(authorId),
            publisherId = publisherId,
            bbkId = bbkId
        )
        repository.create(book)

        lateinit var anotherAuthorId: UUID
        transaction(db) {
            anotherAuthorId = AuthorEntity.insertAndGetId { it[name] = "Test Author 2" }.value
        }

        val anotherBook = BookModel(
            id = UUID.randomUUID(),
            title = "Test Book 2",
            authors = listOf(anotherAuthorId),
            publisherId = publisherId,
            bbkId = bbkId
        )
        repository.create(anotherBook)

        val result = repository.readByAuthorId(authorId)
        Assertions.assertEquals(listOf(book), result)
    }
}
