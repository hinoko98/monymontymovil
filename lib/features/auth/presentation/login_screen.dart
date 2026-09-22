import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../../core/app_theme.dart';
import '../../../core/theme_mode_button.dart';
import '../../../core/widgets/app_fields.dart';
import '../../../core/widgets/brand_lockup.dart';
import '../state/auth_controller.dart';
import 'forgot_password_screen.dart';
import 'register_screen.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final _formKey = GlobalKey<FormState>();
  final _emailController = TextEditingController();
  final _passwordController = TextEditingController();
  bool _obscurePassword = true;

  @override
  void dispose() {
    _emailController.dispose();
    _passwordController.dispose();
    super.dispose();
  }

  String? _validateEmail(String? value) {
    final email = value?.trim() ?? '';
    if (email.isEmpty) return 'Por favor, ingresa tu correo electrónico.';
    final emailRegex = RegExp(r'^[^@\s]+@[^@\s]+\.[^@\s]+$');
    if (!emailRegex.hasMatch(email)) return 'Correo electrónico no válido.';
    return null;
  }

  String? _validatePassword(String? value) {
    if (value == null || value.length < 8) return 'Credenciales inválidas';
    return null;
  }

  Future<void> _onSubmit() async {
    if (!(_formKey.currentState?.validate() ?? false)) return;
    FocusScope.of(context).unfocus();

    final auth = context.read<AuthController>();
    final success = await auth.login(
      email: _emailController.text.trim(),
      password: _passwordController.text,
    );

    if (!mounted) return;

    if (!success && auth.errorMessage != null) {
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text(auth.errorMessage!)));
    }
  }

  Future<void> _openRegister() async {
    final created = await Navigator.of(
      context,
    ).push<bool>(MaterialPageRoute(builder: (_) => const RegisterScreen()));

    if (created == true && mounted) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Cuenta creada. Ya puedes iniciar sesión.'),
        ),
      );
    }
  }

  void _openForgotPassword() => Navigator.of(context).push(
    MaterialPageRoute(
      builder: (_) =>
          ForgotPasswordScreen(initialEmail: _emailController.text.trim()),
    ),
  );

  @override
  Widget build(BuildContext context) {
    final auth = context.watch<AuthController>();
    final colors = AppColors.of(context);
    final enabled = !auth.loading;

    return Scaffold(
      body: SafeArea(
        child: Stack(
          children: [
            const Positioned(top: 0, right: 8, child: ThemeModeButton()),
            Center(
              child: SingleChildScrollView(
                keyboardDismissBehavior:
                    ScrollViewKeyboardDismissBehavior.onDrag,
                padding: const EdgeInsets.fromLTRB(24, 32, 24, 24),
                child: ConstrainedBox(
                  constraints: const BoxConstraints(maxWidth: 420),
                  child: Form(
                    key: _formKey,
                    autovalidateMode: AutovalidateMode.onUserInteraction,
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.stretch,
                      children: [
                        const Center(
                          child: BrandLockup(
                            axis: Axis.vertical,
                            markSize: 64,
                            fontSize: 22,
                          ),
                        ),
                        const SizedBox(height: 36),
                        Text(
                          'Bienvenido de nuevo',
                          style: TextStyle(
                            fontSize: 26,
                            fontWeight: FontWeight.w800,
                            color: colors.title,
                          ),
                        ),
                        const SizedBox(height: 6),
                        Text(
                          'Inicia sesión para continuar gestionando tus finanzas.',
                          style: TextStyle(
                            fontSize: 15,
                            height: 1.4,
                            color: colors.muted,
                          ),
                        ),
                        const SizedBox(height: 24),
                        AppTextField(
                          label: 'Correo electrónico',
                          hint: 'tucorreo@ejemplo.com',
                          controller: _emailController,
                          keyboardType: TextInputType.emailAddress,
                          textInputAction: TextInputAction.next,
                          autofillHints: const [AutofillHints.email],
                          enabled: enabled,
                          validator: _validateEmail,
                        ),
                        const SizedBox(height: 16),
                        AppTextField(
                          label: 'Contraseña',
                          hint: '••••••••',
                          controller: _passwordController,
                          obscureText: _obscurePassword,
                          textInputAction: TextInputAction.done,
                          autofillHints: const [AutofillHints.password],
                          enabled: enabled,
                          validator: _validatePassword,
                          onFieldSubmitted: (_) => _onSubmit(),
                          suffixIcon: IconButton(
                            tooltip: _obscurePassword
                                ? 'Mostrar contraseña'
                                : 'Ocultar contraseña',
                            icon: Icon(
                              _obscurePassword
                                  ? Icons.visibility_outlined
                                  : Icons.visibility_off_outlined,
                            ),
                            onPressed: () => setState(
                              () => _obscurePassword = !_obscurePassword,
                            ),
                          ),
                        ),
                        Align(
                          alignment: Alignment.centerRight,
                          child: TextButton(
                            onPressed: enabled ? _openForgotPassword : null,
                            child: const Text('¿Olvidaste tu contraseña?'),
                          ),
                        ),
                        const SizedBox(height: 8),
                        FilledButton(
                          onPressed: enabled ? _onSubmit : null,
                          child: auth.loading
                              ? const SizedBox(
                                  height: 22,
                                  width: 22,
                                  child: CircularProgressIndicator(
                                    strokeWidth: 2.4,
                                    color: Colors.white,
                                  ),
                                )
                              : const Text('Iniciar Sesión'),
                        ),
                        const SizedBox(height: 12),
                        FilledButton(
                          onPressed: enabled ? _openRegister : null,
                          style: FilledButton.styleFrom(
                            backgroundColor: colors.secondary,
                          ),
                          child: const Text('Crear Cuenta'),
                        ),
                        const SizedBox(height: 32),
                        Center(
                          child: GestureDetector(
                            onTap: enabled ? _openRegister : null,
                            child: Text.rich(
                              TextSpan(
                                style: TextStyle(
                                  fontSize: 14,
                                  color: colors.muted,
                                ),
                                children: [
                                  const TextSpan(text: '¿No tienes cuenta? '),
                                  TextSpan(
                                    text: 'Regístrate',
                                    style: TextStyle(
                                      color: colors.primary,
                                      fontWeight: FontWeight.w700,
                                    ),
                                  ),
                                ],
                              ),
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
