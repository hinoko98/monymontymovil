package com.monteflor.monymontymovil.ui.welcome

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.monteflor.monymontymovil.ui.components.MonyLogo
import com.monteflor.monymontymovil.ui.theme.BrandOrange
import com.monteflor.monymontymovil.ui.theme.DmSans
import com.monteflor.monymontymovil.ui.theme.LocalReduceMotion
import com.monteflor.monymontymovil.ui.theme.breathing
import com.monteflor.monymontymovil.ui.theme.pop
import kotlinx.coroutines.delay

private val DeepOrange = Color(0xFFE04F25)

/**
 * 01 · Splash: el logo aparece con rebote sobre manchas que respiran.
 * [onFinished] se llama al tocar o, sola, cuando termina la comprobación de sesión.
 */
@Composable
fun SplashScreen(ready: Boolean, onFinished: () -> Unit) {
    val reduce = LocalReduceMotion.current
    val tagline = remember { Animatable(if (reduce) 1f else 0f) }

    // Duración mínima para que el logo se vea; luego espera a que la comprobación de sesión termine.
    var minTimeDone by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (!reduce) {
            delay(500)
            tagline.animateTo(1f, tween(600))
        }
        delay(1300)
        minTimeDone = true
    }
    LaunchedEffect(minTimeDone, ready) {
        if (minTimeDone && ready) onFinished()
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(BrandOrange)
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, enabled = ready, onClick = onFinished),
    ) {
        // Manchas que respiran
        Box(
            Modifier
                .size(380.dp)
                .offset(x = (-120).dp, y = (-140).dp)
                .breathing(0.05f, 4200)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.12f)),
        )
        Box(
            Modifier
                .align(Alignment.BottomEnd)
                .size(300.dp)
                .offset(x = 110.dp, y = 120.dp)
                .breathing(0.07f, 5000)
                .clip(CircleShape)
                .background(DeepOrange),
        )
        Box(
            Modifier
                .align(Alignment.TopStart)
                .offset(x = 56.dp, y = 190.dp)
                .size(14.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.7f)),
        )
        Box(
            Modifier
                .align(Alignment.CenterEnd)
                .offset(x = (-44).dp, y = (-110).dp)
                .size(8.dp)
                .clip(CircleShape)
                .background(Color(0xFF22201E)),
        )

        Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.pop(delayMs = 150)) { MonyLogo(color = Color.White, fontSize = 48.sp) }
            Text(
                "Tus finanzas, sin misterio.",
                modifier = Modifier
                    .padding(top = 18.dp)
                    .alpha(tagline.value),
                style = TextStyle(fontFamily = DmSans, fontSize = 14.sp, color = Color(0xFF22201E)),
                textAlign = TextAlign.Center,
            )
        }

        LoadingDots(Modifier.align(Alignment.BottomCenter).navigationBarsPadding().padding(bottom = 56.dp))
    }
}

/** Tres puntos que pulsan de forma escalonada: indican que algo está cargando sin ser un spinner. */
@Composable
private fun LoadingDots(modifier: Modifier = Modifier) {
    val reduce = LocalReduceMotion.current
    val transition = rememberInfiniteTransition(label = "dots")
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(3) { i ->
            val a = if (reduce) {
                0.9f
            } else {
                transition.animateFloat(
                    initialValue = 0.35f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        tween(600, delayMillis = i * 160),
                        RepeatMode.Reverse,
                    ),
                    label = "dot$i",
                ).value
            }
            Box(Modifier.size(7.dp).alpha(a).clip(CircleShape).background(Color.White))
        }
    }
}
