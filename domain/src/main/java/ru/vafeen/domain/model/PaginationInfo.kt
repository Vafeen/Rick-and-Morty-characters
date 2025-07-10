package ru.vafeen.domain.model

/**
 * Pagination information container
 * @property totalCount Total number of items available
 * @property totalPages Total number of pages available
 * @property nextPage Next page number (nullable if no more pages)
 * @property prevPage Previous page number (nullable if on first page)
 */
data class PaginationInfo(
    val totalCount: Int,
    val totalPages: Int,
    val nextPage: Int?,
    val prevPage: Int?
)