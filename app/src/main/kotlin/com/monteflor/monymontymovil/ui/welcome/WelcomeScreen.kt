package com.monteflor.monymontymovil.ui.welcome

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.monteflor.monymontymovil.ui.components.PrimaryButton
import com.monteflor.monymontymovil.ui.components.SecondaryButton
import com.monteflor.monymontymovil.ui.theme.ActionOrange
import com.monteflor.monymontymovil.ui.theme.BrandOrange
import com.monteflor.monymontymovil.ui.theme.Charcoal
import com.monteflor.monymontymovil.ui.theme.Cream
import com.monteflor.monymontymovil.ui.theme.LocalReduceMotion
import com.monteflor.monymontymovil.ui.theme.MmEasing
import com.monteflor.monymontymovil.ui.theme.Mustard
import com.monteflor.monymontymovil.ui.theme.Outfit
import com.monteflor.monymontymovil.ui.theme.Peach
import com.monteflor.monymontymovil.ui.theme.SandFill
import com.monteflor.monymontymovil.ui.theme.Teal
import com.monteflor.monymontymovil.ui.theme.TextMuted
import com.monteflor.monymontymovil.ui.theme.floating
import com.monteflor.monymontymovil.ui.theme.pop
import com.monteflor.monymontymovil.ui.theme.reveal
import kotlinx.coroutines.delay

/** 02 · Bienvenida: tarjeta de saldo flotando, un ingreso que entra y dos caminos. */
@Composable
fun WelcomeScreen(onStart: () -> Unit, onHaveAccount: () -> Unit) {
    Box(Modifier.fillMaxSize().background(Cream)) {
        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp),
        ) {
            // Ilustración
            Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box(
                    Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 48.dp, y = (-24).dp)
                        .size(112.dp)
                        .pop(delayMs = 100)
                        .clip(CircleShape)
                        .background(BrandOrange),
                )
                Box(
                    Modifier
                        .size(250.dp)
                        .pop(delayMs = 0)
                        .clip(CircleShape)
                        .background(Peach.copy(alpha = 0.7f)),
                )
                BalanceCard(Modifier.floating(amplitude = 8.dp, periodMs = 5000))
                CoinBadge(Modifier.align(Alignment.TopStart).offset(x = 8.dp, y = 12.dp).pop(delayMs = 400).floating(5.dp, 4000))
                IncomeChip(Modifier.align(Alignment.BottomEnd).offset(y = (-18).dp))
            }

            Text(
                "BIENVENIDO A MONYMONTY",
                style = MaterialTheme.typography.labelSmall,
                color = ActionOrange,
                modifier = Modifier.reveal(0),
            )
            Spacer(Modifier.height(10.dp))
            Text(
                "Toma el control\nde tu dinero,\na tu ritmo.",
                style = MaterialTheme.typography.displayLarge,
                color = Charcoal,
                modifier = Modifier.reveal(1),
            )
            Spacer(Modifier.height(12.dp))
            Text(
                "Registra tus movimientos, entiende en qué se va cada peso y construye un futuro más seguro.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted,
                modifier = Modifier.reveal(2),
            )
            Spacer(Modifier.height(24.dp))
            PrimaryButton("Comenzar", onStart, showArrow = true, modifier = Modifier.reveal(3))
            Spacer(Modifier.height(10.dp))
            SecondaryButton("Ya tengo una cuenta", onHaveAccount, modifier = Modifier.reveal(4))
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun BalanceCard(modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(24.dp)
    // Barras del mini gráfico: la última (naranja) es el mes actual.
    val bars = listOf(0.35f, 0.5f, 0.4f, 0.85f, 0.55f)
    Column(
        modifier
            .size(width = 230.dp, height = 150.dp)
            .shadow(18.dp, shape, ambientColor = Charcoal.copy(alpha = 0.2f), spotColor = Charcoal.copy(alpha = 0.2f))
            .clip(shape)
            .background(Color.White)
            .padding(18.dp),
    ) {
        Text("DISPONIBLE", style = MaterialTheme.typography.labelSmall, color = TextMuted)
        Text(
            "$ 2.480.000",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
            color = Charcoal,
        )
        Spacer(Modifier.weight(1f))
        Canvas(Modifier.fillMaxWidth().height(46.dp)) {
            val gap = 8.dp.toPx()
            val w = (size.width - gap * (bars.size - 1)) / bars.size
            bars.forEachIndexed { i, v ->
                val h = size.height * v
                drawRoundRect(
                    color = if (i == 3) BrandOrange else SandFill,
                    topLeft = Offset(i * (w + gap), size.height - h),
                    size = Size(w, h),
                    cornerRadius = CornerRadius(6.dp.toPx()),
                )
            }
        }
    }
}

@Composable
private fun CoinBadge(modifier: Modifier = Modifier) {
    Box(
        modifier.size(40.dp).clip(CircleShape).background(Mustard),
        contentAlignment = Alignment.Center,
    ) {
        Text("$", fontFamily = Outfit, fontWeight = FontWeight.Black, fontSize = 20.sp, color = Charcoal)
    }
}

/** El ingreso entra deslizándose desde la derecha, un instante después de que la tarjeta ya está en pantalla. */
@Composable
private fun IncomeChip(modifier: Modifier = Modifier) {
    val reduce = LocalReduceMotion.current
    val progress = remember { Animatable(if (reduce) 1f else 0f) }
    LaunchedEffect(reduce) {
        if (!reduce) {
            delay(900)
            progress.animateTo(1f, tween(700, easing = MmEasing.Bounce))
        }
    }
    Row(
        modifier
            .graphicsLayer {
                alpha = progress.value.coerceIn(0f, 1f)
                translationX = (1f - progress.value) * 120.dp.toPx()
            }
            .clip(RoundedCornerShape(16.dp))
            .background(Charcoal)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(Modifier.size(24.dp).clip(RoundedCornerShape(8.dp)).background(Teal), contentAlignment = Alignment.Center) {
            Icon(Icons.Rounded.ArrowUpward, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
        }
        Column {
            Text("Ingreso", fontSize = 10.sp, color = Color.White.copy(alpha = 0.7f), lineHeight = 11.sp)
            Text("+ $ 350.000", fontFamily = Outfit, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White, lineHeight = 14.sp)
        }
    }
}
