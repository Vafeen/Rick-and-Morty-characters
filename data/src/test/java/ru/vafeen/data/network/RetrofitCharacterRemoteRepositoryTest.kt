package ru.vafeen.data.network

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vafeen.data.network.repository.RetrofitCharacterRemoteRepository
import ru.vafeen.data.network.service.CharacterService
import ru.vafeen.domain.model.Gender
import ru.vafeen.domain.model.LifeStatus
import ru.vafeen.domain.network.ResponseResult

/**
 * Comprehensive test suite for [RetrofitCharacterRemoteRepository].
 * Verifies correct interaction with Rick and Morty API including:
 * - Single character fetching
 * - Character filtering with various parameters
 * - Pagination behavior
 * - Edge case handling
 * - Error scenarios
 */
class RetrofitCharacterRemoteRepositoryTest {

    private lateinit var repository: RetrofitCharacterRemoteRepository
    private val validCharacterId = 1 // Rick Sanchez
    private val invalidCharacterId = 999999

    /**
     * Initializes test environment before each test case.
     * Creates Retrofit instance with:
     * - Base API URL: "https://rickandmortyapi.com/api/"
     * - Gson converter for JSON parsing
     * - Creates repository instance with CharacterService
     */
    @Before
    fun setup() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(CharacterService::class.java)
        repository = RetrofitCharacterRemoteRepository(service)
    }

    /* Single Character Tests */

    /**
     * Tests successful character retrieval by valid ID.
     * Verifies:
     * - Response is of type [ResponseResult.Success]
     * - All character fields are properly populated
     * - Basic field validations (non-empty strings, valid objects)
     */
    @Test
    fun `getCharacter returns success with valid character data`() = runTest {
        val result = repository.getCharacter(validCharacterId)

        assertTrue(result is ResponseResult.Success)
        val character = (result as ResponseResult.Success).data

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

    /**
     * Tests character retrieval with non-existent ID.
     * Verifies:
     * - Response is of type [ResponseResult.Error]
     * - Error contains non-empty stacktrace
     */
    @Test
    fun `getCharacter returns error for non-existent character`() = runTest {
        val result = repository.getCharacter(invalidCharacterId)

        assertTrue(result is ResponseResult.Error)
        assertFalse((result as ResponseResult.Error).stacktrace.isEmpty())
    }

    /**
     * Tests character retrieval with zero ID.
     * Verifies API returns 404 error for invalid ID.
     */
    @Test
    fun `getCharacter returns 404 for zero ID`() = runTest {
        val result = repository.getCharacter(0)

        assertTrue(result is ResponseResult.Error)
        assertTrue((result as ResponseResult.Error).stacktrace.contains("404"))
    }

    /**
     * Tests character retrieval with negative ID.
     * Verifies API returns 404 error for invalid ID.
     */
    @Test
    fun `getCharacter returns 404 for negative ID`() = runTest {
        val result = repository.getCharacter(-1)

        assertTrue(result is ResponseResult.Error)
        assertTrue((result as ResponseResult.Error).stacktrace.contains("404"))
    }

    /* Filtered Characters Tests */

    /**
     * Tests character filtering by name and status.
     * Verifies:
     * - Response contains matching characters
     * - All returned characters contain "Rick" in name (case insensitive)
     * - All characters have status ALIVE
     */
    @Test
    fun `filter by name and status returns matching characters`() = runTest {
        val result = repository.getCharacters(
            page = 1,
            name = "rick",
            status = "alive"
        )

        assertTrue(result is ResponseResult.Success)
        val (_, characters) = (result as ResponseResult.Success).data

        assertTrue(characters.isNotEmpty())
        assertTrue(characters.all { it.name.contains("Rick", ignoreCase = true) })
        assertTrue(characters.all { it.lifeStatus == LifeStatus.ALIVE })
    }

    /**
     * Tests character filtering by species.
     * Verifies:
     * - Response contains only characters of specified species ("Alien")
     * - Case insensitive comparison
     */
    @Test
    fun `filter by species returns correct results`() = runTest {
        val result = repository.getCharacters(
            page = 1,
            species = "Alien"
        )

        assertTrue(result is ResponseResult.Success)
        val characters = (result as ResponseResult.Success).data.second

        assertTrue(characters.isNotEmpty())
        assertTrue(characters.all { it.species.equals("Alien", ignoreCase = true) })
    }

    /**
     * Tests character filtering by gender.
     * Verifies:
     * - Response contains only female characters
     * - Gender enum values match exactly
     */
    @Test
    fun `filter by gender returns correct results`() = runTest {
        val result = repository.getCharacters(
            page = 1,
            gender = "female"
        )

        assertTrue(result is ResponseResult.Success)
        val characters = (result as ResponseResult.Success).data.second

        assertTrue(characters.isNotEmpty())
        assertTrue(characters.all { it.gender == Gender.FEMALE })
    }

    /**
     * Tests character retrieval without filters.
     * Verifies:
     * - Response contains multiple characters
     * - Returned characters have diverse names
     */
    @Test
    fun `filter with only page parameter returns all characters`() = runTest {
        val result = repository.getCharacters(page = 1)

        assertTrue(result is ResponseResult.Success)
        val characters = (result as ResponseResult.Success).data.second

        assertTrue(characters.isNotEmpty())
        val uniqueNames = characters.map { it.name }.toSet()
        assertTrue(uniqueNames.size > 1)
    }

    /**
     * Tests filtering with non-existent character combination.
     * Verifies API returns empty list for impossible filters.
     */
    @Test
    fun `invalid filter combination returns empty list`() = runTest {
        val result = repository.getCharacters(
            page = 1,
            name = "nonexistent123",
            status = "alive"
        )

        assertTrue(result is ResponseResult.Success)
        assertTrue((result as ResponseResult.Success).data.second.isEmpty())
    }

    /* Pagination Tests */

    /**
     * Tests pagination with filters.
     * Verifies:
     * - Different pages return different characters
     * - Filters are applied consistently across pages
     */
    @Test
    fun `pagination works correctly with filters`() = runTest {
        val page1 = repository.getCharacters(
            page = 1,
            status = "alive"
        ) as ResponseResult.Success

        val page2 = repository.getCharacters(
            page = 2,
            status = "alive"
        ) as ResponseResult.Success

        assertNotEquals(
            page1.data.second.first().id,
            page2.data.second.first().id
        )
        assertTrue(page1.data.second.all { it.lifeStatus == LifeStatus.ALIVE })
        assertTrue(page2.data.second.all { it.lifeStatus == LifeStatus.ALIVE })
    }

    /**
     * Tests pagination metadata parsing.
     * Verifies:
     * - Total count and pages are positive numbers
     * - First page has next page reference
     * - First page has no previous page reference
     */
    @Test
    fun `pagination metadata is correctly parsed`() = runTest {
        val result = repository.getCharacters(page = 1)

        assertTrue(result is ResponseResult.Success)
        val (pagination, _) = (result as ResponseResult.Success).data

        assertTrue(pagination.totalCount > 0)
        assertTrue(pagination.totalPages > 0)
        assertNotNull(pagination.nextPage)
        assertNull(pagination.prevPage)
    }

    /* Edge Cases */

    /**
     * Tests empty string filter handling.
     * Verifies empty name filter is treated as no filter.
     */
    @Test
    fun `empty string filter works as no filter`() = runTest {
        val filtered = repository.getCharacters(page = 1, name = "") as ResponseResult.Success
        val unfiltered = repository.getCharacters(page = 1) as ResponseResult.Success

        assertEquals(unfiltered.data.second.size, filtered.data.second.size)
    }

    /**
     * Tests whitespace handling in name filter.
     * Verifies trailing whitespace doesn't affect results.
     */
    @Test
    fun `whitespace in name filter is trimmed`() = runTest {
        val result1 = repository.getCharacters(page = 1, name = "rick ") as ResponseResult.Success
        val result2 = repository.getCharacters(page = 1, name = "rick") as ResponseResult.Success

        assertEquals(result1.data.second.size, result2.data.second.size)
    }

    /**
     * Tests out-of-bounds page number handling.
     * Verifies API returns empty list for non-existent pages.
     */
    @Test
    fun `large page number returns empty list`() = runTest {
        val result = repository.getCharacters(page = 9999)

        assertTrue(result is ResponseResult.Success)
        assertTrue((result as ResponseResult.Success).data.second.isEmpty())
    }
}