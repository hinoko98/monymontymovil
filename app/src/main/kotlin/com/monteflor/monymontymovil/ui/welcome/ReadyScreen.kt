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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.monteflor.monymontymovil.ui.components.PrimaryButton
import com.monteflor.monymontymovil.ui.theme.BrandOrange
import com.monteflor.monymontymovil.ui.theme.Charcoal
import com.monteflor.monymontymovil.ui.theme.LocalReduceMotion
import com.monteflor.monymontymovil.ui.theme.MmEasing
import com.monteflor.monymontymovil.ui.theme.pop
import com.monteflor.monymontymovil.ui.theme.reveal
import kotlinx.coroutines.delay

/**
 * Pantalla puente (adicional al diseño): confirma la moneda elegida con un check que se dibuja
 * y ofrece los dos caminos —crear cuenta o iniciar sesión— antes del login.
 */
@Composable
fun ReadyScreen(currencyCode: String, onLogin: () -> Unit, onCreateAccount: () -> Unit) {
    val reduce = LocalReduceMotion.current
    val draw = remember { Animatable(if (reduce) 1f else 0f) }
    LaunchedEffect(reduce) {
        if (!reduce) {
            delay(450)
            draw.animateTo(1f, tween(600, easing = MmEasing.Soft))
        }
    }

    Column(
        Modifier.fillMaxSize().background(Charcoal).statusBarsPadding().navigationBarsPadding().padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.weight(1f))
        Box(Modifier.size(132.dp).pop(100).clip(CircleShape).background(BrandOrange), contentAlignment = Alignment.Center) {
            Canvas(Modifier.size(56.dp)) {
                val path = Path().apply {
                    moveTo(size.width * 0.1f, size.height * 0.55f)
                    lineTo(size.width * 0.38f, size.height * 0.82f)
                    lineTo(size.width * 0.92f, size.height * 0.2f)
                }
                val measure = PathMeasure().apply { setPath(path, false) }
                val partial = Path()
                measure.getSegment(0f, measure.length * draw.value, partial, true)
                drawPath(partial, Charcoal, style = Stroke(9.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
            }
        }
        Spacer(Modifier.height(28.dp))
        Text(
            "¡Todo listo para empezar!",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.reveal(2),
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Ahora crea tu cuenta o entra con la que ya tienes.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.75f),
            textAlign = TextAlign.Center,
            modifier = Modifier.reveal(3),
        )
        Spacer(Modifier.height(24.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .reveal(4)
                .clip(RoundedCornerShape(18.dp))
                .background(Color.White.copy(alpha = 0.08f))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Moneda principal", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.75f))
            Text(currencyCode, style = MaterialTheme.typography.titleMedium, color = Color.White)
        }
        Spacer(Modifier.weight(1f))
        PrimaryButton("Crear mi cuenta", onCreateAccount, container = Color.White, content = com.monteflor.monymontymovil.ui.theme.ActionOrange, modifier = Modifier.reveal(5))
        Spacer(Modifier.height(10.dp))
        PrimaryButton("Ya tengo una cuenta", onLogin, container = Color.Transparent, content = Color.White, modifier = Modifier.reveal(6))
        Spacer(Modifier.height(20.dp))
    }
}
