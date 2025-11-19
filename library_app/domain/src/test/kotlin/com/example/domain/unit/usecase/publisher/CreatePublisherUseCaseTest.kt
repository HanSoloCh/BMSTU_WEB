package com.example.domain.unit.usecase.publisher

import com.example.domain.exception.ModelDuplicateException
import com.example.domain.model.PublisherModel
import com.example.domain.model.publisher.PublisherMother
import com.example.domain.repository.PublisherRepository
import com.example.domain.unit.repository.InMemoryPublisherRepository
import com.example.domain.usecase.publisher.CreatePublisherUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class CreatePublisherUseCaseTest {

    // mockk
    private val publisherRepository: PublisherRepository = mockk()
    private val createUseCase = CreatePublisherUseCase(publisherRepository)

    // in-memory
    private lateinit var memoryRepository: InMemoryPublisherRepository
    private lateinit var classicCreateUseCase: CreatePublisherUseCase

    private lateinit var testPublisher: PublisherModel

    @Before
    fun setup() {
        testPublisher = PublisherMother.random()
        memoryRepository = InMemoryPublisherRepository()
        classicCreateUseCase = CreatePublisherUseCase(memoryRepository)
    }

    // Позитивный тест (mockk)
    @Test
    fun `create new publisher successfully`() = runTest {
        coEvery { publisherRepository.isContain(any()) } returns false
        coEvery { publisherRepository.create(testPublisher) } returns testPublisher.id

        val result = createUseCase(testPublisher)

        assertEquals(testPublisher.id, result)
        coVerify { publisherRepository.create(testPublisher) }
    }

    // Негативный тест — дубликат (mockk)
    @Test
    fun `create duplicate publisher throws exception`() = runTest {
        coEvery { publisherRepository.isContain(any()) } returns true

        assertFailsWith<ModelDuplicateException> { createUseCase(testPublisher) }
        coVerify(exactly = 0) { publisherRepository.create(any()) }
    }

    // Позитивный тест (классический стиль)
    @Test
    fun `classic repo - create publisher`() = runTest {
        val createdId = classicCreateUseCase(testPublisher)
        assertEquals(testPublisher.id, createdId)
        assertNotNull(memoryRepository.readById(createdId))
    }

    // Негативный тест (классический стиль)
    @Test
    fun `classic repo - create duplicate throws`() = runTest {
        memoryRepository.create(testPublisher)
        assertFailsWith<ModelDuplicateException> { classicCreateUseCase(testPublisher) }
    }
}
