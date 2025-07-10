package ru.vafeen.data.network.dto

import com.google.gson.annotations.SerializedName

/**
 * Location data transfer object
 * @property name Common name of the location
 * @property url API endpoint URL for this location
 */
data class LocationDTO(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)