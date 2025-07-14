package ru.vafeen.data.local_database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.vafeen.data.local_database.dao.CharacterDao
import ru.vafeen.data.local_database.entity.CharacterEntity

/**
 * Room Database implementation for the application.
 *
 * Serves as the main access point to the persisted application data and provides DAO instances.
 *
 * The database stores [CharacterEntity] objects with all related character information.
 *
 * @see CharacterEntity
 * @see CharacterDao
 */
@Database(
    entities = [CharacterEntity::class],
    version = 1,
    exportSchema = true
)
internal abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides access to character data operations.
     * @return [CharacterDao] instance for database operations
     */
    abstract fun characterDao(): CharacterDao

    companion object {
        /**
         * Constant defining the database filename.
         * Used when creating/opening the database instance.
         */
        const val NAME = "RickAndMortyDb"
    }
}