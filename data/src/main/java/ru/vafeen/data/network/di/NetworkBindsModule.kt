package ru.vafeen.data.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import ru.vafeen.data.network.repository.RetrofitCharacterRemoteRepository
import ru.vafeen.data.network.service.impl.AndroidConnectivityChecker
import ru.vafeen.domain.network.ConnectivityChecker
import ru.vafeen.domain.network.repository.CharacterRemoteRepository

@Module
@InstallIn(ActivityRetainedComponent::class)
internal abstract class NetworkBindsModule {

    @Binds
    abstract fun bindsCharactersRepository(
        retrofitCharactersRepository: RetrofitCharacterRemoteRepository
    ): CharacterRemoteRepository

    @Binds
    abstract fun bindsConnectivityChecker(
        androidConnectivityChecker: AndroidConnectivityChecker
    ): ConnectivityChecker
}