package com.example.domain.unit.usecase.apu

import com.example.domain.exception.ModelDuplicateException
import com.example.domain.exception.ModelNotFoundException
import com.example.domain.model.ApuModel
import com.example.domain.model.TestApu
import com.example.domain.repository.ApuRepository
import com.example.domain.repository.BbkRepository
import com.example.domain.usecase.apu.CreateApuUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CreateApuUseCaseTest {
    private val apuRepository: ApuRepository = mockk()
    private val bbkRepository: BbkRepository = mockk()

    private val createUseCase = CreateApuUseCase(apuRepository, bbkRepository)

    private lateinit var testApu: ApuModel

    @Before
    fun setup() {
        testApu = TestApu.create(bbkId = UUID.randomUUID())
    }

    @Test
    fun `simple create apu test`() = runTest {
        coEvery { apuRepository.isContain(any()) } returns false
        coEvery { bbkRepository.isContain(any()) } returns true
        coEvery { apuRepository.create(testApu) } returns testApu.id

        val createdId = createUseCase(testApu)

        assertEquals(testApu.id, createdId)

        coVerify { apuRepository.isContain(any()) }
        coVerify { bbkRepository.isContain(any()) }
        coVerify { apuRepository.create(testApu) }
    }

    @Test
    fun `create duplicate apu`() = runTest {
        coEvery { apuRepository.isContain(any()) } returns true
        coEvery { bbkRepository.isContain(any()) } returns true

        assertFailsWith<ModelDuplicateException> { createUseCase(testApu) }
        coVerify { apuRepository.isContain(any()) }
        coVerify(exactly = 0) { apuRepository.create(any()) }
    }

    @Test
    fun `create apu with unknown bbkId`() = runTest {
        coEvery { apuRepository.isContain(any()) } returns false
        coEvery { bbkRepository.isContain(any()) } returns false

        assertFailsWith<ModelNotFoundException> { createUseCase(testApu) }

        coVerify { bbkRepository.isContain(any()) }
        coVerify(exactly = 0) { apuRepository.create(any()) }

    }
}