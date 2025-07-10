package ru.vafeen.data.network.dto

import com.google.gson.annotations.SerializedName

/**
 * Represents the API response structure for character list endpoint
 * @property info Metadata about pagination and results
 * @property results List of character data transfer objects
 */
data class CharacterResponse(
    @SerializedName("info") val info: PageInfo,
    @SerializedName("results") val results: List<CharacterDataDTO>?
) {

    /**
     * Pagination metadata container
     * @property totalCount Total number of characters available
     * @property totalPages Total number of pages available
     * @property nextPageUrl URL for the next page of results (nullable if no more pages)
     * @property prevPageUrl URL for the previous page of results (nullable if on first page)
     */
    data class PageInfo(
        @SerializedName("count") val totalCount: Int,
        @SerializedName("pages") val totalPages: Int,
        @SerializedName("next") val nextPageUrl: String?,
        @SerializedName("prev") val prevPageUrl: String?
    )

}