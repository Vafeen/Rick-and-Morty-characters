package ru.vafeen.data.caching

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import ru.vafeen.data.local_database.entity.CharacterEntity
import ru.vafeen.domain.local_database.repository.CharacterLocalRepository
import ru.vafeen.domain.network.ConnectivityChecker
import ru.vafeen.domain.network.ResponseResult
import ru.vafeen.domain.network.repository.CharacterRemoteRepository

/**
 * [RemoteMediator] implementation for handling paginated character data
 * with network-and-database synchronization.
 *
 * Handles three scenarios:
 * 1. Online mode - fetches from network and caches in local DB
 * 2. Offline mode - serves cached data when available
 * 3. Hybrid mode - uses cache when offline with periodic network refresh
 *
 * @property localRepository Local data source ([CharacterLocalRepository])
 * @property remoteRepository Network data source ([AllCharactersRepository])
 * @property connectivityChecker Network status provider ([ConnectivityChecker])
 */
@OptIn(ExperimentalPagingApi::class)
internal class CharactersRemoteMediator @AssistedInject constructor(
    @Assisted private val localRepository: CharacterLocalRepository,
    private val remoteRepository: CharacterRemoteRepository,
    private val connectivityChecker: ConnectivityChecker,
) : RemoteMediator<Int, CharacterEntity>() {

    private var pageIndex = 1

    /**
     * Loads data based on pagination requirements and network availability.
     *
     * @param loadType Type of load operation ([LoadType.REFRESH], [LoadType.APPEND], [LoadType.PREPEND])
     * @param state Current paging state containing loaded pages and scroll position
     * @return [MediatorResult] with data loading result
     */
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {
        return try {
            val isConnected = connectivityChecker.isInternetAvailable()
            val page = getPageIndex(loadType) ?: return MediatorResult.Success(
                endOfPaginationReached = true
            )

            val limit = state.config.pageSize
            val offset = pageIndex * limit

            // Offline handling for non-refresh loads
            if (!isConnected && loadType != LoadType.REFRESH) {
                return MediatorResult.Success(endOfPaginationReached = true)
            }

            if (isConnected) {
                delay(2000) // Network delay simulation
                val result = remoteRepository.getCharacters(page = page)

                when (result) {
                    is ResponseResult.Success -> {
                        val (pagination, characters) = result.data

                        if (loadType == LoadType.REFRESH) {
                            localRepository.clear()
                        }

                        localRepository.insert(characters)
                        MediatorResult.Success(
                            endOfPaginationReached = characters.isEmpty() || characters.size < limit
                        )
                    }

                    is ResponseResult.Error -> {
                        MediatorResult.Error(Exception(result.stacktrace))
                    }
                }
            } else {
                // Offline cache fallback
                if (loadType == LoadType.REFRESH) {
                    val cachedItems = localRepository.getCharactersCount()
                    MediatorResult.Success(endOfPaginationReached = cachedItems == 0)
                } else {
                    MediatorResult.Success(endOfPaginationReached = true)
                }
            }
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    /**
     * Calculates the next page index based on load type.
     *
     * @param loadType Type of load operation ([LoadType])
     * @return Page index or null if pagination should stop
     */
    private fun getPageIndex(loadType: LoadType): Int? {
        pageIndex = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return null
            LoadType.APPEND -> pageIndex + 1
        }
        return pageIndex
    }

    /**
     * AssistedFactory for Dagger-assisted injection.
     *
     * Allows creating [CharactersRemoteMediator] with [CharacterLocalRepository].
     */
    @AssistedFactory
    interface Factory {
        /**
         * Creates a new instance of [CharactersRemoteMediator].
         *
         * @param characterLocalRepository Local data source implementation
         */
        fun create(characterLocalRepository: CharacterLocalRepository): CharactersRemoteMediator
    }
}