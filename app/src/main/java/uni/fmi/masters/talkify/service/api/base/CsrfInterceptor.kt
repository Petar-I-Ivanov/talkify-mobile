package uni.fmi.masters.talkify.service.api.base

import okhttp3.CookieJar
import okhttp3.Interceptor
import okhttp3.Response

class CsrfInterceptor(private val cookieJar: CookieJar) : Interceptor {

    companion object {
        private const val CSRF_HEADER = "X-CSRF-TOKEN";
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url

        // Extract cookies for the current request URL
        val cookies = cookieJar.loadForRequest(url)
        val csrfToken = cookies.find { it.name == CSRF_HEADER }?.value

        // Add header if CSRF token is found
        val newRequest = if (csrfToken != null) {
            originalRequest.newBuilder()
                .addHeader(CSRF_HEADER, csrfToken)
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(newRequest)
    }
}