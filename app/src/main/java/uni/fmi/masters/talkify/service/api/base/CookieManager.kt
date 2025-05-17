package uni.fmi.masters.talkify.service.api.base

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class CookieManager : CookieJar {

    // Use a thread-safe map for storing cookies
    private val store = mutableMapOf<String, MutableList<Cookie>>()

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val cookies = store[url.host] ?: emptyList()

        // Filter out expired cookies
        val validCookies = cookies.filter { !it.isExpired() }
        store[url.host] = validCookies.toMutableList()
        return validCookies
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        // Save all cookies received in a thread-safe manner
        val currentCookies = store[url.host]?.toMutableList() ?: mutableListOf()
        currentCookies.removeAll { storedCookie ->
            cookies.any { newCookie -> newCookie.name == storedCookie.name }
        }
        currentCookies.addAll(cookies)
        store[url.host] = currentCookies
    }

    fun getCookiesForHost(host: String): List<Cookie> {
        return store[host]?.filter { !it.isExpired() } ?: emptyList()
    }

    private fun Cookie.isExpired(): Boolean {
        return this.expiresAt < System.currentTimeMillis()
    }
}