package ru.vafeen.data.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vafeen.data.network.APIInfo
import ru.vafeen.data.network.service.AllCharactersService
import ru.vafeen.data.network.service.FilterCharactersService
import ru.vafeen.data.network.service.SingleCharacterService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class NetworkServicesModule {
    private val retrofit = Retrofit.Builder().baseUrl(APIInfo.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideAllCharactersService(): AllCharactersService =
        retrofit.create(AllCharactersService::class.java)

    @Provides
    @Singleton
    fun provideSingleCharacterService(): SingleCharacterService =
        retrofit.create(SingleCharacterService::class.java)

    @Provides
    @Singleton
    fun provideFilterCharactersService(): FilterCharactersService =
        retrofit.create(FilterCharactersService::class.java)

}