package ru.vafeen.data.local_database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.vafeen.domain.model.Gender
import ru.vafeen.domain.model.LifeStatus

/**
 * Room entity representing a character in local database
 * @property id The id of the character
 * @property name The name of the character
 * @property lifeStatus The status of the character ('Alive', 'Dead' or 'unknown')
 * @property species The species of the character
 * @property subtype The type or subspecies of the character
 * @property gender The gender of the character ('Female', 'Male', 'Genderless' or 'unknown')
 * @property originName Name of the character's origin location
 * @property originId The id of the origin location
 * @property currentLocationName Name of the character's last known location
 * @property currentLocationId The id of the last known location
 * @property imageUrl Link to the character's image (300x300px)
 * @property episodeIds List of episodes in which this character appeared (as JSON string)
 * @property apiUrl Link to the character's own URL endpoint
 * @property createdAt Time at which the character was created in the database
 * @property nextKey Key for pagination support
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