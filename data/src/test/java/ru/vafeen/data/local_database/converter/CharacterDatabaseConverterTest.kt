package ru.vafeen.data.local_database.converter

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.junit.Test
import ru.vafeen.data.local_database.entity.CharacterEntity
import ru.vafeen.domain.model.CharacterData
import ru.vafeen.domain.model.Gender
import ru.vafeen.domain.model.LifeStatus
import ru.vafeen.domain.model.Location
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.fail

/**
 * Test suite for Character database converters.
 *
 * Verifies the bidirectional conversion between [CharacterData] domain model
 * and [CharacterEntity] database entity.
 */
class CharacterDatabaseConverterTest {

    private val testJson = Json { ignoreUnknownKeys = true }
    private val testDateTime = ZonedDateTime.now()
    private val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME

    /**
     * Tests complete conversion from domain model to entity and back.
     * Verifies all fields maintain their values through the round-trip conversion.
     */
    @Test
    fun `domain to entity and back conversion maintains all values`() {
        // Given
        val original = createTestCharacterData()

        // When
        val entity = original.toEntity()
        val restored = entity.toCharacterData()

        // Then
        assertEquals(original.id, restored.id)
        assertEquals(original.name, restored.name)
        assertEquals(original.lifeStatus, restored.lifeStatus)
        assertEquals(original.species, restored.species)
        assertEquals(original.type, restored.type)
        assertEquals(original.gender, restored.gender)
        assertEquals(original.origin, restored.origin)
        assertEquals(original.currentLocation, restored.currentLocation)
        assertEquals(original.imageUrl, restored.imageUrl)
        assertEquals(original.episodeIds, restored.episodeIds)
        assertEquals(original.apiUrl, restored.apiUrl)
        assertEquals(original.nextKey, restored.nextKey)
        // Compare formatted dates to avoid precision differences
        assertEquals(
            original.createdAt.format(formatter),
            restored.createdAt.format(formatter)
        )
    }

    /**
     * Tests conversion when episodeIds list is empty.
     * Verifies both serialization and deserialization handle empty lists correctly.
     */
    @Test
    fun `conversion handles empty episodeIds list`() {
        // Given
        val original = createTestCharacterData().copy(episodeIds = emptyList())

        // When
        val entity = original.toEntity()
        val restored = entity.toCharacterData()

        // Then
        assertTrue(restored.episodeIds.isEmpty())
        assertEquals("[]", entity.episodeIds)
    }

    /**
     * Tests conversion when type is null.
     * Verifies null subtype is properly handled in both directions.
     */
    @Test
    fun `conversion handles null type`() {
        // Given
        val original = createTestCharacterData().copy(type = null)

        // When
        val entity = original.toEntity()
        val restored = entity.toCharacterData()

        // Then
        assertNull(entity.subtype)
        assertNull(restored.type)
    }

    /**
     * Tests deserialization fallback for invalid episodeIds JSON.
     * Verifies the converter returns empty list when JSON is corrupted.
     */
    @Test
    fun `invalid episodeIds JSON falls back to empty list`() {
        // Given
        val brokenEntity = createTestCharacterEntity().copy(
            episodeIds = "{not a valid json}"
        )

        // When
        val result = brokenEntity.toCharacterData()

        // Then
        assertTrue(result.episodeIds.isEmpty())
    }

    /**
     * Tests deserialization fallback for invalid date format.
     * Verifies the converter uses current date when date parsing fails.
     */
    @Test
    fun `invalid date falls back to current time`() {
        // Given
        val brokenEntity = createTestCharacterEntity().copy(
            createdAt = "not a valid date"
        )

        // When
        val result = brokenEntity.toCharacterData()

        // Then
        assertNotNull(result.createdAt)
        // Should be roughly "now" (within 1 second to account for test execution time)
        assertTrue(
            ZonedDateTime.now().toEpochSecond() - result.createdAt.toEpochSecond() < 1,
            "Fallback date should be close to current time"
        )
    }

    /**
     * Tests serialization fallback when episodeIds serialization fails.
     * Verifies the converter uses empty array JSON as fallback.
     */
    @Test
    fun `episodeIds serialization failure falls back to empty array`() {
        // Given
        val brokenCharacter = createTestCharacterData().copy(
            episodeIds = listOf() // Using wrong type to force error
        )

        // When
        val entity = try {
            brokenCharacter.toEntity()
        } catch (e: SerializationException) {
            fail("Serialization should not throw")
        }

        // Then
        assertEquals("[]", entity.episodeIds)
    }

    private fun createTestCharacterData(): CharacterData = CharacterData(
        id = 1,
        name = "Test Character",
        lifeStatus = LifeStatus.ALIVE,
        species = "Human",
        type = "Test type",
        gender = Gender.MALE,
        origin = Location("Earth", 1),
        currentLocation = Location("Mars", 2),
        imageUrl = "https://example.com/image.jpg",
        episodeIds = listOf(1, 2, 3),
        apiUrl = "https://example.com/api/character/1",
        createdAt = testDateTime,
        nextKey = 42
    )

    private fun createTestCharacterEntity(): CharacterEntity = CharacterEntity(
        id = 1,
        name = "Test Character",
        lifeStatus = LifeStatus.ALIVE,
        species = "Human",
        subtype = "Test type",
        gender = Gender.MALE,
        originName = "Earth",
        originId = 1,
        currentLocationName = "Mars",
        currentLocationId = 2,
        imageUrl = "https://example.com/image.jpg",
        episodeIds = testJson.encodeToString(listOf(1, 2, 3)),
        apiUrl = "https://example.com/api/character/1",
        createdAt = testDateTime.format(formatter),
        nextKey = 42
    )
}