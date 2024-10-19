package com.example.sumsubtesttask.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import java.util.concurrent.Executor
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoroutinesModule {

    @DefaultExecutor
    @Singleton
    @Provides
    fun provideDefaultExecutor(): Executor {
        return Dispatchers.Default.asExecutor()
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultExecutor
