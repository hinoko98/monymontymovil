package com.monteflor.monymontymovil.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import com.monteflor.monymontymovil.R

@OptIn(androidx.compose.ui.text.ExperimentalTextApi::class)
private fun variable(res: Int, vararg weights: FontWeight) = FontFamily(
    weights.map { w ->
        Font(res, weight = w, variationSettings = FontVariation.Settings(FontVariation.weight(w.weight)))
    },
)

/** Títulos y cifras: sus formas redondas hablan el mismo idioma que el logo. */
val Outfit = variable(R.font.outfit, FontWeight.Normal, FontWeight.Medium, FontWeight.SemiBold, FontWeight.Bold, FontWeight.ExtraBold, FontWeight.Black)

/** Textos, etiquetas y formularios: legible desde 12 px. */
val DmSans = variable(R.font.dm_sans, FontWeight.Normal, FontWeight.Medium, FontWeight.SemiBold, FontWeight.Bold)
