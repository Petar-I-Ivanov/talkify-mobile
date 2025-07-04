package uni.fmi.masters.talkify.service.api.base

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uni.fmi.masters.talkify.service.api.ChannelApi
import uni.fmi.masters.talkify.service.api.ChannelMemberApi
import uni.fmi.masters.talkify.service.api.CsrfApi
import uni.fmi.masters.talkify.service.api.FriendshipApi
import uni.fmi.masters.talkify.service.api.MessageApi
import uni.fmi.masters.talkify.service.api.UserApi

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    fun provideUserApi(): UserApi {
        return ApiClient.create(UserApi::class.java)
    }

    @Provides
    fun provideChannelApi(): ChannelApi {
        return ApiClient.create(ChannelApi::class.java)
    }

    @Provides
    fun provideMessageApi(): MessageApi {
        return ApiClient.create(MessageApi::class.java)
    }

    @Provides
    fun provideChannelMemberApi(): ChannelMemberApi {
        return ApiClient.create(ChannelMemberApi::class.java)
    }

    @Provides
    fun provideFriendshipApi(): FriendshipApi {
        return ApiClient.create(FriendshipApi::class.java)
    }

    @Provides
    fun provideCsrfApi(): CsrfApi {
        return ApiClient.create(CsrfApi::class.java)
    }
}