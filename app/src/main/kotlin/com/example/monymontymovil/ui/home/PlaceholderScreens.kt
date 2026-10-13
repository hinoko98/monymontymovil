package com.example.monymontymovil.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.monymontymovil.ui.components.BackButton
import com.example.monymontymovil.ui.components.PrimaryButton
import com.example.monymontymovil.ui.components.SecondaryButton
import com.example.monymontymovil.ui.theme.Charcoal
import com.example.monymontymovil.ui.theme.Cream
import com.example.monymontymovil.ui.theme.TextMuted

/** Tablero provisional (pantalla 15 del diseño, pendiente): confirma que el login con la API funcionó. */
@Composable
fun HomePlaceholderScreen(onLogout: () -> Unit) {
    Column(
        Modifier.fillMaxSize().background(Cream).statusBarsPadding().navigationBarsPadding().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Sesión iniciada", style = MaterialTheme.typography.headlineMedium, color = Charcoal)
        Spacer(Modifier.height(8.dp))
        Text("El tablero llega en la siguiente entrega.", style = MaterialTheme.typography.bodyMedium, color = TextMuted, textAlign = TextAlign.Center)
        Spacer(Modifier.height(28.dp))
        SecondaryButton("Cerrar sesión", onLogout)
    }
}

/** Destino provisional de pantallas que aún no se construyen (registro, recuperar contraseña). */
@Composable
fun ComingSoonScreen(title: String, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Cream).statusBarsPadding().navigationBarsPadding().padding(24.dp)) {
        BackButton(onBack)
        Spacer(Modifier.height(32.dp))
        Text(title, style = MaterialTheme.typography.headlineMedium, color = Charcoal)
        Spacer(Modifier.height(8.dp))
        Text("Esta pantalla se diseña en la siguiente entrega.", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
        Spacer(Modifier.height(24.dp))
        PrimaryButton("Volver", onBack)
    }
}
