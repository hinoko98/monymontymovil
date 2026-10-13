package com.example.monymontymovil.nav

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.monymontymovil.MonyMontyApp
import com.example.monymontymovil.ui.auth.LoginScreen
import com.example.monymontymovil.ui.auth.LoginViewModel
import com.example.monymontymovil.ui.home.ComingSoonScreen
import com.example.monymontymovil.ui.home.HomePlaceholderScreen
import com.example.monymontymovil.ui.theme.MmEasing
import com.example.monymontymovil.ui.welcome.CurrencyScreen
import com.example.monymontymovil.ui.welcome.OnboardingScreen
import com.example.monymontymovil.ui.welcome.ReadyScreen
import com.example.monymontymovil.ui.welcome.SplashScreen
import com.example.monymontymovil.ui.welcome.WelcomeScreen
import kotlinx.coroutines.launch

private object Route {
    const val Splash = "splash"
    const val Welcome = "welcome"
    const val Onboarding = "onboarding"
    const val Currency = "currency"
    const val Ready = "ready"
    const val Login = "login"
    const val Register = "register"
    const val Recover = "recover"
    const val Home = "home"
}

// Transiciones suaves entre pantallas: deslizamiento corto + fundido, con la curva "suave" del diseño.
private val enter: AnimatedContentTransitionScope<*>.() -> EnterTransition = {
    slideInHorizontally(tween(450, easing = MmEasing.Soft)) { it / 5 } + fadeIn(tween(350))
}
private val exit: AnimatedContentTransitionScope<*>.() -> ExitTransition = {
    slideOutHorizontally(tween(450, easing = MmEasing.Soft)) { -it / 8 } + fadeOut(tween(250))
}
private val popEnter: AnimatedContentTransitionScope<*>.() -> EnterTransition = {
    slideInHorizontally(tween(450, easing = MmEasing.Soft)) { -it / 8 } + fadeIn(tween(350))
}
private val popExit: AnimatedContentTransitionScope<*>.() -> ExitTransition = {
    slideOutHorizontally(tween(450, easing = MmEasing.Soft)) { it / 5 } + fadeOut(tween(250))
}

@Composable
fun AppNav() {
    val app = LocalContext.current.applicationContext as MonyMontyApp
    val nav = rememberNavController()
    val scope = rememberCoroutineScope()

    // Se pregunta a la API si la cookie guardada sigue vigente mientras se muestra el splash.
    var sessionActive by remember { mutableStateOf<Boolean?>(null) }
    LaunchedEffect(Unit) { sessionActive = app.auth.hasActiveSession() }

    NavHost(
        navController = nav,
        startDestination = Route.Splash,
        enterTransition = enter,
        exitTransition = exit,
        popEnterTransition = popEnter,
        popExitTransition = popExit,
    ) {
        composable(Route.Splash) {
            SplashScreen(ready = sessionActive != null) {
                val next = when {
                    sessionActive == true -> Route.Home
                    app.prefs.onboardingDone -> Route.Login
                    else -> Route.Welcome
                }
                nav.navigate(next) { popUpTo(Route.Splash) { inclusive = true } }
            }
        }
        composable(Route.Welcome) {
            WelcomeScreen(
                onStart = { nav.navigate(Route.Onboarding) },
                onHaveAccount = { nav.goToLogin(app) },
            )
        }
        composable(Route.Onboarding) {
            OnboardingScreen(
                onSkip = { nav.navigate(Route.Currency) },
                onFinish = { nav.navigate(Route.Currency) },
                onBackToWelcome = { nav.popBackStack() },
            )
        }
        composable(Route.Currency) {
            CurrencyScreen(
                initialCode = app.prefs.currencyCode,
                onBack = { nav.popBackStack() },
                onContinue = {
                    app.prefs.currencyCode = it.code
                    nav.navigate(Route.Ready)
                },
            )
        }
        composable(Route.Ready) {
            ReadyScreen(
                currencyCode = app.prefs.currencyCode,
                onLogin = { nav.goToLogin(app) },
                onCreateAccount = {
                    app.prefs.onboardingDone = true
                    nav.navigate(Route.Register)
                },
            )
        }
        composable(Route.Login) {
            val vm: LoginViewModel = viewModel(factory = LoginViewModel.Factory(app.auth))
            LoginScreen(
                viewModel = vm,
                onLoggedIn = { nav.navigate(Route.Home) { popUpTo(0) { inclusive = true } } },
                onForgotPassword = { nav.navigate(Route.Recover) },
                onCreateAccount = { nav.navigate(Route.Register) },
            )
        }
        composable(Route.Register) { ComingSoonScreen("Crear cuenta") { nav.popBackStack() } }
        composable(Route.Recover) { ComingSoonScreen("Recuperar cuenta") { nav.popBackStack() } }
        composable(Route.Home) {
            HomePlaceholderScreen(
                onLogout = {
                    scope.launch {
                        app.auth.logout()
                        nav.navigate(Route.Login) { popUpTo(0) { inclusive = true } }
                    }
                },
            )
        }
    }
}

/** Entrar al login desde cualquier punto de la bienvenida cuenta como onboarding resuelto: no se vuelve a mostrar. */
private fun NavHostController.goToLogin(app: MonyMontyApp) {
    app.prefs.onboardingDone = true
    navigate(Route.Login) { popUpTo(Route.Welcome) { inclusive = true } }
}
