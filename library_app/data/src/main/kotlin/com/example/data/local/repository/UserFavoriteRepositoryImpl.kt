package com.example.data.local.repository

import com.example.data.local.entity.BookEntity
import com.example.data.local.entity.UserFavoriteCrossRef
import com.example.data.local.mapping.BookMapper
import com.example.domain.model.BookModel
import com.example.domain.repository.UserFavoriteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID


class UserFavoriteRepositoryImpl(
    private val db: Database
) : UserFavoriteRepository {
    override suspend fun create(userId: UUID, bookId: UUID) = withContext(Dispatchers.IO) {
        transaction(db) {
            UserFavoriteCrossRef.insert {
                it[UserFavoriteCrossRef.userId] = userId
                it[UserFavoriteCrossRef.bookId] = bookId
            }
        }
        userId to bookId
    }

    override suspend fun delete(userId: UUID, bookId: UUID) = withContext(Dispatchers.IO) {
        transaction(db) {
            UserFavoriteCrossRef
                .deleteWhere {
                    (UserFavoriteCrossRef.userId eq userId) and (UserFavoriteCrossRef.bookId eq bookId)
                }
        }
    }

    override suspend fun readByUserId(userId: UUID, page: Int, pageSize: Int): List<BookModel> =
        withContext(Dispatchers.IO) {
            val offset: Long = (page * pageSize).toLong()
            transaction(db) {
                (BookEntity innerJoin UserFavoriteCrossRef)
                    .select(BookEntity.columns)
                    .where { UserFavoriteCrossRef.userId eq userId }
                    .limit(pageSize, offset)
                    .map { BookMapper.toDomain(it) }
            }
        }
}