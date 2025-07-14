package ru.vafeen.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import ru.vafeen.domain.network.repository.CharacterRemoteRepository
import ru.vafeen.presentation.ui.screen.main_screen.CharactersScreen
import ru.vafeen.presentation.ui.theme.RickAndMortyCharactersTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var singleCharacterRemoteRepository: SingleCharacterRemoteRepository

    @Inject
    lateinit var characterRemoteRepository: CharacterRemoteRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        lifecycleScope.launch(Dispatchers.IO) {
//            val result: ResponseResult<Pair<PaginationInfo, List<CharacterData>>> =
//                filterCharactersRepository.filterCharacters(
//                    name = "nonexistentcharacter123"
//                )
//            when (result) {
//                is ResponseResult.Error -> {
//                    Log.e("result", result.stacktrace)
//                }
//
//                is ResponseResult.Success<Pair<PaginationInfo, List<CharacterData>>> -> {
//                    Log.d("result", "${result.data}")
//                }
//            }
//
//        }
        enableEdgeToEdge()
        setContent {
            RickAndMortyCharactersTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        CharactersScreen()
                    }
                }
            }
        }
    }
}
