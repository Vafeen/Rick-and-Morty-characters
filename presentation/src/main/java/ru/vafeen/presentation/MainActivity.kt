package ru.vafeen.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vafeen.domain.model.CharacterData
import ru.vafeen.domain.model.PaginationInfo
import ru.vafeen.domain.network.ResponseResult
import ru.vafeen.domain.network.repository.AllCharactersRepository
import ru.vafeen.domain.network.repository.FilterCharactersRepository
import ru.vafeen.domain.network.repository.SingleCharacterRepository
import ru.vafeen.presentation.ui.theme.RickAndMortyCharactersTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var allCharactersRepository: AllCharactersRepository

    @Inject
    lateinit var singleCharacterRepository: SingleCharacterRepository

    @Inject
    lateinit var filterCharactersRepository: FilterCharactersRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO) {
            val result: ResponseResult<Pair<PaginationInfo, List<CharacterData>>> =
                filterCharactersRepository.filterCharacters(
                    name = "nonexistentcharacter123"
                )
            when (result) {
                is ResponseResult.Error -> {
                    Log.e("result", result.stacktrace)
                }

                is ResponseResult.Success<Pair<PaginationInfo, List<CharacterData>>> -> {
                    Log.d("result", "${result.data}")
                }
            }

        }
        enableEdgeToEdge()
        setContent {
            RickAndMortyCharactersTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RickAndMortyCharactersTheme {
        Greeting("Android")
    }
}