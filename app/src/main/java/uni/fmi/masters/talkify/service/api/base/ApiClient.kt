package uni.fmi.masters.talkify.service.api.base

import okhttp3.Cookie
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private val cookieManager = CookieManager()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://localhost:8080")
        .client(OkHttpClient.Builder()
            .cookieJar(cookieManager)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }

    fun isUserLoggedIn(): Boolean {
        return cookieManager.getCookiesForHost("localhost")
            .any { cookie -> cookie.name == "JSESSIONID" }
    }
}