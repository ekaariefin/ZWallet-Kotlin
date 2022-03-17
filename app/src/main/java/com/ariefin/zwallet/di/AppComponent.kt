package com.ariefin.zwallet.di

import android.content.Context
import com.ariefin.zwallet.data.ZWalletDataSource
import com.ariefin.zwallet.data.api.ZWalletApi
import com.ariefin.zwallet.network.NetworkConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppComponent {
    @Provides
    fun provideAPI(@ApplicationContext context: Context): ZWalletApi = NetworkConfig(context)
        .buildApi()

    @Provides
    @Singleton
    fun provideDataSource(apiClient: ZWalletApi): ZWalletDataSource = ZWalletDataSource(apiClient)
    
}