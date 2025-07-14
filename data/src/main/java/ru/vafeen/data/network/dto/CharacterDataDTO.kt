package ru.vafeen.data.network.dto

import com.google.gson.annotations.SerializedName

/**
 * Network DTO representing character data from API
 * @property id The id of the character
 * @property name The name of the character
 * @property status The status of the character ('Alive', 'Dead' or 'unknown')
 * @property species The species of the character
 * @property type The type or subspecies of the character
 * @property gender The gender of the character ('Female', 'Male', 'Genderless' or 'unknown')
 * @property origin Name and link to the character's origin location
 * @property location Name and link to the character's last known location endpoint
 * @property image Link to the character's image (300x300px)
 * @property episode List of episodes in which this character appeared
 * @property url Link to the character's own URL endpoint
 * @property created Time at which the character was created in the database
 */
data class CharacterDataDTO(
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