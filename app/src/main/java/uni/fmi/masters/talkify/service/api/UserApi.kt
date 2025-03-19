package uni.fmi.masters.talkify.service.api;

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST;
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import uni.fmi.masters.talkify.model.common.PagedModel
import uni.fmi.masters.talkify.model.common.UniqueValueRequest
import uni.fmi.masters.talkify.model.user.User
import uni.fmi.masters.talkify.model.user.UserCreateRequest
import uni.fmi.masters.talkify.model.user.UserUpdateRequest

interface UserApi {
    companion object {
        const val BASE_URL = "/api/v1/users"
    }

    @POST(BASE_URL)
    suspend fun create(@Body request: UserCreateRequest): Response<User>

    @POST("$BASE_URL/register")
    suspend fun register(@Body request: UserCreateRequest): Response<User>

    @GET("$BASE_URL/exists/username")
    suspend fun existsByName(@Query("value") value: String,
                             @Query("exceptId") exceptId: String): Response<Boolean>

    @GET("$BASE_URL/{id}")
    suspend fun getById(@Path("id") id: String): Response<User>

    @GET("$BASE_URL/current")
    suspend fun getCurrent(): Response<User>

    @GET(BASE_URL)
    suspend fun getAllByCriteria(@Query("search") search: String,
                                 @Query("username") username: String,
                                 @Query("email") email: String,
                                 @Query("inChannelId") inChannelId: String,
                                 @Query("notInChannelId") notInChannelId: String,
                                 @Query("onlyFriends") onlyFriends: Boolean,
                                 @Query("active") active: Boolean,
                                 @Query("page") page: Number,
                                 @Query("size") size: Number,
                                 @Query("sort") sort: String): Response<PagedModel<User>>

    @PUT("$BASE_URL/{id}")
    suspend fun update(@Path("id") id: String,
                       @Body request: UserUpdateRequest): Response<User>

    @DELETE("$BASE_URL/{id}")
    suspend fun delete(@Path("id") id: String): Response<Void>
}
