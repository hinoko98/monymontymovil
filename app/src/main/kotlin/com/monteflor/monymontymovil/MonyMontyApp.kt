package com.monteflor.monymontymovil

import android.app.Application
import com.monteflor.monymontymovil.data.ApiClient
import com.monteflor.monymontymovil.data.AppPrefs
import com.monteflor.monymontymovil.data.AuthRepository
import com.monteflor.monymontymovil.data.PersistentCookieJar

class MonyMontyApp : Application() {
    lateinit var prefs: AppPrefs
        private set
    lateinit var auth: AuthRepository
        private set

    override fun onCreate() {
        super.onCreate()
        prefs = AppPrefs(this)
        val cookieJar = PersistentCookieJar(this)
        auth = AuthRepository(ApiClient.build(cookieJar), cookieJar)
    }
}
