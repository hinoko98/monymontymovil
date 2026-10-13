package com.example.monymontymovil.ui.welcome

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.monymontymovil.ui.components.ArrowFab
import com.example.monymontymovil.ui.components.BackButton
import com.example.monymontymovil.ui.components.PrimaryButton
import com.example.monymontymovil.ui.theme.ActionOrange
import com.example.monymontymovil.ui.theme.BrandOrange
import com.example.monymontymovil.ui.theme.Charcoal
import com.example.monymontymovil.ui.theme.Cream
import com.example.monymontymovil.ui.theme.ErrorBg
import com.example.monymontymovil.ui.theme.LocalReduceMotion
import com.example.monymontymovil.ui.theme.MmEasing
import com.example.monymontymovil.ui.theme.Mustard
import com.example.monymontymovil.ui.theme.Outfit
import com.example.monymontymovil.ui.theme.Peach
import com.example.monymontymovil.ui.theme.PeachSoft
import com.example.monymontymovil.ui.theme.SandFill
import com.example.monymontymovil.ui.theme.Teal
import com.example.monymontymovil.ui.theme.TealSoft
import com.example.monymontymovil.ui.theme.TextMuted
import com.example.monymontymovil.ui.theme.floating
import com.example.monymontymovil.ui.theme.pop
import com.example.monymontymovil.ui.theme.reveal
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private class OnboardingPage(val title: String, val body: String)

// Páginas 3–5 del diseño + una cuarta (seguridad) que suma confianza justo antes de pedir datos en el login.
private val Pages = listOf(
    OnboardingPage("Registra cada\nmovimiento\nen segundos.", "Ingresos, egresos y transferencias entre tus cuentas, todo en un mismo lugar."),
    OnboardingPage("Descubre en qué\nse va tu dinero.", "Categorías y un ranking claro de tus gastos, por día, mes o año."),
    OnboardingPage("Tu información,\nbajo tu control.", "Entras con tu contraseña y decides qué se ve en pantalla. Tus números son solo tuyos."),
    OnboardingPage("Aprende, decide\nmejor y haz\ncrecer tu futuro.", "Educación financiera práctica para pasar de “¿dónde está mi plata?” a “la tengo bajo control”."),
)

/** 03–05 · Onboarding (más una página de seguridad): cada página dispara su propia animación al llegar. */
@Composable
fun OnboardingScreen(onSkip: () -> Unit, onFinish: () -> Unit, onBackToWelcome: () -> Unit) {
    val pager = rememberPagerState(pageCount = { Pages.size })
    val scope = rememberCoroutineScope()
    val last = pager.currentPage == Pages.lastIndex

    Column(Modifier.fillMaxSize().background(Cream).statusBarsPadding().navigationBarsPadding()) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 12.dp).height(44.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            BackButton({
                if (pager.currentPage == 0) onBackToWelcome() else scope.launch { pager.animateScrollToPage(pager.currentPage - 1) }
            })
            Text("${pager.currentPage + 1} DE ${Pages.size}", style = MaterialTheme.typography.labelSmall, color = TextMuted)
            Text(
                "Saltar",
                style = MaterialTheme.typography.labelLarge,
                color = ActionOrange,
                modifier = Modifier.clickable(role = Role.Button, onClick = onSkip).padding(8.dp),
            )
        }

        HorizontalPager(pager, Modifier.weight(1f)) { page ->
            val active = pager.currentPage == page
            Column(Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
                Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    when (page) {
                        0 -> RegisterIllustration(active)
                        1 -> UnderstandIllustration(active)
                        2 -> SecurityIllustration(active)
                        else -> GrowIllustration(active)
                    }
                }
                Spacer(Modifier.height(16.dp))
                PageDots(current = page, count = Pages.size)
                Spacer(Modifier.height(16.dp))
                Text(Pages[page].title, style = MaterialTheme.typography.displayLarge, color = Charcoal, modifier = Modifier.reveal(0, active))
                Spacer(Modifier.height(10.dp))
                Text(Pages[page].body, style = MaterialTheme.typography.bodyMedium, color = TextMuted, modifier = Modifier.reveal(1, active))
                Spacer(Modifier.height(16.dp))
            }
        }

        // Zona de acción fija: la flecha en las primeras páginas, el botón completo en la última.
        Box(Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(bottom = 20.dp).height(56.dp)) {
            if (last) {
                PrimaryButton("Empezar ahora", onFinish, modifier = Modifier.align(Alignment.Center))
            } else {
                ArrowFab({ scope.launch { pager.animateScrollToPage(pager.currentPage + 1) } }, Modifier.align(Alignment.CenterEnd))
            }
        }
    }
}

@Composable
private fun PageDots(current: Int, count: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        repeat(count) { i ->
            val width by animateDpAsState(if (i == current) 26.dp else 7.dp, tween(300, easing = MmEasing.Soft), label = "dotW")
            val color by animateColorAsState(if (i == current) BrandOrange else SandFill, tween(300), label = "dotC")
            Box(Modifier.height(7.dp).width(width).clip(CircleShape).background(color))
        }
    }
}

/** Progreso 0→1 que arranca cuando [active] es true (gráficas que crecen: 900–1400 ms). */
@Composable
private fun rememberProgress(active: Boolean, durationMs: Int = 1100, delayMs: Int = 200): Float {
    val reduce = LocalReduceMotion.current
    val progress = remember { Animatable(if (reduce) 1f else 0f) }
    LaunchedEffect(active, reduce) {
        when {
            reduce -> progress.snapTo(1f)
            active -> {
                delay(delayMs.toLong())
                progress.animateTo(1f, tween(durationMs, easing = MmEasing.Soft))
            }
            else -> progress.snapTo(0f)
        }
    }
    return progress.value
}

/** Entra deslizándose desde la derecha, uno tras otro. */
@Composable
private fun Modifier.slideIn(index: Int, active: Boolean): Modifier {
    val p = rememberProgress(active, durationMs = 700, delayMs = 150 + index * 140)
    return graphicsLayer {
        alpha = p
        translationX = (1f - p) * 90.dp.toPx()
    }
}

// ---------- Página 1: Registra ----------

@Composable
private fun RegisterIllustration(active: Boolean) {
    Box(contentAlignment = Alignment.Center) {
        Box(Modifier.size(250.dp).clip(CircleShape).background(Peach.copy(alpha = 0.6f)).pop(0, active))
        Column(Modifier.floating(6.dp, 5000), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            MovementCard(Icons.Rounded.ArrowUpward, TealSoft, Teal, "Salario", "Ingreso · Bancolombia", "+ $ 3.200.000", Teal, Modifier.slideIn(0, active).padding(end = 18.dp))
            MovementCard(Icons.Rounded.ArrowDownward, ErrorBg, ActionOrange, "Mercado", "Egreso · Efectivo", "− $ 186.500", ActionOrange, Modifier.slideIn(1, active).padding(start = 18.dp))
            MovementCard(Icons.Rounded.SwapHoriz, Charcoal, Color.White, "A Nequi", "Transferencia", "400.000", Charcoal, Modifier.slideIn(2, active).padding(end = 8.dp))
        }
    }
}

@Composable
private fun MovementCard(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    subtitle: String,
    amount: String,
    amountColor: Color,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(18.dp)
    Row(
        modifier
            .width(290.dp)
            .shadow(10.dp, shape, ambientColor = Charcoal.copy(alpha = 0.12f), spotColor = Charcoal.copy(alpha = 0.12f))
            .clip(shape)
            .background(Color.White)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(iconBg), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
        }
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.labelLarge, color = Charcoal)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextMuted)
        }
        // El signo (+ / −) acompaña al color: el significado no depende solo del tono.
        Text(amount, fontFamily = Outfit, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = amountColor)
    }
}

// ---------- Página 2: Entiende ----------

@Composable
private fun UnderstandIllustration(active: Boolean) {
    val p = rememberProgress(active, 1200)
    val shape = RoundedCornerShape(26.dp)
    Column(
        Modifier
            .fillMaxWidth()
            .floating(6.dp, 5200)
            .shadow(16.dp, shape, ambientColor = Charcoal.copy(alpha = 0.14f), spotColor = Charcoal.copy(alpha = 0.14f))
            .clip(shape)
            .background(Color.White)
            .padding(20.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(18.dp)) {
            Box(Modifier.size(96.dp), contentAlignment = Alignment.Center) {
                Canvas(Modifier.fillMaxSize()) {
                    val stroke = 14.dp.toPx()
                    val inset = stroke / 2
                    val arcSize = Size(size.width - stroke, size.height - stroke)
                    // Segmentos: Hogar 40 %, Transporte 25 %, Comida 20 %, resto 15 %; la dona "gira" hasta su lugar.
                    val parts = listOf(0.40f to BrandOrange, 0.25f to Charcoal, 0.20f to Mustard, 0.15f to Teal)
                    var start = -90f + (1f - p) * 120f
                    parts.forEach { (share, color) ->
                        val sweep = share * 360f * p
                        drawArc(color, start, (sweep - 2f).coerceAtLeast(0f), false, Offset(inset, inset), arcSize, style = Stroke(stroke, cap = StrokeCap.Butt))
                        start += sweep
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("40%", fontFamily = Outfit, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = Charcoal)
                    Text("Hogar", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                }
            }
            Column {
                Text("GASTOS DEL MES", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                Text("$ 1.912.300", fontFamily = Outfit, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = Charcoal)
            }
        }
        Spacer(Modifier.height(18.dp))
        RankRow("Hogar", "764.900", 1f, BrandOrange, p)
        Spacer(Modifier.height(10.dp))
        RankRow("Transporte", "478.000", 0.62f, Charcoal, p)
        Spacer(Modifier.height(10.dp))
        RankRow("Comida", "382.400", 0.5f, Mustard, p)
    }
}

@Composable
private fun RankRow(name: String, amount: String, fraction: Float, color: Color, progress: Float) {
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(name, style = MaterialTheme.typography.labelMedium, color = Charcoal)
            Text(amount, style = MaterialTheme.typography.labelMedium, color = TextMuted)
        }
        Spacer(Modifier.height(4.dp))
        Box(Modifier.fillMaxWidth().height(6.dp).clip(CircleShape).background(SandFill)) {
            Box(Modifier.fillMaxWidth(fraction * progress).height(6.dp).clip(CircleShape).background(color))
        }
    }
}

// ---------- Página 3: Seguridad (adicional) ----------

@Composable
private fun SecurityIllustration(active: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center) {
            Box(Modifier.size(160.dp).clip(CircleShape).background(PeachSoft).pop(0, active))
            Box(
                Modifier
                    .size(84.dp)
                    .floating(6.dp, 4500)
                    .pop(150, active)
                    .clip(RoundedCornerShape(26.dp))
                    .background(Charcoal),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Rounded.Lock, contentDescription = null, tint = BrandOrange, modifier = Modifier.size(40.dp))
            }
        }
        Spacer(Modifier.height(22.dp))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            TrustRow("Acceso con tu correo y contraseña", Modifier.reveal(3, active))
            TrustRow("Oculta tus saldos cuando quieras", Modifier.reveal(4, active))
            TrustRow("Tú decides qué registrar", Modifier.reveal(5, active))
        }
    }
}

@Composable
private fun TrustRow(text: String, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier
            .width(290.dp)
            .clip(shape)
            .background(Color.White)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(Modifier.size(24.dp).clip(CircleShape).background(Teal), contentAlignment = Alignment.Center) {
            Icon(Icons.Rounded.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(15.dp))
        }
        Text(text, style = MaterialTheme.typography.labelMedium, color = Charcoal)
    }
}

// ---------- Página 4: Crece ----------

@Composable
private fun GrowIllustration(active: Boolean) {
    val p = rememberProgress(active, 1400)
    val shape = RoundedCornerShape(28.dp)
    Box(
        Modifier
            .fillMaxWidth()
            .height(250.dp)
            .floating(5.dp, 5600)
            .clip(shape)
            .background(Charcoal),
    ) {
        val heights = listOf(0.22f, 0.32f, 0.42f, 0.58f, 0.84f)
        Canvas(Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 24.dp)) {
            val gap = 12.dp.toPx()
            val w = (size.width - gap * (heights.size - 1)) / heights.size
            val tops = mutableListOf<Offset>()
            heights.forEachIndexed { i, v ->
                val h = size.height * 0.85f * v * p
                val x = i * (w + gap)
                drawRoundRect(
                    color = if (i == heights.lastIndex) BrandOrange else Color.White.copy(alpha = 0.16f),
                    topLeft = Offset(x, size.height - h),
                    size = Size(w, h),
                    cornerRadius = CornerRadius(8.dp.toPx()),
                )
                tops += Offset(x + w / 2, size.height - size.height * 0.85f * v - 8.dp.toPx())
            }
            // La curva se dibuja hacia la meta después de que las barras ya subieron.
            val curve = Path().apply {
                moveTo(tops.first().x, tops.first().y)
                for (i in 1 until tops.size) {
                    val prev = tops[i - 1]
                    val cur = tops[i]
                    val mid = (prev.x + cur.x) / 2
                    cubicTo(mid, prev.y, mid, cur.y, cur.x, cur.y)
                }
            }
            val measure = PathMeasure().apply { setPath(curve, false) }
            val partial = Path()
            measure.getSegment(0f, measure.length * p, partial, true)
            drawPath(partial, Color(0xFFFFE4DA), style = Stroke(3.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
            if (p > 0.97f) {
                drawCircle(Mustard, 7.dp.toPx(), tops.last())
                drawCircle(Charcoal, 3.dp.toPx(), tops.last())
            }
        }
        // Tarjeta de meta
        Column(
            Modifier
                .padding(18.dp)
                .width(150.dp)
                .pop(500, active)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White)
                .padding(12.dp),
        ) {
            Text("Fondo de emergencia", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = Charcoal)
            Spacer(Modifier.height(6.dp))
            Box(Modifier.fillMaxWidth().height(5.dp).clip(CircleShape).background(SandFill)) {
                Box(Modifier.fillMaxWidth(0.68f * p).height(5.dp).clip(CircleShape).background(Teal))
            }
            Spacer(Modifier.height(4.dp))
            Text("68% de la meta", style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp), color = TextMuted)
        }
    }
}
