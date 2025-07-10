package ru.vafeen.data.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import ru.vafeen.data.network.repository.RetrofitAllCharactersRepository
import ru.vafeen.data.network.repository.RetrofitFilterCharactersRepository
import ru.vafeen.data.network.repository.RetrofitSingleCharacterRepository
import ru.vafeen.domain.network.repository.AllCharactersRepository
import ru.vafeen.domain.network.repository.FilterCharactersRepository
import ru.vafeen.domain.network.repository.SingleCharacterRepository

@Module
@InstallIn(ActivityRetainedComponent::class)
internal abstract class NetworkRepositoryModule {
    @Binds
    abstract fun bindsAllCharactersRepository(
        retrofitAllCharactersRepository: RetrofitAllCharactersRepository
    ): AllCharactersRepository

    @Binds
    abstract fun bindsSingleCharacterRepository(
        retrofitSingleCharacterRepository: RetrofitSingleCharacterRepository
    ): SingleCharacterRepository

    @Binds
    abstract fun bindsFilterCharactersRepository(
        retrofitFilterCharactersRepository: RetrofitFilterCharactersRepository
    ): FilterCharactersRepository
}