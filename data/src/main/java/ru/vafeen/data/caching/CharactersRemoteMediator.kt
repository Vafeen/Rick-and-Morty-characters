package ru.vafeen.data.caching

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.delay
import ru.vafeen.data.local_database.entity.CharacterEntity
import ru.vafeen.domain.local_database.repository.CharacterLocalRepository
import ru.vafeen.domain.network.ResponseResult
import ru.vafeen.domain.network.repository.AllCharactersRepository
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
internal class CharactersRemoteMediator @Inject constructor(
    private val localRepository: CharacterLocalRepository,
    private val remoteRepository: AllCharactersRepository
) : RemoteMediator<Int, CharacterEntity>() {
    private var pageIndex = 0
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {
        Log.d("loading", "start loading")
        // Определяем текущую страницу
        val page = getPageIndex(loadType) ?: return MediatorResult.Success(
            endOfPaginationReached = true
        )

        val limit = state.config.pageSize
        val offset = pageIndex * limit

        delay(2000)
        val result = remoteRepository.getAllCharacters(page)
        // Загружаем данные из сети
        return when (result) {
            is ResponseResult.Success -> {
                val (pagination, characters) = result.data

                // Очищаем базу только при первой загрузке
                if (loadType == LoadType.REFRESH) {
                    // todo здесь сделать умное обновление героев которые добавлены в избранное
                    // или это можно сделать отдельной табличкой
                    localRepository.clear()
                }

                localRepository.insert(characters)
                MediatorResult.Success(
                    endOfPaginationReached = characters.size < limit
                )
            }

            is ResponseResult.Error -> {
                MediatorResult.Error(Exception(result.stacktrace))
            }
        }
    }

    private fun getPageIndex(loadType: LoadType): Int? {
        pageIndex = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return null
            LoadType.APPEND -> pageIndex + 1
        }
        return pageIndex
    }
}