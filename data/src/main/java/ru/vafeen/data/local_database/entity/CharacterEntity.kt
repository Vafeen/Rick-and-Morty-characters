package ru.vafeen.data.local_database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.vafeen.domain.model.Gender
import ru.vafeen.domain.model.LifeStatus

/**
 * Room Entity class representing a character in the local database.
 * Stores all character properties with type conversions:
 * - Enums are stored as Strings
 * - Lists are serialized to JSON strings
 * - Timestamps are stored in ISO format
 *
 * @property id Unique character identifier
 * @property name Character name
 * @property lifeStatus Character life status (converted from enum to String)
 * @property species Character species classification
 * @property subtype Character subtype or subspecies (nullable)
 * @property gender Character gender (converted from enum to String)
 * @property originName Origin location name
 * @property originId Origin location ID (nullable)
 * @property currentLocationName Current location name
 * @property currentLocationId Current location ID (nullable)
 * @property imageUrl URL of character's image
 * @property episodeIds List of episode IDs (stored as JSON string)
 * @property apiUrl API endpoint URL for this character
 * @property createdAt Creation timestamp in ISO format
 * @property nextKey Key for paging
 */
@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    @ColumnInfo(name = "life_status") val lifeStatus: LifeStatus,
    val species: String,
    val subtype: String?,
    val gender: Gender,
    @ColumnInfo(name = "origin_name") val originName: String,
    @ColumnInfo(name = "origin_id") val originId: Int?,
    @ColumnInfo(name = "current_location_name") val currentLocationName: String,
    @ColumnInfo(name = "current_location_id") val currentLocationId: Int?,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "episode_ids") val episodeIds: String,
    @ColumnInfo(name = "api_url") val apiUrl: String,
    @ColumnInfo(name = "created_at") val createdAt: String,
    val nextKey: Int? = null
)