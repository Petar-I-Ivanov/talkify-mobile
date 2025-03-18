package uni.fmi.masters.talkify.service.api.base

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uni.fmi.masters.talkify.service.api.UserApi

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    fun provideApiService(): UserApi {
        return ApiClient.create(UserApi::class.java)
    }
}