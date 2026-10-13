package com.example.monymontymovil.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.monymontymovil.ui.components.MonyLogo
import com.example.monymontymovil.ui.components.PrimaryButton
import com.example.monymontymovil.ui.components.SecondaryButton
import com.example.monymontymovil.ui.theme.ActionOrange
import com.example.monymontymovil.ui.theme.BrandOrange
import com.example.monymontymovil.ui.theme.Charcoal
import com.example.monymontymovil.ui.theme.Cream
import com.example.monymontymovil.ui.theme.ErrorBg
import com.example.monymontymovil.ui.theme.LocalReduceMotion
import com.example.monymontymovil.ui.theme.Peach
import com.example.monymontymovil.ui.theme.SandLine
import com.example.monymontymovil.ui.theme.TextMuted
import com.example.monymontymovil.ui.theme.reveal
import kotlin.math.roundToInt

/** 07 · Iniciar sesión (conectado a POST /auth/login). El formulario se sacude si algo falla. */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoggedIn: () -> Unit,
    onForgotPassword: () -> Unit,
    onCreateAccount: () -> Unit,
) {
    val focus = LocalFocusManager.current
    val reduce = LocalReduceMotion.current
    val shake = remember { Animatable(0f) }

    // Sacudida de 600 ms al fallar; con "reducir movimiento" el aviso de error basta.
    LaunchedEffect(viewModel.errorTick) {
        if (viewModel.errorTick > 0 && !reduce) {
            shake.snapTo(0f)
            shake.animateTo(0f, keyframes {
                durationMillis = 600
                0f at 0
                -12f at 100
                10f at 200
                -8f at 300
                5f at 400
                -2f at 500
                0f at 600
            })
        }
    }

    Box(Modifier.fillMaxSize().background(Cream)) {
        // Mancha cálida de la esquina, como en el diseño
        Box(
            Modifier
                .align(Alignment.TopEnd)
                .offset(x = 70.dp, y = (-50).dp)
                .size(220.dp)
                .clip(CircleShape)
                .background(Peach.copy(alpha = 0.55f)),
        )
        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
        ) {
            Spacer(Modifier.height(36.dp))
            MonyLogo(color = BrandOrange, fontSize = 26.sp, modifier = Modifier.reveal(0))
            Spacer(Modifier.height(28.dp))
            Text("Hola de nuevo", style = MaterialTheme.typography.headlineMedium, color = Charcoal, modifier = Modifier.reveal(1))
            Spacer(Modifier.height(6.dp))
            Text(
                "Ingresa para ver cómo van tus finanzas.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted,
                modifier = Modifier.reveal(2),
            )
            Spacer(Modifier.height(24.dp))

            Column(Modifier.offset { IntOffset(shake.value.dp.toPx().roundToInt(), 0) }) {
                AnimatedVisibility(viewModel.formError != null) {
                    ErrorBanner(viewModel.formError.orEmpty(), Modifier.padding(bottom = 16.dp))
                }

                Column(Modifier.reveal(3)) {
                    FieldLabel("Correo electrónico")
                    MmTextField(
                        value = viewModel.email,
                        onValueChange = viewModel::onEmail,
                        placeholder = "nombre@correo.com",
                        error = viewModel.emailError,
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                        onNext = { focus.moveFocus(FocusDirection.Down) },
                    )
                    Spacer(Modifier.height(16.dp))
                    FieldLabel("Contraseña")
                    MmTextField(
                        value = viewModel.password,
                        onValueChange = viewModel::onPassword,
                        placeholder = "Tu contraseña",
                        error = viewModel.passwordError,
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                        visualTransformation = if (viewModel.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        onDone = {
                            focus.clearFocus()
                            viewModel.submit(onLoggedIn)
                        },
                        trailing = {
                            IconButton(onClick = viewModel::togglePassword) {
                                Icon(
                                    if (viewModel.passwordVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                                    contentDescription = if (viewModel.passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                    tint = TextMuted,
                                )
                            }
                        },
                    )
                }
            }

            Text(
                "¿Olvidaste tu contraseña?",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
                color = ActionOrange,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .reveal(4)
                    .clickable(role = Role.Button, onClick = onForgotPassword)
                    .padding(vertical = 10.dp),
            )
            Spacer(Modifier.height(8.dp))
            PrimaryButton(
                text = if (viewModel.formError != null) "Intentar de nuevo" else "Ingresar",
                onClick = {
                    focus.clearFocus()
                    viewModel.submit(onLoggedIn)
                },
                loading = viewModel.loading,
                modifier = Modifier.reveal(5),
            )
            Spacer(Modifier.height(20.dp))
            Row(Modifier.fillMaxWidth().reveal(6), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                HorizontalDivider(Modifier.weight(1f), color = SandLine)
                Text("¿Nuevo en MonyMonty?", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                HorizontalDivider(Modifier.weight(1f), color = SandLine)
            }
            Spacer(Modifier.height(16.dp))
            SecondaryButton("Crear una cuenta", onCreateAccount, Modifier.reveal(7))
            Spacer(Modifier.height(28.dp))
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(text, style = MaterialTheme.typography.labelMedium, color = Charcoal, modifier = Modifier.padding(bottom = 6.dp))
}

@Composable
private fun ErrorBanner(message: String, modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(ErrorBg)
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Icon(Icons.Rounded.ErrorOutline, contentDescription = null, tint = ActionOrange, modifier = Modifier.size(20.dp))
        Text(message, style = MaterialTheme.typography.bodySmall, color = Charcoal)
    }
}

@Composable
private fun MmTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    error: String?,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailing: (@Composable () -> Unit)? = null,
    onNext: () -> Unit = {},
    onDone: () -> Unit = {},
) {
    Column(modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = error != null,
            placeholder = { Text(placeholder, color = TextMuted.copy(alpha = 0.7f)) },
            visualTransformation = visualTransformation,
            trailingIcon = trailing,
            shape = RoundedCornerShape(16.dp),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = Charcoal),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            keyboardActions = KeyboardActions(onNext = { onNext() }, onDone = { onDone() }),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                errorContainerColor = Color.White,
                focusedBorderColor = BrandOrange,
                unfocusedBorderColor = SandLine,
                errorBorderColor = ActionOrange,
                cursorColor = ActionOrange,
            ),
        )
        // El error se dice con texto (no solo con el borde de color) y explica qué hacer.
        if (error != null) {
            Text(error, style = MaterialTheme.typography.bodySmall, color = ActionOrange, modifier = Modifier.padding(start = 4.dp, top = 6.dp))
        }
    }
}
