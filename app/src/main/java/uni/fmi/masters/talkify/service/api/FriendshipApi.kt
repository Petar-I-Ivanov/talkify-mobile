package uni.fmi.masters.talkify.service.api

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface FriendshipApi {
    companion object {
        const val BASE_URL = "/api/v1/friendships"
    }

    @POST("$BASE_URL/{friendId}")
    suspend fun addFriend(@Path("friendId") friendId: String): Response<Void>

    @DELETE("$BASE_URL/{friendId}")
    suspend fun removeFriend(@Path("friendId") friendId: String): Response<Void>
}