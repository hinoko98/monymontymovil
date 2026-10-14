package com.monteflor.monymontymovil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.monteflor.monymontymovil.nav.AppNav
import com.monteflor.monymontymovil.ui.theme.MonyMontyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MonyMontyTheme {
                AppNav()
            }
        }
    }
}
