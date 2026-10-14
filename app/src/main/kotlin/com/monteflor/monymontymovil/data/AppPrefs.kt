package com.monteflor.monymontymovil.data

import android.content.Context

data class Currency(val code: String, val symbol: String, val name: String)

/**
 * Divisas de la pantalla de bienvenida. /divisa de la API exige sesión, así que antes de
 * entrar no se puede consultar: esta lista local es la de arranque (la API usa el código ISO como _id).
 */
val StartCurrencies = listOf(
    Currency("COP", "$", "Peso colombiano"),
    Currency("USD", "US$", "Dólar estadounidense"),
    Currency("EUR", "€", "Euro"),
    Currency("MXN", "MX$", "Peso mexicano"),
    Currency("PEN", "S/", "Sol peruano"),
)

class AppPrefs(context: Context) {
    private val prefs = context.getSharedPreferences("monymonty", Context.MODE_PRIVATE)

    var onboardingDone: Boolean
        get() = prefs.getBoolean("onboardingDone", false)
        set(value) = prefs.edit().putBoolean("onboardingDone", value).apply()

    var currencyCode: String
        get() = prefs.getString("currency", "COP") ?: "COP"
        set(value) = prefs.edit().putString("currency", value).apply()
}
