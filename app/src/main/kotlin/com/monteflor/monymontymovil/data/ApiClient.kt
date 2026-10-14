package com.monteflor.monymontymovil.data

import android.content.Context
import com.monteflor.monymontymovil.BuildConfig
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * La API (Express) autentica con una cookie de sesión (`express-session`), no con el header Authorization.
 * Por eso el cliente necesita un CookieJar; este lo guarda en disco para que la sesión sobreviva
 * al cierre de la app y el splash pueda saltarse el login con `/auth/check`.
 */
class PersistentCookieJar(context: Context) : CookieJar {
    private val prefs = context.getSharedPreferences("monymonty_cookies", Context.MODE_PRIVATE)
    private val cookies = linkedMapOf<String, Cookie>()

    init {
        prefs.all.forEach { (key, value) ->
            val cookie = (value as? String)?.let { Cookie.parse(HttpUrl.Builder().scheme("https").host(hostFromKey(key)).build(), it) }
            if (cookie != null && cookie.expiresAt > System.currentTimeMillis()) cookies[key] = cookie
        }
    }

    // La clave incluye el host para poder reconstruir la cookie al leerla del disco.
    private fun keyOf(host: String, c: Cookie) = "$host|${c.name}|${c.path}"
    private fun hostFromKey(key: String) = key.substringBefore('|')

    @Synchronized
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val editor = prefs.edit()
        cookies.forEach { c ->
            val key = keyOf(url.host, c)
            if (c.expiresAt <= System.currentTimeMillis()) {
                this.cookies.remove(key)
                editor.remove(key)
            } else {
                this.cookies[key] = c
                editor.putString(key, c.toString())
            }
        }
        editor.apply()
    }

    @Synchronized
    override fun loadForRequest(url: HttpUrl): List<Cookie> =
        cookies.values.filter { it.matches(url) && it.expiresAt > System.currentTimeMillis() }

    @Synchronized
    fun clear() {
        cookies.clear()
        prefs.edit().clear().apply()
    }
}

object ApiClient {
    val baseUrl: String = BuildConfig.API_URL

    fun build(cookieJar: CookieJar): OkHttpClient = OkHttpClient.Builder()
        .cookieJar(cookieJar)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()
}
