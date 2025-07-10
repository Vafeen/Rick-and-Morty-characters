package ru.vafeen.data.network

import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vafeen.data.network.repository.RetrofitFilterCharactersRepository
import ru.vafeen.data.network.service.FilterCharactersService
import ru.vafeen.domain.model.Gender
import ru.vafeen.domain.model.LifeStatus
import ru.vafeen.domain.network.ResponseResult

class RetrofitFilterCharactersRepositoryTest {

    private lateinit var repository: RetrofitFilterCharactersRepository

    @Before
    fun setup() {
        val retrofit = Retrofit.Builder()
            .baseUrl(APIInfo.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(FilterCharactersService::class.java)
        repository = RetrofitFilterCharactersRepository(service)
    }

    @Test
    fun `test filter by name and status`() = runBlocking {
        val result = repository.filterCharacters(
            name = "rick",
            status = "alive"
        )

        Assert.assertTrue(result is ResponseResult.Success)

        val (pagination, characters) = (result as ResponseResult.Success).data

        Assert.assertTrue(characters.isNotEmpty())
        Assert.assertTrue(characters.all { it.name.contains("Rick", ignoreCase = true) })
        Assert.assertTrue(characters.all { it.lifeStatus == LifeStatus.ALIVE })
        Assert.assertTrue(pagination.totalCount > 0)
    }

    @Test
    fun `test filter by species`() = runBlocking {
        val result = repository.filterCharacters(
            species = "Alien"
        )

        Assert.assertTrue(result is ResponseResult.Success)

        val characters = (result as ResponseResult.Success).data.second
        Assert.assertTrue(characters.isNotEmpty())
        Assert.assertTrue(characters.all { it.species == "Alien" })
    }

    @Test
    fun `test filter by gender`() = runBlocking {
        val result = repository.filterCharacters(
            gender = "female"
        )

        Assert.assertTrue(result is ResponseResult.Success)

        val characters = (result as ResponseResult.Success).data.second
        Assert.assertTrue(characters.isNotEmpty())
        Assert.assertTrue(characters.all { it.gender == Gender.FEMALE })
    }

    @Test
    fun `test empty filters returns all characters`() = runBlocking {
        val result = repository.filterCharacters()

        Assert.assertTrue(result is ResponseResult.Success)
        Assert.assertTrue((result as ResponseResult.Success).data.second.isNotEmpty())
    }

    @Test
    fun `test invalid filter returns empty list`() = runBlocking {
        val result = repository.filterCharacters(
            name = "nonexistentcharacter123"
        )

        Assert.assertTrue(result is ResponseResult.Success)
        Assert.assertTrue((result as ResponseResult.Success).data.second.isEmpty())
    }
}