package ru.vafeen.data.local_database.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.vafeen.data.local_database.dao.FavouritesDao
import ru.vafeen.data.local_database.entity.FavouritesEntity
import ru.vafeen.domain.local_database.repository.FavouritesLocalRepository
import javax.inject.Inject

/**
 * Room implementation of [FavouritesLocalRepository] for managing favourite characters locally.
 *
 * @property favouritesDao Data Access Object for favourites-related database operations.
 */
internal class RoomFavouritesLocalRepository @Inject constructor(
    private val favouritesDao: FavouritesDao
) : FavouritesLocalRepository {

    /**
     * Adds a character to the favourites list.
     *
     * @param id The ID of the character to add as a favourite.
     */
    override suspend fun addToFavourites(id: Int) =
        favouritesDao.insert(listOf(FavouritesEntity(id)))


    /**
     * Removes a character from the favourites list.
     *
     * @param id The ID of the character to remove from favourites.
     */
    override suspend fun removeFromFavourites(id: Int) =
        favouritesDao.delete(listOf(FavouritesEntity(id)))

    /**
     * Retrieves all favourite character IDs as a reactive [Flow].
     *
     * @return [Flow] emitting the list of favourite character IDs.
     */
    override fun getAllFavourites(): Flow<List<Int>> =
        favouritesDao.getAll()
            .map { list -> list.map { it.id } }

}
