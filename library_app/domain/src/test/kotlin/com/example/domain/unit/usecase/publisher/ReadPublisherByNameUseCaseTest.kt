package com.example.domain.unit.usecase.publisher

import com.example.domain.model.publisher.PublisherBuilder
import com.example.domain.repository.PublisherRepository
import com.example.domain.usecase.publisher.ReadPublisherByNameUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ReadPublisherByNameUseCaseTest {

    private val publisherRepository: PublisherRepository = mockk()
    private val readUseCase = ReadPublisherByNameUseCase(publisherRepository)

    private val testPublisher = PublisherBuilder().withName("Penguin").build()

    @Test
    fun `read publisher by existing name`() = runTest {
        coEvery { publisherRepository.query(any()) } returns listOf(testPublisher)

        val result = readUseCase("Penguin")

        assertEquals(listOf(testPublisher), result)
        coVerify { publisherRepository.query(any()) }
    }

    @Test
    fun `read publisher by unknown name returns empty list`() = runTest {
        coEvery { publisherRepository.query(any()) } returns emptyList()

        val result = readUseCase("Unknown")

        assertTrue(result.isEmpty())
        coVerify { publisherRepository.query(any()) }
    }
}
