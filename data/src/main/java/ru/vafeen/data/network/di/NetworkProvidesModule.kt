package ru.vafeen.data.network.di

import android.content.Context
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vafeen.data.network.APIInfo
import ru.vafeen.data.network.service.CharacterService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class NetworkProvidesModule {

    @Provides
    @Singleton
    fun provideFilterCharactersService(): CharacterService =
        Retrofit
            .Builder()
            .baseUrl(APIInfo.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CharacterService::class.java)

    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

}