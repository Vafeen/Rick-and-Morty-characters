package ru.vafeen.data.local_database.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.vafeen.data.local_database.dao.FavouritesDao
import ru.vafeen.data.local_database.entity.FavouritesEntity
import ru.vafeen.domain.local_database.repository.FavouritesLocalRepository
import javax.inject.Inject

internal class RoomFavouritesLocalRepository @Inject constructor(
    private val favouritesDao: FavouritesDao
) : FavouritesLocalRepository {
    override suspend fun addToFavourites(id: Int) =
        favouritesDao.insert(listOf(FavouritesEntity(id)))


    override suspend fun removeFromFavourites(id: Int) =
        favouritesDao.delete(listOf(FavouritesEntity(id)))

    override fun getAllFavourites(): Flow<List<Int>> = favouritesDao.getAll()
        .map { list -> list.map { it.id } }

}