package uni.fmi.masters.talkify.service.api.base

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "https://eba2-5-53-198-14.ngrok-free.app"
    private val cookieManager = CookieManager()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(OkHttpClient.Builder()
            .cookieJar(cookieManager) // Use custom CookieManager
            .addInterceptor(CsrfInterceptor(cookieManager))
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
        return cookieManager.getCookiesForHost(BASE_URL.replace("https://", ""))
            .any { cookie -> cookie.name == "JSESSIONID" }
    }
}