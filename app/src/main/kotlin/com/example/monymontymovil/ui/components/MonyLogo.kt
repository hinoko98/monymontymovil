package com.example.monymontymovil.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.monymontymovil.ui.theme.Outfit

/**
 * Logotipo MONY / MONTY con las dobles flechas hacia arriba.
 * Es una aproximación tipográfica: cuando exista el SVG oficial se reemplaza solo este composable.
 */
@Composable
fun MonyLogo(color: Color, modifier: Modifier = Modifier, fontSize: TextUnit = 40.sp) {
    val style = TextStyle(
        fontFamily = Outfit,
        fontWeight = FontWeight.Black,
        fontSize = fontSize,
        lineHeight = fontSize * 0.95f,
        letterSpacing = (-0.5).sp,
        color = color,
    )
    val chevronSize = with(LocalDensity.current) { (fontSize * 0.8f).toDp() }
    Column(modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("MONY", style = style)
            DoubleChevron(color, chevronSize)
        }
        Text("MONTY", style = style)
    }
}

@Composable
private fun DoubleChevron(color: Color, size: Dp) {
    Canvas(Modifier.size(size)) {
        val w = this.size.width
        val stroke = Stroke(width = w * 0.17f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        for (i in 0..1) {
            val y = w * (0.42f + i * 0.34f)
            val path = Path().apply {
                moveTo(w * 0.12f, y)
                lineTo(w * 0.5f, y - w * 0.3f)
                lineTo(w * 0.88f, y)
            }
            drawPath(path, color, style = stroke)
        }
    }
}
