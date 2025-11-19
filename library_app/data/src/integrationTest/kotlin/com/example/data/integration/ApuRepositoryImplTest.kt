package com.example.data.integration

import com.example.data.local.entity.BbkEntity
import com.example.data.local.repository.ApuRepositoryImpl
import com.example.data.local.repository.BbkRepositoryImpl
import com.example.domain.model.ApuModel
import com.example.domain.specification.apu.ApuTermSpecification
import kotlinx.coroutines.test.runTest
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class ApuRepositoryImplTest : BasePostgresIntegrationTest() {

    // Ленивый repository, чтобы db была инициализирована
    private val repository: ApuRepositoryImpl by lazy { ApuRepositoryImpl(db) }

    private lateinit var bbkId: UUID

    @BeforeEach
    fun setup() {
        transaction(db) {
            bbkId = BbkEntity.insertAndGetId {
                it[code] = "Test Code"
                it[description] = "Test Description"
            }.value
        }
    }

    @Test
    fun `simple create apu test`() = runTest {
        val apu = ApuModel(UUID.randomUUID(), "Test", bbkId)
        val id = repository.create(apu)
        Assertions.assertEquals(apu.id, id)

        val found = repository.readById(id)
        Assertions.assertNotNull(found)
        Assertions.assertEquals(apu, found)
    }

    @Test
    fun `read with specification test`() = runTest {
        val apu = ApuModel(UUID.randomUUID(), "Test", bbkId)
        repository.create(apu)
        repository.create(
            ApuModel(UUID.randomUUID(), "Other APU", bbkId)
        )

        val result = repository.query(ApuTermSpecification("Test"))
        Assertions.assertEquals(apu, result.firstOrNull())
    }

    @Test
    fun `delete apu test`() = runTest {
        val newApu = UUID.randomUUID()
        val apu = ApuModel(newApu, "Test", bbkId)
        repository.create(apu)

        // Удаляем BBK, чтобы проверить каскадное удаление или связь
        BbkRepositoryImpl(db).deleteById(bbkId)

        val found = repository.readById(newApu)
        Assertions.assertNull(found)
    }
}
