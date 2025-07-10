package ru.vafeen.data.network.dto

import com.google.gson.annotations.SerializedName

/**
 * Character life status values
 */
enum class LifeStatusDTO {
    @SerializedName("Alive")
    ALIVE,

    @SerializedName("Dead")
    DEAD,

    @SerializedName("unknown")
    UNKNOWN
}