package com.example.monymontymovil

import android.app.Application
import com.example.monymontymovil.data.ApiClient
import com.example.monymontymovil.data.AppPrefs
import com.example.monymontymovil.data.AuthRepository
import com.example.monymontymovil.data.PersistentCookieJar

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
