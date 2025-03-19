package uni.fmi.masters.talkify.service.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import uni.fmi.masters.talkify.model.channel.Channel
import uni.fmi.masters.talkify.model.channel.ChannelCreateUpdateRequest
import uni.fmi.masters.talkify.model.common.PagedModel

interface ChannelApi {
    companion object {
        const val BASE_URL = "/api/v1/channels"
    }

    @POST(BASE_URL)
    suspend fun create(@Body request: ChannelCreateUpdateRequest): Response<Channel>

    @GET("$BASE_URL/exists/name")
    suspend fun existsByName(@Query("value") value: String,
                             @Query("exceptId") exceptId: String): Response<Boolean>

    @GET("$BASE_URL/{id}")
    suspend fun getById(@Path("id") id: String): Response<Channel>

    @GET(BASE_URL)
    suspend fun getAllByCriteria(@Query("name") name: String,
                                 @Query("userId") userId: String,
                                 @Query("ownerId") ownerId: String,
                                 @Query("adminId") adminId: String,
                                 @Query("guestId") guestId: String,
                                 @Query("active") active: Boolean,
                                 @Query("page") page: Number,
                                 @Query("size") size: Number,
                                 @Query("sort") sort: String): Response<PagedModel<Channel>>

    @PUT("$BASE_URL/{id}")
    suspend fun update(@Path("id") id: String,
                       @Body request: ChannelCreateUpdateRequest): Response<Channel>

    @DELETE("$BASE_URL/{id}")
    suspend fun delete(@Path("id") id: String): Response<Void>
}