package ru.vafeen.data.network.dto

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object for single character response
 * @property id Unique identifier for the character
 * @property name Full name of the character
 * @property status Current life status ('Alive', 'Dead' or 'unknown')
 * @property species Biological species classification
 * @property type Subspecies or variant information (empty if none)
 * @property gender Gender identity ('Female', 'Male', 'Genderless' or 'unknown')
 * @property origin Character's place of origin
 * @property location Character's last known location
 * @property image URL to character's avatar image (300x300px)
 * @property episode List of episode URLs where character appears
 * @property url Direct URL to this character's API endpoint
 * @property created ISO-8601 formatted creation timestamp
 */
data class SingleCharacterDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("status") val status: LifeStatusDTO,
    @SerializedName("species") val species: String,
    @SerializedName("type") val type: String,
    @SerializedName("gender") val gender: GenderDTO,
    @SerializedName("origin") val origin: LocationDTO,
    @SerializedName("location") val location: LocationDTO,
    @SerializedName("image") val image: String,
    @SerializedName("episode") val episode: List<String>,
    @SerializedName("url") val url: String,
    @SerializedName("created") val created: String
)