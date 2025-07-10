package ru.vafeen.data.network

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vafeen.data.network.repository.RetrofitAllCharactersRepository
import ru.vafeen.data.network.service.AllCharactersService
import ru.vafeen.domain.network.ResponseResult

class RetrofitAllCharactersRepositoryTest {

    private lateinit var repository: RetrofitAllCharactersRepository

    @Before
    fun setup() {
        val retrofit = Retrofit.Builder()
            .baseUrl(APIInfo.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(AllCharactersService::class.java)
        repository = RetrofitAllCharactersRepository(service)
    }

    @Test
    fun `getAllCharacters returns success with valid data structure`() = runBlocking {
        // Act
        val result = repository.getAllCharacters(page = 1)

        // Assert
        assertTrue(result is ResponseResult.Success)
        val (pagination, characters) = (result as ResponseResult.Success).data

        // Проверяем структуру пагинации
        assertTrue(pagination.totalCount > 0)
        assertTrue(pagination.totalPages > 0)
        assertNotNull(characters)
        assertTrue(characters.isNotEmpty())

        // Проверяем структуру первого персонажа
        val firstCharacter = characters.first()
        assertTrue(firstCharacter.id > 0)
        assertFalse(firstCharacter.name.isEmpty())
        assertNotNull(firstCharacter.lifeStatus)
        assertFalse(firstCharacter.species.isEmpty())
        assertNotNull(firstCharacter.gender)
        assertNotNull(firstCharacter.origin)
        assertNotNull(firstCharacter.currentLocation)
        assertFalse(firstCharacter.imageUrl.isEmpty())
        assertTrue(firstCharacter.episodeIds.isNotEmpty())
        assertFalse(firstCharacter.apiUrl.isEmpty())
        assertNotNull(firstCharacter.createdAt)
    }

    @Test
    fun `getAllCharacters handles pagination correctly`() = runBlocking {
        // First page
        val page1 = repository.getAllCharacters(page = 1) as ResponseResult.Success
        val (pagination1, characters1) = page1.data

        // Second page
        val page2 = repository.getAllCharacters(page = 2) as ResponseResult.Success
        val (pagination2, characters2) = page2.data

        // Проверяем что страницы разные
        assertNotEquals(characters1.first().id, characters2.first().id)

        // Проверяем связь пагинации
        assertEquals(pagination1.totalCount, pagination2.totalCount)
        assertEquals(pagination1.totalPages, pagination2.totalPages)
    }

    @Test
    fun `getAllCharacters returns empty list for invalid page`() = runBlocking {
        // Act (запрашиваем заведомо несуществующую страницу)
        val invalidPage = 9999
        val result = repository.getAllCharacters(page = invalidPage)

        // Assert
        assertTrue(result is ResponseResult.Success)
        val (pagination, characters) = (result as ResponseResult.Success).data
        assertTrue(characters.isEmpty())
        assertEquals(
            invalidPage - 1,
            pagination.prevPage
        ) // API возвращает prevPage даже для пустых страниц
    }

    @Test
    fun `getAllCharacters handles network errors`() = runBlocking {
        // Arrange - создаем репозиторий с нерабочим URL
        val brokenRetrofit = Retrofit.Builder()
            .baseUrl("https://invalid.url/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val brokenService = brokenRetrofit.create(AllCharactersService::class.java)
        val brokenRepository = RetrofitAllCharactersRepository(brokenService)

        // Act
        val result = brokenRepository.getAllCharacters(page = 1)

        // Assert
        assertTrue(result is ResponseResult.Error)
        assertFalse((result as ResponseResult.Error).stacktrace.isEmpty())
    }
}