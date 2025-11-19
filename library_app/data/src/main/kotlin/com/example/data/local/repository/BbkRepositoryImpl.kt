package com.example.data.local.repository

import com.example.data.local.entity.BbkEntity
import com.example.data.local.mapping.BbkMapper
import com.example.data.local.specification.BbkSpecToExpressionMapper
import com.example.domain.model.BbkModel
import com.example.domain.repository.BbkRepository
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

class BbkRepositoryImpl(
    private val db: Database
) : BbkRepository {
    override suspend fun readById(bbkId: UUID): BbkModel? = withContext(Dispatchers.IO) {
        transaction(db) {
            BbkEntity.selectAll().where { BbkEntity.id eq bbkId }.firstOrNull()?.let {
                BbkMapper.toDomain(it)
            }
        }
    }

    override suspend fun create(bbkModel: BbkModel): UUID = withContext(Dispatchers.IO) {
        transaction(db) {
            BbkEntity.insertAndGetId {
                BbkMapper.toInsertStatement(bbkModel, it)
            }.value
        }
    }

    override suspend fun update(bbkModel: BbkModel): Int = withContext(Dispatchers.IO) {
        transaction(db) {
            BbkEntity.update({ BbkEntity.id eq bbkModel.id }) {
                BbkMapper.toUpdateStatement(bbkModel, it)
            }
        }
    }

    override suspend fun deleteById(bbkId: UUID) = withContext(Dispatchers.IO) {
        transaction(db) {
            BbkEntity.deleteWhere { id eq bbkId }
        }
    }

    override suspend fun isContain(spec: Specification<BbkModel>) = withContext(Dispatchers.IO) {
        query(spec).isNotEmpty()
    }

    override suspend fun query(
        spec: Specification<BbkModel>,
        page: Int,
        pageSize: Int
    ): List<BbkModel> =
        withContext(Dispatchers.IO) {
            val expression = BbkSpecToExpressionMapper.map(spec)
            val offset: Long = (page * pageSize).toLong()
            transaction(db) {
                BbkEntity.selectAll().where { expression }.limit(pageSize, offset)
                    .map { BbkMapper.toDomain(it) }
            }
        }
}