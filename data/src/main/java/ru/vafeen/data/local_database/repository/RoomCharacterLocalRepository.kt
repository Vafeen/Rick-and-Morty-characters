package ru.vafeen.data.local_database.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.vafeen.data.caching.CharactersRemoteMediator
import ru.vafeen.data.local_database.converter.toCharacterData
import ru.vafeen.data.local_database.converter.toEntity
import ru.vafeen.data.local_database.dao.CharacterDao
import ru.vafeen.domain.local_database.repository.CharacterLocalRepository
import ru.vafeen.domain.model.CharacterData
import javax.inject.Inject

/**
 * Room implementation of [CharacterLocalRepository] with paging support.
 *
 * Handles all local database operations for character data including:
 * - Retrieving all characters
 * - Paginated character loading
 * - Inserting/updating characters
 * - Deleting characters
 *
 * @property characterDao Data Access Object for character operationsg
 */
internal class RoomCharacterLocalRepository @Inject constructor(
    private val characterDao: CharacterDao,
    private val remoteMediatorFactory: CharactersRemoteMediator.Factory
) : CharacterLocalRepository {


    /**
     * Retrieves all characters from local database.
     * @return Flow emitting complete list of [CharacterData]
     */
    override fun getAll(): Flow<List<CharacterData>> =
        characterDao.getAll().map { list -> list.map { it.toCharacterData() } }


    /**
     * Retrieves characters with pagination support.
     * @return Flow of [PagingData]<[CharacterData]> for efficient paginated loading
     */
    override suspend fun getPagedCharacters(
        offset: Int,
        limit: Int
    ): List<CharacterData> = characterDao.getPagedCharacters(
        offset = offset,
        limit = limit
    ).map { it.toCharacterData() }


    @OptIn(ExperimentalPagingApi::class)
    override fun getPaged(): Flow<PagingData<CharacterData>> = Pager(
        initialKey = 1,
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = true,
        ),
        pagingSourceFactory = { characterDao.getPagingSource() },
        remoteMediator = remoteMediatorFactory.create(this),
    ).flow.map { data -> data.map { it.toCharacterData() } }


    /**
     * Inserts or updates characters in local database.
     * @param characters List of [CharacterData] to be inserted/updated
     */
    override suspend fun insert(characters: List<CharacterData>) =
        characterDao.insert(characters.map { it.toEntity() })

    /**
     * Deletes characters from local database.
     * @param characters List of [CharacterData] to be deleted
     */
    override suspend fun delete(characters: List<CharacterData>) =
        characterDao.delete(characters.map { it.toEntity() })

    override suspend fun clear() {
        characterDao.clear()
    }

    override suspend fun getCharactersCount(): Int = characterDao.getCharactersCount()

    companion object {
        private const val PAGE_SIZE = 20
    }
}