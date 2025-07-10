package ru.vafeen.domain.network.repository

import ru.vafeen.domain.model.CharacterData
import ru.vafeen.domain.network.ResponseResult

interface SingleCharacterRepository {
    suspend fun getCharacter(id: Int): ResponseResult<CharacterData>
}