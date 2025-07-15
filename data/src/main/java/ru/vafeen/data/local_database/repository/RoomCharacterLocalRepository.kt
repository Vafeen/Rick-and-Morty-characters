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
import ru.vafeen.domain.network.repository.CharacterRemoteRepository
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
 * @property characterDao Data Access Object for character operations
 * @property remoteMediatorFactory Factory to create [CharactersRemoteMediator] for remote mediation.
 */
internal class RoomCharacterLocalRepository @Inject constructor(
    private val characterDao: CharacterDao,
    private val remoteMediatorFactory: CharactersRemoteMediator.Factory,
    private val characterRemoteRepository: CharacterRemoteRepository,
) : CharacterLocalRepository {

    /**
     * Retrieves all characters from local database as a reactive Flow.
     *
     * @return [Flow] emitting complete list of [CharacterData].
     */
    override fun getAll(): Flow<List<CharacterData>> =
        characterDao.getAll().map { list -> list.map { it.toCharacterData() } }

    /**
     * Retrieves characters with pagination support using offset and limit.
     *
     * @param offset The starting position in the dataset.
     * @param limit The maximum number of characters to retrieve.
     * @return List of [CharacterData] for the requested page.
     */
    override suspend fun getPagedCharacters(offset: Int, limit: Int): List<CharacterData> =
        characterDao.getPagedCharacters(offset = offset, limit = limit)
            .map { it.toCharacterData() }

    /**
     * Retrieves characters through Jetpack Paging 3 integration with remote mediation support.
     *
     * @return [Flow] of [PagingData]<[CharacterData]> for efficient paginated loading.
     */
    @OptIn(ExperimentalPagingApi::class)
    override fun getPaged(
        name: String?,
        status: String?,
        species: String?,
        type: String?,
        gender: String?
    ): Flow<PagingData<CharacterData>> = Pager(
        initialKey = 1,
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = true,
        ),
        pagingSourceFactory = {
            characterDao.getPagingSource(
                name = name,
                status = status,
                species = species,
                type = type,
                gender = gender
            )
        },
        remoteMediator = remoteMediatorFactory.create(this) { page ->
            characterRemoteRepository.getCharacters(
                name = name,
                status = status,
                species = species,
                type = type,
                gender = gender,
                page = page
            )
        },
    ).flow.map { data -> data.map { it.toCharacterData() } }

    /**
     * Inserts or updates characters in local database.
     *
     * @param characters List of [CharacterData] to be inserted or updated.
     */
    override suspend fun insert(characters: List<CharacterData>) =
        characterDao.insert(characters.map { it.toEntity() })

    /**
     * Deletes characters from local database.
     *
     * @param characters List of [CharacterData] to be deleted.
     */
    override suspend fun delete(characters: List<CharacterData>) =
        characterDao.delete(characters.map { it.toEntity() })

    /**
     * Clears all characters from the local database.
     */
    override suspend fun clear() {
        characterDao.clear()
    }

    /**
     * Returns the total number of character records stored locally.
     *
     * @return Total count of characters.
     */
    override suspend fun getCharactersCount(): Int = characterDao.getCharactersCount()

    /**
     * Fetches a single character by its unique identifier.
     *
     * @param id The unique identifier of the character.
     * @return The character data, or null if not found.
     */
    override suspend fun getCharacter(id: Int): CharacterData? =
        characterDao.getCharacter(id)?.toCharacterData()

    private companion object {
        private const val PAGE_SIZE = 20
    }
}
