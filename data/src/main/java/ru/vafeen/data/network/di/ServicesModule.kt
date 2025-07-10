package ru.vafeen.data.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vafeen.data.network.service.APIInfo
import ru.vafeen.data.network.service.AllCharactersService
import ru.vafeen.data.network.service.SingleCharacterService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class ServicesModule {
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


}