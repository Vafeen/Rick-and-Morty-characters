package ru.vafeen.data.local_database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity representing a favourite character in the local database.
 *
 * @property id The unique ID of the character marked as favourite.
 */
@Entity(tableName = "favourites")
data class FavouritesEntity(
    @PrimaryKey val id: Int
)
