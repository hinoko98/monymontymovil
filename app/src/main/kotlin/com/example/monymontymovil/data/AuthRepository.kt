package com.example.monymontymovil.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

sealed interface LoginResult {
    data object Success : LoginResult

    /** [message] ya viene listo para mostrar: dice qué pasó y qué hacer. */
    data class Failure(val message: String, val offline: Boolean = false) : LoginResult
}

class AuthRepository(
    private val client: OkHttpClient,
    private val cookieJar: PersistentCookieJar,
    private val baseUrl: String = ApiClient.baseUrl,
) {
    private val json = "application/json; charset=utf-8".toMediaType()

    /** POST /auth/login → 201 {token} + cookie de sesión. 401 credenciales; 400 validación. */
    suspend fun login(email: String, password: String): LoginResult = withContext(Dispatchers.IO) {
        val body = JSONObject().put("email", email.trim()).put("password", password).toString()
        val request = Request.Builder().url(baseUrl + "auth/login").post(body.toRequestBody(json)).build()
        try {
            client.newCall(request).execute().use { response ->
                val text = response.body?.string().orEmpty()
                when {
                    response.isSuccessful -> LoginResult.Success
                    response.code == 401 -> LoginResult.Failure("Correo o contraseña incorrectos. Revisa e intenta de nuevo.")
                    response.code == 400 -> LoginResult.Failure(firstValidationMessage(text) ?: "Revisa los datos que escribiste.")
                    else -> LoginResult.Failure("Tuvimos un problema de nuestro lado. Intenta de nuevo en unos minutos.")
                }
            }
        } catch (e: IOException) {
            LoginResult.Failure("No pudimos conectar con MonyMonty. Revisa tu internet e intenta de nuevo.", offline = true)
        }
    }

    /** GET /auth/check → 200 si la cookie guardada sigue siendo una sesión válida. */
    suspend fun hasActiveSession(): Boolean = withContext(Dispatchers.IO) {
        val request = Request.Builder().url(baseUrl + "auth/check").get().build()
        try {
            client.newCall(request).execute().use { it.isSuccessful }
        } catch (e: IOException) {
            false
        }
    }

    suspend fun logout() = withContext(Dispatchers.IO) {
        val request = Request.Builder().url(baseUrl + "auth/logout").get().build()
        try {
            client.newCall(request).execute().close()
        } catch (_: IOException) {
            // Sin red igual cerramos localmente: la cookie se borra abajo.
        }
        cookieJar.clear()
    }

    // Formato de express-validator: {"errors":[{"msg":"...","path":"email"}]}
    private fun firstValidationMessage(text: String): String? = runCatching {
        JSONObject(text).getJSONArray("errors").getJSONObject(0).getString("msg")
    }.getOrNull()
}
