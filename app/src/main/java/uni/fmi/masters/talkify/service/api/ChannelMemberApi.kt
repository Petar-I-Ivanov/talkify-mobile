package uni.fmi.masters.talkify.service.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import uni.fmi.masters.talkify.model.channel.members.AddChannelGuestRequest
import uni.fmi.masters.talkify.model.channel.members.ChannelMember
import uni.fmi.masters.talkify.model.common.CollectionModel

interface ChannelMemberApi {
    companion object {
        const val BASE_URL = "/api/v1/channels/{id}/members"
    }

    @POST(BASE_URL)
    suspend fun addMember(
        @Path("id") id: String,
        @Body request: AddChannelGuestRequest): Response<Void>

    @GET(BASE_URL)
    suspend fun getChannelMembers(@Path("id") id: String): Response<CollectionModel<ChannelMember>>

    @PATCH("$BASE_URL/{userId}/admin")
    suspend fun makeMemberAdmin(
        @Path("id") id: String,
        @Path("userId") userId: String): Response<Void>

    @DELETE("$BASE_URL/{userId}")
    suspend fun removeMember(
        @Path("id") id: String,
        @Path("userId") userId: String): Response<Void>
}