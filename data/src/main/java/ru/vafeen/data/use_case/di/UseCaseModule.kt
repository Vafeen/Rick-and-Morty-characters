package ru.vafeen.data.use_case.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import ru.vafeen.data.use_case.FetchCharacterDataUseCaseImpl
import ru.vafeen.domain.use_case.FetchCharacterDataUseCase

@Module
@InstallIn(ActivityRetainedComponent::class)
internal abstract class UseCaseModule {
    @Binds
    abstract fun bindsFetchCharacterDataUseCase(impl: FetchCharacterDataUseCaseImpl): FetchCharacterDataUseCase
}