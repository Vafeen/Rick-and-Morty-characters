package ru.vafeen.data.caching

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.vafeen.domain.model.CharacterData

internal class CharacterPagingSource(
    private val pageSize: Int,
    private val get: suspend (myPageConfig: MyPageConfig) -> List<CharacterData>,
) : PagingSource<Int, CharacterData>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterData> {
        return try {
            // Вычисляем текущую страницу
            val pageIndex = params.key ?: 0

            // Вычисляем offset для запроса
            val offset = (pageIndex - 1) * params.loadSize

            // Загружаем данные из локальной БД
            val characters = get.invoke(
                MyPageConfig(
                    offset = offset,
                    limit = params.loadSize
                )
            )

            LoadResult.Page(
                data = characters,
                prevKey = if (pageIndex == 1) null else pageIndex - 1,
                nextKey = if (characters.size == params.loadSize)
                    pageIndex + (params.loadSize / pageSize)
                else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CharacterData>): Int? {
        // Возвращаем ключ для позиции, ближайшей к anchorPosition
        val anchorPosition = state.anchorPosition ?: return null
        // конвертируем индекс элемента в индекс страницы
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        // Возвращаем текущий индекс страницы
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }
}