package uni.fmi.masters.talkify.service.api

import retrofit2.Response
import retrofit2.http.GET

interface CsrfApi {

    @GET("/csrf-token")
    suspend fun loadCsrf(): Response<Void>
}