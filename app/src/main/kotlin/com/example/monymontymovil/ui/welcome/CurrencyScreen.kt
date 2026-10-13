package com.example.monymontymovil.ui.welcome

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.monymontymovil.data.Currency
import com.example.monymontymovil.data.StartCurrencies
import com.example.monymontymovil.ui.components.BackButton
import com.example.monymontymovil.ui.components.PrimaryButton
import com.example.monymontymovil.ui.theme.ActionOrange
import com.example.monymontymovil.ui.theme.BrandOrange
import com.example.monymontymovil.ui.theme.Charcoal
import com.example.monymontymovil.ui.theme.Cream
import com.example.monymontymovil.ui.theme.Outfit
import com.example.monymontymovil.ui.theme.PeachSoft
import com.example.monymontymovil.ui.theme.SandFill
import com.example.monymontymovil.ui.theme.SandLine
import com.example.monymontymovil.ui.theme.TextMuted
import com.example.monymontymovil.ui.theme.pop
import com.example.monymontymovil.ui.theme.reveal

/** 06 · Elige tu divisa: la selección se anima y el botón cambia a la moneda elegida. */
@Composable
fun CurrencyScreen(initialCode: String, onBack: () -> Unit, onContinue: (Currency) -> Unit) {
    var selected by remember { mutableStateOf(StartCurrencies.firstOrNull { it.code == initialCode } ?: StartCurrencies.first()) }

    Column(Modifier.fillMaxSize().background(Cream).statusBarsPadding().navigationBarsPadding().padding(horizontal = 24.dp)) {
        Spacer(Modifier.height(12.dp))
        BackButton(onBack)
        Spacer(Modifier.height(24.dp))
        Text("¿En qué moneda\nmanejas tu dinero?", style = MaterialTheme.typography.headlineMedium, color = Charcoal, modifier = Modifier.reveal(0))
        Spacer(Modifier.height(8.dp))
        Text(
            "Será tu divisa principal. Puedes tener cuentas en otras monedas cuando quieras.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextMuted,
            modifier = Modifier.reveal(1),
        )
        Spacer(Modifier.height(20.dp))
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            StartCurrencies.forEachIndexed { i, currency ->
                CurrencyRow(currency, currency == selected, { selected = currency }, Modifier.reveal(2 + i))
            }
        }
        Spacer(Modifier.height(12.dp))
        PrimaryButton("Continuar con ${selected.code}", { onContinue(selected) })
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun CurrencyRow(currency: Currency, selected: Boolean, onSelect: () -> Unit, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(18.dp)
    val border by animateColorAsState(if (selected) BrandOrange else SandLine, tween(250), label = "border")
    val bg by animateColorAsState(if (selected) PeachSoft else Color.White, tween(250), label = "bg")
    Row(
        modifier
            .fillMaxWidth()
            .clip(shape)
            .background(bg)
            .border(BorderStroke(if (selected) 1.5.dp else 1.dp, border), shape)
            .selectable(selected = selected, role = Role.RadioButton, onClick = onSelect)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Box(Modifier.size(44.dp).clip(RoundedCornerShape(14.dp)).background(SandFill), contentAlignment = Alignment.Center) {
            Text(currency.symbol, fontFamily = Outfit, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Charcoal)
        }
        Column(Modifier.weight(1f)) {
            Text(currency.name, style = MaterialTheme.typography.labelLarge, color = Charcoal)
            Text(currency.code, style = MaterialTheme.typography.bodySmall, color = TextMuted)
        }
        // El check aparece con rebote al elegir; el borde naranja ya indica selección, no solo el color del check.
        if (selected) {
            Box(Modifier.size(26.dp).pop().clip(CircleShape).background(ActionOrange), contentAlignment = Alignment.Center) {
                Icon(Icons.Rounded.Check, contentDescription = "Seleccionada", tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }
    }
}
