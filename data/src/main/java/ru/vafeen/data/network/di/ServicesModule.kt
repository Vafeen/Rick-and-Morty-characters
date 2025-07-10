package ru.vafeen.data.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vafeen.data.network.service.AllCharactersService

@Module
@InstallIn(SingletonComponent::class)
internal class ServicesModule {
    @Provides
    fun provideAllCharactersService(): AllCharactersService =
        Retrofit
            .Builder()
            .baseUrl(AllCharactersService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AllCharactersService::class.java)

}