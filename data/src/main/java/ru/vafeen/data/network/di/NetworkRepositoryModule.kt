package ru.vafeen.data.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import ru.vafeen.data.network.repository.RetrofitAllCharactersRepository
import ru.vafeen.domain.network.repository.AllCharactersRepository

@Module
@InstallIn(ActivityRetainedComponent::class)
internal abstract class NetworkRepositoryModule {
    @Binds
    abstract fun bindsAllCharactersRepository(
        retrofitAllCharactersRepository: RetrofitAllCharactersRepository
    ): AllCharactersRepository
}