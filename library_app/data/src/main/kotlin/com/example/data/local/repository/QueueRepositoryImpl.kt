package com.example.data.local.repository

import com.example.data.local.entity.QueueEntity
import com.example.data.local.mapping.QueueMapper
import com.example.data.local.specification.QueueSpecToExpressionMapper
import com.example.domain.model.QueueModel
import com.example.domain.repository.QueueRepository
import com.example.domain.specification.Specification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.UUID

class QueueRepositoryImpl(
    private val db: Database
) : QueueRepository {
    override suspend fun create(queueModel: QueueModel) = withContext(Dispatchers.IO) {
        transaction(db) {
            QueueEntity.insertAndGetId {
                QueueMapper.toInsertStatement(queueModel, it)
            }.value
        }
    }

    override suspend fun update(queueModel: QueueModel) = withContext(Dispatchers.IO) {
        transaction(db) {
            QueueEntity.update({ QueueEntity.id eq queueModel.id }) {
                QueueMapper.toUpdateStatement(queueModel, it)
            }
        }
    }

    override suspend fun deleteById(queueId: UUID) = withContext(Dispatchers.IO) {
        transaction(db) {
            QueueEntity.deleteWhere { id eq queueId }
        }
    }

    override suspend fun isContain(spec: Specification<QueueModel>) =
        withContext(Dispatchers.IO) {
            query(spec).isNotEmpty()
        }

    override suspend fun getQueuePosition(bookId: UUID, userId: UUID): Int? {
        return withContext(Dispatchers.IO) {
            transaction(db) {
                exec("SELECT get_queue_number('$bookId', '$userId')") { rs ->
                    if (rs.next()) rs.getInt(1) else null
                }
            }
        }
    }


    override suspend fun query(
        spec: Specification<QueueModel>,
        page: Int,
        pageSize: Int
    ): List<QueueModel> =
        withContext(Dispatchers.IO) {
            val expression = QueueSpecToExpressionMapper.map(spec)
            val offset: Long = (page * pageSize).toLong()
            transaction(db) {
                QueueEntity.selectAll().where { expression }.limit(pageSize, offset)
                    .map { QueueMapper.toDomain(it) }
            }
        }
}