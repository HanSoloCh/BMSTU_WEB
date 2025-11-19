package com.example.data.local.repository

import com.example.data.local.entity.AuthorEntity
import com.example.data.local.entity.BookAuthorCrossRef
import com.example.data.local.entity.BookEntity
import com.example.data.local.mapping.AuthorMapper
import com.example.data.local.mapping.BookMapper
import com.example.data.local.specification.BookSpecToExpressionMapper
import com.example.domain.model.AuthorModel
import com.example.domain.model.BookModel
import com.example.domain.repository.BookRepository
import com.example.domain.specification.Specification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.UUID

class BookRepositoryImpl(
    private val db: Database
) : BookRepository {
    override suspend fun readBooks(page: Int, pageSize: Int): List<BookModel> =
        withContext(Dispatchers.IO) {
            transaction(db) {
                val offset: Long = ((page - 1) * pageSize).toLong()
                val books = BookEntity
                    .selectAll()
                    .limit(pageSize, offset)
                    .toList()
                val authors = getAuthorsByBookId(books.map { it[BookEntity.id].value })
                books.map { BookMapper.toDomain(it, authors[it[BookEntity.id].value].orEmpty()) }
            }
        }

    override suspend fun readById(bookId: UUID): BookModel? = withContext(Dispatchers.IO) {
        transaction(db) {
            BookEntity.selectAll().where { BookEntity.id eq bookId }.firstOrNull()?.let {
                BookMapper.toDomain(it, getAuthorsByBookId(bookId))
            }
        }
    }

    override suspend fun readByAuthorId(authorId: UUID): List<BookModel> =
        withContext(Dispatchers.IO) {
            transaction(db) {
                val books = (BookEntity innerJoin BookAuthorCrossRef)
                    .select(BookEntity.columns)
                    .where { BookAuthorCrossRef.authorId eq authorId }
                    .toList()
                val authors = getAuthorsByBookId(books.map { it[BookEntity.id].value })
                books.map { BookMapper.toDomain(it, authors[it[BookEntity.id].value].orEmpty()) }
            }
        }


    override suspend fun create(bookModel: BookModel) = withContext(Dispatchers.IO) {
        transaction(db) {
            // Create book
            val bookId = BookEntity.insertAndGetId {
                BookMapper.toInsertStatement(bookModel, it)
            }.value
            // Create connections
            bookModel.authors.forEach { author ->
                BookAuthorCrossRef.insert {
                    it[BookAuthorCrossRef.bookId] = bookId
                    it[authorId] = author
                }
            }
            bookId
        }
    }

    override suspend fun update(bookModel: BookModel) = withContext(Dispatchers.IO) {
        transaction(db) {
            val returnValue = BookEntity.update({ BookEntity.id eq bookModel.id }) {
                BookMapper.toUpdateStatement(bookModel, it)
            }
            if (returnValue > 0) {
                BookAuthorCrossRef.deleteWhere { bookId eq bookModel.id }

                bookModel.authors.forEach { author ->
                    BookAuthorCrossRef.insert {
                        it[bookId] = bookModel.id
                        it[authorId] = author
                    }
                }
            }
            returnValue
        }
    }

    override suspend fun deleteById(bookId: UUID) = withContext(Dispatchers.IO) {
        transaction(db) {
            BookEntity.deleteWhere { id eq bookId }
        }
    }

    override suspend fun isContain(spec: Specification<BookModel>) = withContext(Dispatchers.IO) {
        query(spec).isNotEmpty()
    }

    override suspend fun query(spec: Specification<BookModel>): List<BookModel> =
        withContext(Dispatchers.IO) {
            val expression = BookSpecToExpressionMapper.map(spec)

            transaction(db) {
                val books = BookEntity
                    .selectAll()
                    .where { expression }
                    .toList()
                val authors = getAuthorsByBookId(books.map { it[BookEntity.id].value })
                books.map { BookMapper.toDomain(it, authors[it[BookEntity.id].value].orEmpty()) }
            }
        }

    private fun getAuthorsByBookId(bookId: UUID): List<AuthorModel> = transaction {
        getAuthorsByBookId(listOf(bookId))[bookId].orEmpty()
    }

    private fun getAuthorsByBookId(bookIds: List<UUID>): Map<UUID, List<AuthorModel>> =
        transaction {
            (AuthorEntity innerJoin BookAuthorCrossRef)
                .selectAll()
                .where { BookAuthorCrossRef.bookId inList bookIds }
                .map { row -> row[BookAuthorCrossRef.bookId].value to AuthorMapper.toDomain(row) }
                .groupBy({ it.first }, { it.second })
        }
}