package com.example.monymontymovil.ui.theme

import android.provider.Settings
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

// Principios de movimiento (pág. 4): cada animación explica algo, nada se mueve por adorno.
object MmEasing {
    val Soft = CubicBezierEasing(0.22f, 0.9f, 0.24f, 1f)
    val Bounce = CubicBezierEasing(0.34f, 1.56f, 0.64f, 1f)
}

/** Con "quitar animaciones" activo en el teléfono, todo aparece sin movimiento y nada se pierde. */
val LocalReduceMotion = compositionLocalOf { false }

@Composable
fun ProvideMotionPreferences(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val reduce = remember {
        Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f
    }
    CompositionLocalProvider(LocalReduceMotion provides reduce, content = content)
}

/** Entrada escalonada: 700 ms, +60 ms por elemento. [active] permite relanzarla al llegar a una página. */
@Composable
fun Modifier.reveal(index: Int = 0, active: Boolean = true, distance: Dp = 24.dp): Modifier {
    val reduce = LocalReduceMotion.current
    val progress = remember { Animatable(if (reduce) 1f else 0f) }
    LaunchedEffect(active, reduce) {
        when {
            reduce -> progress.snapTo(1f)
            active -> {
                delay(60L * index)
                progress.animateTo(1f, tween(700, easing = MmEasing.Soft))
            }
            else -> progress.snapTo(0f)
        }
    }
    val px = with(LocalDensity.current) { distance.toPx() }
    return graphicsLayer {
        alpha = progress.value
        translationY = (1f - progress.value) * px
    }
}

/** Pop con rebote: íconos, insignias y diálogos (500–700 ms). */
@Composable
fun Modifier.pop(delayMs: Int = 0, active: Boolean = true): Modifier {
    val reduce = LocalReduceMotion.current
    val progress = remember { Animatable(if (reduce) 1f else 0f) }
    LaunchedEffect(active, reduce) {
        when {
            reduce -> progress.snapTo(1f)
            active -> {
                delay(delayMs.toLong())
                progress.animateTo(1f, tween(650, easing = MmEasing.Bounce))
            }
            else -> progress.snapTo(0f)
        }
    }
    return graphicsLayer {
        val p = progress.value
        scaleX = p
        scaleY = p
        alpha = (p * 2f).coerceIn(0f, 1f)
    }
}

/** Flotación: ilustraciones del onboarding, 4–6 s en bucle con ease-in-out. */
@Composable
fun Modifier.floating(amplitude: Dp = 8.dp, periodMs: Int = 5000): Modifier {
    if (LocalReduceMotion.current) return this
    val transition = rememberInfiniteTransition(label = "float")
    val offset = transition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(periodMs / 2, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "floatOffset",
    )
    val px = with(LocalDensity.current) { amplitude.toPx() }
    return graphicsLayer { translationY = offset.value * px }
}

/** Respiración suave (manchas del splash). */
@Composable
fun Modifier.breathing(scaleDelta: Float = 0.06f, periodMs: Int = 4000): Modifier {
    if (LocalReduceMotion.current) return this
    val transition = rememberInfiniteTransition(label = "breath")
    val s = transition.animateFloat(
        initialValue = 1f,
        targetValue = 1f + scaleDelta,
        animationSpec = infiniteRepeatable(tween(periodMs, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "breathScale",
    )
    return graphicsLayer {
        scaleX = s.value
        scaleY = s.value
    }
}

/** Respuesta al toque: los botones se encogen al 97 % con resorte (250 ms aprox). */
@Composable
fun Modifier.pressScale(source: MutableInteractionSource): Modifier {
    val pressed = source.collectIsPressedAsState().value
    val scale = animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "press",
    )
    return scale(scale.value)
}
