package com.example.domain.unit.usecase.publisher

import com.example.domain.model.publisher.PublisherMother
import com.example.domain.repository.PublisherRepository
import com.example.domain.usecase.publisher.ReadPublisherByIdUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ReadPublisherByIdUseCaseTest {

    private val publisherRepository: PublisherRepository = mockk()
    private val readUseCase = ReadPublisherByIdUseCase(publisherRepository)

    private val testPublisher = PublisherMother.random()

    @Test
    fun `read existing publisher by id`() = runTest {
        coEvery { publisherRepository.readById(testPublisher.id) } returns testPublisher

        val result = readUseCase(testPublisher.id)

        assertEquals(testPublisher, result)
        coVerify { publisherRepository.readById(testPublisher.id) }
    }

    @Test
    fun `read non-existing publisher by id`() = runTest {
        coEvery { publisherRepository.readById(testPublisher.id) } returns null

        val result = readUseCase(testPublisher.id)

        assertNull(result)
        coVerify { publisherRepository.readById(testPublisher.id) }
    }
}
