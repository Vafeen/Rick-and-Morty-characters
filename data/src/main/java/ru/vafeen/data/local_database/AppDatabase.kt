package ru.vafeen.data.local_database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.vafeen.data.local_database.dao.CharacterDao
import ru.vafeen.data.local_database.dao.FavouritesDao
import ru.vafeen.data.local_database.entity.CharacterEntity
import ru.vafeen.data.local_database.entity.FavouritesEntity

/**
 * Room Database implementation for the application.
 *
 * Serves as the main access point to the persisted application data and provides DAO instances.
 *
 * The database stores [CharacterEntity] and [FavouritesEntity] objects with all related information.
 *
 * @see CharacterEntity
 * @see FavouritesEntity
 * @see CharacterDao
 * @see FavouritesDao
 */
@Database(
    entities = [CharacterEntity::class, FavouritesEntity::class],
    version = 1,
    exportSchema = true
)
internal abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides access to character data operations.
     *
     * @return [CharacterDao] instance for database operations
     */
    abstract fun characterDao(): CharacterDao

    /**
     * Provides access to favourites data operations.
     *
     * @return [FavouritesDao] instance for database operations
     */
    abstract fun favouritesDao(): FavouritesDao

    companion object {
        /**
         * Constant defining the database filename.
         * Used when creating or opening the database instance.
         */
        const val NAME = "RickAndMortyDb"
    }
}
