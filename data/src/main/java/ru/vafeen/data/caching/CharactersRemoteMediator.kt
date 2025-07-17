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
import ru.vafeen.domain.model.CharacterData
import ru.vafeen.domain.model.PaginationInfo
import ru.vafeen.domain.network.ConnectivityChecker
import ru.vafeen.domain.network.ResponseResult
import ru.vafeen.domain.network.repository.CharacterRemoteRepository

/**
 * [RemoteMediator] implementation for handling paginated character data
 * with network-and-database synchronization.
 *
 * This mediator handles three scenarios:
 * 1. Online mode: fetches data from network and caches it in local database.
 * 2. Offline mode: serves cached data when network is unavailable.
 * 3. Hybrid mode: uses cached data when offline and performs network refreshes when online.
 *
 * @property localRepository Local data source ([CharacterLocalRepository]) for database operations.
 * @property getFromNetwork Suspend function that fetches page data from network.
 * @property remoteRepository Remote data source ([CharacterRemoteRepository]) for network operations.
 * @property connectivityChecker Provider to check current network connectivity status.
 */
@OptIn(ExperimentalPagingApi::class)
internal class CharactersRemoteMediator @AssistedInject constructor(
    @Assisted private val localRepository: CharacterLocalRepository,
    @Assisted private val getFromNetwork: suspend (Int) -> ResponseResult<Pair<PaginationInfo, List<CharacterData>>>,
    private val remoteRepository: CharacterRemoteRepository,
    private val connectivityChecker: ConnectivityChecker,
) : RemoteMediator<Int, CharacterEntity>() {

    private var pageIndex = 1

    /**
     * Loads data according to the specified [LoadType] and current [PagingState].
     *
     * @param loadType The type of load operation. One of [LoadType.REFRESH], [LoadType.APPEND], or [LoadType.PREPEND].
     * @param state The current paging state including loaded pages and configuration.
     * @return [MediatorResult] representing success or failure of load operation.
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

            // If offline and not refreshing, do not attempt to load more
            if (!isConnected && loadType != LoadType.REFRESH) {
                return MediatorResult.Success(endOfPaginationReached = true)
            }

            if (isConnected) {
                // Simulate network delay (for demonstration)
                delay(2000)
                val result = getFromNetwork.invoke(page)

                when (result) {
                    is ResponseResult.Success -> {
                        val (pagination, characters) = result.data

//                        if (loadType == LoadType.REFRESH) {
//                            localRepository.clear()
//                        }

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
     * Calculates next page index according to the [loadType].
     *
     * @param loadType The type of load operation.
     * @return The page index for loading or null if no paging is needed.
     */
    private fun getPageIndex(loadType: LoadType): Int? {
        pageIndex = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return null // Prepend not supported
            LoadType.APPEND -> pageIndex + 1
        }
        return pageIndex
    }

    /**
     * AssistedFactory for creating instances of [CharactersRemoteMediator].
     */
    @AssistedFactory
    interface Factory {
        /**
         * Creates a new instance of [CharactersRemoteMediator].
         *
         * @param localRepository Local data source implementation.
         * @param getFromNetwork Function to fetch data from network by page index.
         * @return A new instance of [CharactersRemoteMediator].
         */
        fun create(
            @Assisted localRepository: CharacterLocalRepository,
            @Assisted getFromNetwork: suspend (Int) -> ResponseResult<Pair<PaginationInfo, List<CharacterData>>>,
        ): CharactersRemoteMediator
    }
}
