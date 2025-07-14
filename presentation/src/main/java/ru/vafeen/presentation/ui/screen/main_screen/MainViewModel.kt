package ru.vafeen.presentation.ui.screen.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.vafeen.domain.local_database.repository.CharacterLocalRepository
import ru.vafeen.domain.model.CharacterData
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val characterLocalRepository: CharacterLocalRepository,
) : ViewModel() {

    val charactersFlow: Flow<PagingData<CharacterData>> =
        characterLocalRepository.getPaged().cachedIn(viewModelScope)

    fun insert(characterData: CharacterData) {
        viewModelScope.launch(Dispatchers.IO) {
            characterLocalRepository.insert(listOf(characterData))
        }
    }
}