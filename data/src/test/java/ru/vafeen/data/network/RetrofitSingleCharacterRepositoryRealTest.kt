package ru.vafeen.data.network

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vafeen.data.network.repository.RetrofitSingleCharacterRepository
import ru.vafeen.data.network.service.SingleCharacterService
import ru.vafeen.domain.network.ResponseResult

class RetrofitSingleCharacterRepositoryRealTest {

    private lateinit var repository: RetrofitSingleCharacterRepository

    @Before
    fun setup() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(SingleCharacterService::class.java)
        repository = RetrofitSingleCharacterRepository(service)
    }

    @Test
    fun `getCharacter returns success with valid character data`() = runBlocking {
        // Arrange - известный существующий ID персонажа
        val validCharacterId = 1 // Rick Sanchez

        // Act
        val result = repository.getCharacter(validCharacterId)

        // Assert
        assertTrue(result is ResponseResult.Success)
        val character = (result as ResponseResult.Success).data

        // Проверяем структуру персонажа
        assertEquals(validCharacterId, character.id)
        assertFalse(character.name.isEmpty())
        assertNotNull(character.lifeStatus)
        assertFalse(character.species.isEmpty())
        assertNotNull(character.gender)
        assertNotNull(character.origin)
        assertNotNull(character.currentLocation)
        assertFalse(character.imageUrl.isEmpty())
        assertTrue(character.episodeIds.isNotEmpty())
        assertFalse(character.apiUrl.isEmpty())
        assertNotNull(character.createdAt)
    }

    @Test
    fun `getCharacter returns error for non-existent character`() = runBlocking {
        // Arrange - заведомо несуществующий ID
        val invalidCharacterId = 999999

        // Act
        val result = repository.getCharacter(invalidCharacterId)

        // Assert
        assertTrue(result is ResponseResult.Error)
        assertFalse((result as ResponseResult.Error).stacktrace.isEmpty())
    }

    @Test
    fun `getCharacter handles network errors`() = runBlocking {
        // Arrange - создаем репозиторий с нерабочим URL
        val brokenRetrofit = Retrofit.Builder()
            .baseUrl("https://invalid.url/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val brokenService = brokenRetrofit.create(SingleCharacterService::class.java)
        val brokenRepository = RetrofitSingleCharacterRepository(brokenService)

        // Act
        val result = brokenRepository.getCharacter(1)

        // Assert
        assertTrue(result is ResponseResult.Error)
        assertFalse((result as ResponseResult.Error).stacktrace.isEmpty())
    }

    @Test
    fun `getCharacter handles malformed response`() = runBlocking {
        // Arrange - создаем репозиторий с URL, возвращающим некорректные данные
        val malformedRetrofit = Retrofit.Builder()
            .baseUrl("https://google.com/") // URL, который вернет неожиданный ответ
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val malformedService = malformedRetrofit.create(SingleCharacterService::class.java)
        val malformedRepository = RetrofitSingleCharacterRepository(malformedService)

        // Act
        val result = malformedRepository.getCharacter(1)

        // Assert
        assertTrue(result is ResponseResult.Error)
        assertFalse((result as ResponseResult.Error).stacktrace.isEmpty())
    }
}