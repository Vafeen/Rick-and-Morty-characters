package ru.vafeen.data.service.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.vafeen.data.service.SettingsManagerImpl
import ru.vafeen.domain.service.SettingsManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class ServicesModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(
            SettingsManagerImpl.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE
        )

    @Provides
    @Singleton
    fun provideSettingsManager(impl: SettingsManagerImpl): SettingsManager = impl

}