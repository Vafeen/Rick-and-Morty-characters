package ru.vafeen.domain.use_case

import ru.vafeen.domain.model.CharacterData

/**
 * Use case for fetching character data by its unique identifier.
 */
interface FetchCharacterDataUseCase {
    /**
     * Fetches the character data associated with the given ID.
     *
     * @param id The unique identifier of the character.
     * @return The corresponding [CharacterData] if found, or null otherwise.
     */
    suspend operator fun invoke(id: Int): CharacterData?
}
