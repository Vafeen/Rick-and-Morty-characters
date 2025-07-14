package ru.vafeen.data.network.converter

import org.junit.Test
import ru.vafeen.data.network.dto.CharacterDataDTO
import ru.vafeen.data.network.dto.CharacterResponse
import ru.vafeen.data.network.dto.GenderDTO
import ru.vafeen.data.network.dto.LifeStatusDTO
import ru.vafeen.data.network.dto.LocationDTO
import ru.vafeen.data.network.dto.SingleCharacterDTO
import ru.vafeen.domain.model.Gender
import ru.vafeen.domain.model.LifeStatus
import ru.vafeen.domain.model.Location
import java.time.ZonedDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Test suite for CharacterResponse converter functions.
 *
 * This class contains unit tests for verifying the correct conversion
 * from network DTOs to domain models.
 */
class CharacterConverterTest {

    /**
     * Tests the conversion of CharacterResponse to domain model with pagination info.
     * Verifies that both pagination data and character list are correctly converted.
     */
    @Test
    fun `character response toDomain converts correctly`() {
        // Given
        val response = CharacterResponse(
            info = CharacterResponse.PageInfo(
                totalCount = 42,
                totalPages = 2,
                nextPageUrl = "https://example.com/api?page=2",
                prevPageUrl = "https://example.com/api?page=1"
            ),
            results = listOf(
                createTestCharacterDTO(1),
                createTestCharacterDTO(2)
            )
        )

        // When
        val (pagination, characters) = response.toDomain()

        // Then
        assertEquals(42, pagination.totalCount)
        assertEquals(2, pagination.totalPages)
        assertEquals(2, pagination.nextPage)
        assertEquals(1, pagination.prevPage)
        assertEquals(2, characters.size)
    }

    /**
     * Tests the conversion of CharacterResponse with null results.
     * Verifies that empty list is returned when results are null.
     */
    @Test
    fun `character response with null results converts to empty list`() {
        // Given
        val response = CharacterResponse(
            info = CharacterResponse.PageInfo(
                totalCount = 0,
                totalPages = 0,
                nextPageUrl = null,
                prevPageUrl = null
            ),
            results = null
        )

        // When
        val (pagination, characters) = response.toDomain()

        // Then
        assertEquals(0, pagination.totalCount)
        assertTrue(characters.isEmpty())
    }

    /**
     * Tests the conversion of CharacterDataDTO to domain model.
     * Verifies all fields are correctly mapped including nested objects.
     */
    @Test
    fun `character data dto toDomain converts correctly`() {
        // Given
        val dto = createTestCharacterDTO(1)

        // When
        val character = dto.toDomain(2)

        // Then
        assertEquals(1, character.id)
        assertEquals("Test Character", character.name)
        assertEquals(LifeStatus.ALIVE, character.lifeStatus)
        assertEquals("Human", character.species)
        assertEquals("Test type", character.type)
        assertEquals(Gender.MALE, character.gender)
        assertEquals(Location("Earth", 1), character.origin)
        assertEquals(Location("Mars", 2), character.currentLocation)
        assertEquals("https://example.com/image.jpg", character.imageUrl)
        assertEquals(listOf(1, 2, 3), character.episodeIds)
        assertEquals("https://example.com/api/character/1", character.apiUrl)
        assertEquals(2, character.nextKey)
    }

    /**
     * Tests the conversion of SingleCharacterDTO to domain model.
     * Verifies all fields are correctly mapped with null nextKey.
     */
    @Test
    fun `single character dto toDomain converts correctly`() {
        // Given
        val dto = SingleCharacterDTO(
            id = 1,
            name = "Test Character",
            status = LifeStatusDTO.ALIVE,
            species = "Human",
            type = "Test type",
            gender = GenderDTO.MALE,
            origin = LocationDTO("Earth", "https://example.com/api/location/1"),
            location = LocationDTO("Mars", "https://example.com/api/location/2"),
            image = "https://example.com/image.jpg",
            episode = listOf(
                "https://example.com/api/episode/1",
                "https://example.com/api/episode/2",
                "https://example.com/api/episode/3"
            ),
            url = "https://example.com/api/character/1",
            created = "2023-01-01T00:00:00Z"
        )

        // When
        val character = dto.toDomain()

        // Then
        assertEquals(1, character.id)
        assertEquals("Test Character", character.name)
        assertEquals(LifeStatus.ALIVE, character.lifeStatus)
        assertEquals("Human", character.species)
        assertEquals("Test type", character.type)
        assertEquals(Gender.MALE, character.gender)
        assertEquals(Location("Earth", 1), character.origin)
        assertEquals(Location("Mars", 2), character.currentLocation)
        assertEquals("https://example.com/image.jpg", character.imageUrl)
        assertEquals(listOf(1, 2, 3), character.episodeIds)
        assertEquals("https://example.com/api/character/1", character.apiUrl)
        assertNull(character.nextKey)
    }

    /**
     * Tests the conversion of LifeStatusDTO enum values.
     * Verifies all enum cases are correctly mapped to domain model.
     */
    @Test
    fun `life status dto toDomain converts all enum values`() {
        assertEquals(LifeStatus.ALIVE, LifeStatusDTO.ALIVE.toDomain())
        assertEquals(LifeStatus.DEAD, LifeStatusDTO.DEAD.toDomain())
        assertEquals(LifeStatus.UNKNOWN, LifeStatusDTO.UNKNOWN.toDomain())
    }

    /**
     * Tests the conversion of GenderDTO enum values.
     * Verifies all enum cases are correctly mapped to domain model.
     */
    @Test
    fun `gender dto toDomain converts all enum values`() {
        assertEquals(Gender.MALE, GenderDTO.MALE.toDomain())
        assertEquals(Gender.FEMALE, GenderDTO.FEMALE.toDomain())
        assertEquals(Gender.GENDERLESS, GenderDTO.GENDERLESS.toDomain())
        assertEquals(Gender.UNKNOWN, GenderDTO.UNKNOWN.toDomain())
    }

    /**
     * Tests URL extraction for ID from various URL formats.
     * Verifies correct ID extraction and null handling for invalid URLs.
     */
    @Test
    fun `extractId handles various url formats`() {
        assertEquals(1, "https://example.com/api/1".extractId())
        assertNull("https://example.com/api/".extractId())
        assertNull("not-a-url".extractId())
        assertNull("".extractId())
    }

    /**
     * Tests page number extraction from pagination URLs.
     * Verifies correct page number extraction and null handling.
     */
    @Test
    fun `extractPageNumber handles various url formats`() {
        assertEquals(2, "https://example.com/api?page=2".extractPageNumber())
        assertEquals(3, "https://example.com/api?page=3&other=param".extractPageNumber())
        assertEquals(1, "page=1".extractPageNumber())
        assertNull("https://example.com/api".extractPageNumber())
        assertNull("page=".extractPageNumber())
        assertNull("".extractPageNumber())
    }

    private fun createTestCharacterDTO(id: Int): CharacterDataDTO {
        return CharacterDataDTO(
            id = id,
            name = "Test Character",
            status = LifeStatusDTO.ALIVE,
            species = "Human",
            type = "Test type",
            gender = GenderDTO.MALE,
            origin = LocationDTO("Earth", "https://example.com/api/location/1"),
            location = LocationDTO("Mars", "https://example.com/api/location/2"),
            image = "https://example.com/image.jpg",
            episode = listOf(
                "https://example.com/api/episode/1",
                "https://example.com/api/episode/2",
                "https://example.com/api/episode/3"
            ),
            url = "https://example.com/api/character/$id",
            created = ZonedDateTime.now().toString()
        )
    }
}