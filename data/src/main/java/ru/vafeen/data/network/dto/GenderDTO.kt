package ru.vafeen.data.network.dto

import com.google.gson.annotations.SerializedName

/**
 * Character gender values
 */
enum class GenderDTO {
    @SerializedName("Male")
    MALE,

    @SerializedName("Female")
    FEMALE,

    @SerializedName("Genderless")
    GENDERLESS,

    @SerializedName("unknown")
    UNKNOWN
}