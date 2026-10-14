package com.monteflor.monymontymovil.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.monteflor.monymontymovil.ui.theme.ActionOrange
import com.monteflor.monymontymovil.ui.theme.Charcoal
import com.monteflor.monymontymovil.ui.theme.SandLine
import com.monteflor.monymontymovil.ui.theme.pressScale

// Áreas táctiles de 54 dp (≥ 44) y bordes de 18 dp, dentro del rango 16–28 que pide el diseño.
private val ButtonShape = RoundedCornerShape(18.dp)

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    container: Color = ActionOrange,
    content: Color = Color.White,
    showArrow: Boolean = false,
    loading: Boolean = false,
    enabled: Boolean = true,
) {
    val source = remember { MutableInteractionSource() }
    Box(
        modifier
            .fillMaxWidth()
            .heightIn(min = 54.dp)
            .pressScale(source)
            
            .clip(ButtonShape)
            .background(if (enabled) container else container.copy(alpha = 0.45f))
            .clickable(interactionSource = source, indication = null, enabled = enabled && !loading, role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (loading) {
            CircularProgressIndicator(Modifier.size(22.dp), color = content, strokeWidth = 2.5.dp)
        } else {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text, style = MaterialTheme.typography.labelLarge, color = content)
                if (showArrow) Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null, tint = content, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
fun SecondaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val source = remember { MutableInteractionSource() }
    Box(
        modifier
            .fillMaxWidth()
            .heightIn(min = 54.dp)
            .pressScale(source)
            .clip(ButtonShape)
            .background(Color.White)
            .border(BorderStroke(1.dp, SandLine), ButtonShape)
            .clickable(interactionSource = source, indication = null, role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge, color = Charcoal)
    }
}

/** Botón con flecha para avanzar en el onboarding. */
@Composable
fun ArrowFab(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val source = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(20.dp)
    Box(
        modifier
            .size(56.dp)
            .pressScale(source)
            .clip(shape)
            .background(ActionOrange)
            .clickable(interactionSource = source, indication = null, role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = "Siguiente", tint = Color.White)
    }
}

/** Botón cuadrado blanco con flecha atrás. */
@Composable
fun BackButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(14.dp)
    Box(
        modifier
            .size(44.dp)
            .clip(shape)
            .background(Color.White)
            .border(BorderStroke(1.dp, SandLine), shape)
            .clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Volver", tint = Charcoal, modifier = Modifier.size(20.dp))
    }
}
