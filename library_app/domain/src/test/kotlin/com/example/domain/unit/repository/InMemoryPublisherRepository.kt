package com.example.domain.unit.repository

import com.example.domain.model.PublisherModel
import com.example.domain.repository.PublisherRepository
import com.example.domain.specification.Specification
import java.util.UUID

class InMemoryPublisherRepository : PublisherRepository {
    private val storage = mutableMapOf<UUID, PublisherModel>()

    override suspend fun isContain(spec: Specification<PublisherModel>): Boolean {
        return storage.values.any { spec.specified(it) }
    }

    override suspend fun create(model: PublisherModel): UUID {
        storage[model.id] = model
        return model.id
    }

    override suspend fun deleteById(id: UUID): Int {
        if (storage.remove(id) == null)
            return 0
        return 1
    }

    override suspend fun readById(id: UUID): PublisherModel? {
        return storage[id]
    }

    override suspend fun query(
        spec: Specification<PublisherModel>,
        page: Int,
        pageSize: Int
    ): List<PublisherModel> {
        return storage.values.filter { spec.specified(it) }.subList(page * pageSize, (page + 1) * pageSize)
    }

    override suspend fun update(model: PublisherModel): Int {
        if (storage.contains(model.id)) {
            storage[model.id] = model
            return 1
        }
        return 0
    }
}
