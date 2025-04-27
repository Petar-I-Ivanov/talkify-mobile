package uni.fmi.masters.talkify.service.api.base

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class CookieManager : CookieJar {

    private val store = mutableMapOf<String, List<Cookie>>()

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return store[url.host] ?: emptyList()
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        store[url.host] = cookies
    }

    fun getCookiesForHost(host: String): List<Cookie> {
        return store[host] ?: emptyList();
    }
}