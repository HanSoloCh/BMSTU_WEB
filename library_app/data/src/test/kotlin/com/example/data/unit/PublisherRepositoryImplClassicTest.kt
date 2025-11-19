package com.example.data.unit

import com.example.data.local.entity.PublisherEntity
import com.example.data.local.repository.PublisherRepositoryImpl
import com.example.data.model.publisher.PublisherMother
import com.example.domain.specification.publisher.PublisherIdSpecification
import com.example.domain.specification.publisher.PublisherNameSpecification
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PublisherRepositoryImplClassicTest {

    private lateinit var db: Database
    private lateinit var repo: PublisherRepositoryImpl

    @Before
    fun setup() {
        // In-memory база H2
        db = Database.Companion.connect(
            "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;",
            driver = "org.h2.Driver"
        )

        transaction(db) {
            SchemaUtils.create(PublisherEntity)
            PublisherEntity.deleteAll()
        }

        repo = PublisherRepositoryImpl(db)
    }

    @After
    fun tearDown() {
        transaction(db) {
            SchemaUtils.drop(PublisherEntity)
        }
    }

    // --- create ---
    @Test
    fun `create should insert publisher`() = runBlocking {
        val publisher = PublisherMother.random()

        repo.create(publisher)

        val fromDb = repo.readById(publisher.id)
        assertNotNull(fromDb)
        assertEquals(publisher.name, fromDb.name)
    }

    // --- readById ---
    @Test
    fun `readById should return publisher when exists`() = runBlocking {
        val publisher = PublisherMother.random()
        repo.create(publisher)

        val result = repo.readById(publisher.id)

        assertNotNull(result)
        assertEquals(publisher.id, result.id)
    }

    @Test
    fun `readById should return null when not exists`() = runBlocking {
        val result = repo.readById(UUID.randomUUID())
        assertNull(result)
    }

    // --- update ---
    @Test
    fun `update should modify existing publisher`() = runBlocking {
        val publisher = PublisherMother.random()
        repo.create(publisher)

        val updated = PublisherMother.withId(publisher.id).copy(name = "Updated name")
        repo.update(updated)

        val fromDb = repo.readById(publisher.id)
        assertEquals("Updated name", fromDb?.name)
    }

    @Test
    fun `update should not throw if publisher does not exist`() = runBlocking {
        val nonExistent = PublisherMother.random()

        repo.update(nonExistent)
        val fromDb = repo.readById(nonExistent.id)

        assertNull(fromDb)
    }

    // --- deleteById ---
    @Test
    fun `deleteById should remove publisher`() = runBlocking {
        val publisher = PublisherMother.random()
        repo.create(publisher)

        repo.deleteById(publisher.id)

        val result = repo.readById(publisher.id)
        assertNull(result)
    }

    @Test
    fun `deleteById should do nothing if publisher not exists`() = runBlocking {
        val rc = repo.deleteById(UUID.randomUUID())
        assertEquals(rc, 0)
    }

    // --- isContain ---
    @Test
    fun `isContain should return true if publisher exists`() = runBlocking {
        val publisher = PublisherMother.random()
        repo.create(publisher)

        val spec = PublisherIdSpecification(publisher.id)
        val result = repo.isContain(spec)

        assertTrue(result)
    }

    @Test
    fun `isContain should return false if publisher not exists`() = runBlocking {
        val spec = PublisherIdSpecification(UUID.randomUUID())
        val result = repo.isContain(spec)

        TestCase.assertFalse(result)
    }

    // --- query ---
    @Test
    fun `query should return publishers matching spec`() = runBlocking {
        val p1 = PublisherMother.random().copy(name = "Alpha")
        val p2 = PublisherMother.random().copy(name = "Beta")
        repo.create(p1)
        repo.create(p2)

        val result = repo.query(PublisherNameSpecification("Alpha"))

        assertEquals(1, result.size)
        assertEquals("Alpha", result.first().name)
    }

    @Test
    fun `query should return empty list when no match`() = runBlocking {
        val result = repo.query(PublisherNameSpecification("NonExistent"))
        assertTrue(result.isEmpty())
    }
}