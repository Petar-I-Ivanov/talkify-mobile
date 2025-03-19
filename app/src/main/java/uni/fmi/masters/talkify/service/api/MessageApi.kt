package uni.fmi.masters.talkify.service.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import uni.fmi.masters.talkify.model.common.PagedModel
import uni.fmi.masters.talkify.model.message.Message
import uni.fmi.masters.talkify.model.message.MessageCreateRequest
import uni.fmi.masters.talkify.model.message.MessageUpdateRequest

interface MessageApi {
    companion object {
        const val BASE_URL = "/api/v1/messages"
    }

    @POST(BASE_URL)
    suspend fun create(@Body request: MessageCreateRequest): Response<Message>

    @GET(BASE_URL)
    suspend fun getAllByCriteria(@Query("channelId") channelId: String,
                                 @Query("page") page: Number,
                                 @Query("size") size: Number,
                                 @Query("sort") sort: String): Response<PagedModel<Message>>

    @PUT("$BASE_URL/{id}")
    suspend fun update(@Path("id") id: String,
                       @Body request: MessageUpdateRequest): Response<Message>

    @DELETE("$BASE_URL/{id}")
    suspend fun delete(@Path("id") id: String): Response<Void>
}