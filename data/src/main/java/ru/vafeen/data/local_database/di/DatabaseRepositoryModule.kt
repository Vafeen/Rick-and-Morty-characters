package ru.vafeen.data.local_database.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import ru.vafeen.data.local_database.repository.RoomCharacterLocalRepository
import ru.vafeen.data.local_database.repository.RoomFavouritesLocalRepository
import ru.vafeen.domain.local_database.repository.CharacterLocalRepository
import ru.vafeen.domain.local_database.repository.FavouritesLocalRepository

@Module
@InstallIn(ActivityRetainedComponent::class)
internal abstract class DatabaseRepositoryModule {
    @Binds
    abstract fun provideCharacterLocalRepository(
        roomCharacterLocalRepository: RoomCharacterLocalRepository
    ): CharacterLocalRepository

    @Binds
    abstract fun provideFavouritesLocalRepository(
        roomFavouritesLocalRepository: RoomFavouritesLocalRepository
    ): FavouritesLocalRepository
}