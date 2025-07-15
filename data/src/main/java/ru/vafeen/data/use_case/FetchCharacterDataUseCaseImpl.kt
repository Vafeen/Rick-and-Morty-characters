package ru.vafeen.data.use_case

import ru.vafeen.domain.local_database.repository.CharacterLocalRepository
import ru.vafeen.domain.model.CharacterData
import ru.vafeen.domain.network.ConnectivityChecker
import ru.vafeen.domain.network.ResponseResult
import ru.vafeen.domain.network.repository.CharacterRemoteRepository
import ru.vafeen.domain.use_case.FetchCharacterDataUseCase
import javax.inject.Inject

/**
 * Implementation of [FetchCharacterDataUseCase] that fetches character data
 * either from a remote repository if internet is available,
 * or from a local repository otherwise.
 *
 * @property characterRemoteRepository The remote repository for character data.
 * @property characterLocalRepository The local repository for character data.
 * @property connectivityChecker Utility to check internet connectivity.
 */
internal class FetchCharacterDataUseCaseImpl @Inject constructor(
    private val characterRemoteRepository: CharacterRemoteRepository,
    private val characterLocalRepository: CharacterLocalRepository,
    private val connectivityChecker: ConnectivityChecker,
) : FetchCharacterDataUseCase {

    override suspend fun invoke(id: Int): CharacterData? =
        if (connectivityChecker.isInternetAvailable()) {
            when (val result = characterRemoteRepository.getCharacter(id)) {
                is ResponseResult.Success<CharacterData> -> result.data.also {
                    characterLocalRepository.insert(listOf(it))
                }

                else -> null
            }
        } else {
            characterLocalRepository.getCharacter(id)
        }
}
