package uni.fmi.masters.talkify.service.api.base

import okhttp3.Cookie
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private val cookieManager = CookieManager()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://9c7b-5-53-198-14.ngrok-free.app")
        .client(OkHttpClient.Builder()
            .cookieJar(cookieManager) // Use custom CookieManager
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY // Logs request and response details
            })
            .build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }

    fun isUserLoggedIn(): Boolean {
        return cookieManager.getCookiesForHost("9c7b-5-53-198-14.ngrok-free.app")
            .any { cookie -> cookie.name == "JSESSIONID" }
    }
}