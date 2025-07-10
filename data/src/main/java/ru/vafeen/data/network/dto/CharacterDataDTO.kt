package ru.vafeen.data.network.dto

import com.google.gson.annotations.SerializedName

/**
 * Character data transfer object (DTO) representing API response structure
 * @property id Unique identifier for the character
 * @property name Full name of the character
 * @property status Current life status (Alive/Dead/unknown)
 * @property species Biological species classification
 * @property type Subspecies or variant information (empty string if none)
 * @property gender Gender identity (Male/Female/Genderless/unknown)
 * @property origin Character's original location
 * @property location Character's last known location
 * @property image URL to character's avatar image (300x300px)
 * @property episode List of episode URLs where character appears
 * @property url Direct URL to this character's API endpoint
 * @property created ISO-8601 formatted creation timestamp
 */
data class CharacterDataDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("status") val status: Status,
    @SerializedName("species") val species: String,
    @SerializedName("type") val type: String,
    @SerializedName("gender") val gender: Gender,
    @SerializedName("origin") val origin: LocationDTO,
    @SerializedName("location") val location: LocationDTO,
    @SerializedName("image") val image: String,
    @SerializedName("episode") val episode: List<String>,
    @SerializedName("url") val url: String,
    @SerializedName("created") val created: String
) {
    /**
     * Character life status values
     */
    enum class Status {
        @SerializedName("Alive")
        ALIVE,

        @SerializedName("Dead")
        DEAD,

        @SerializedName("unknown")
        UNKNOWN
    }

    /**
     * Character gender values
     */
    enum class Gender {
        @SerializedName("Male")
        MALE,

        @SerializedName("Female")
        FEMALE,

        @SerializedName("Genderless")
        GENDERLESS,

        @SerializedName("unknown")
        UNKNOWN
    }

    /**
     * Location data transfer object
     * @property name Common name of the location
     * @property url API endpoint URL for this location
     */
    data class LocationDTO(
        @SerializedName("name") val name: String,
        @SerializedName("url") val url: String
    )
}