package ru.vafeen.domain.local_database.repository

import kotlinx.coroutines.flow.Flow

interface FavouritesLocalRepository {
    suspend fun addToFavourites(id: Int)
    suspend fun removeFromFavourites(id: Int)
    fun getAllFavourites(): Flow<List<Int>>
}