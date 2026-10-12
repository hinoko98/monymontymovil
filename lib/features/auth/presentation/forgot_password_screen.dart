import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../../core/app_theme.dart';
import '../../../core/widgets/app_fields.dart';
import '../state/auth_controller.dart';
import 'email_sent_screen.dart';

/// Pide el correo para enviar el enlace de recuperación de contraseña.
class ForgotPasswordScreen extends StatefulWidget {
  const ForgotPasswordScreen({super.key, this.initialEmail = ''});

  /// Correo ya escrito en el login, para no pedirlo de nuevo.
  final String initialEmail;

  @override
  State<ForgotPasswordScreen> createState() => _ForgotPasswordScreenState();
}

class _ForgotPasswordScreenState extends State<ForgotPasswordScreen> {
  final _formKey = GlobalKey<FormState>();
  late final _emailController = TextEditingController(
    text: widget.initialEmail,
  );

  @override
  void dispose() {
    _emailController.dispose();
    super.dispose();
  }

  String? _validateEmail(String? value) {
    final email = value?.trim() ?? '';
    if (email.isEmpty) return 'Por favor, ingresa tu correo electrónico.';
    final emailRegex = RegExp(r'^[^@\s]+@[^@\s]+\.[^@\s]+$');
    if (!emailRegex.hasMatch(email)) return 'Correo electrónico no válido.';
    return null;
  }

  Future<void> _onSubmit() async {
    if (!(_formKey.currentState?.validate() ?? false)) return;
    FocusScope.of(context).unfocus();

    final email = _emailController.text.trim();
    final auth = context.read<AuthController>();
    final success = await auth.recuperarCuenta(email);

    if (!mounted) return;

    if (success) {
      Navigator.of(context).pushReplacement(
        MaterialPageRoute(builder: (_) => EmailSentScreen(email: email)),
      );
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text(auth.errorMessage ?? 'Error al enviar correo')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    final auth = context.watch<AuthController>();
    final colors = AppColors.of(context);
    final enabled = !auth.loading;

    return Scaffold(
      body: SafeArea(
        child: SingleChildScrollView(
          keyboardDismissBehavior: ScrollViewKeyboardDismissBehavior.onDrag,
          padding: const EdgeInsets.fromLTRB(24, 12, 24, 24),
          child: ConstrainedBox(
            constraints: const BoxConstraints(maxWidth: 420),
            child: Form(
              key: _formKey,
              autovalidateMode: AutovalidateMode.onUserInteraction,
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  Align(
                    alignment: Alignment.centerLeft,
                    child: BackSquareButton(
                      onPressed: enabled
                          ? () => Navigator.of(context).maybePop()
                          : null,
                    ),
                  ),
                  const SizedBox(height: 28),
                  Container(
                    width: 64,
                    height: 64,
                    decoration: BoxDecoration(
                      color: colors.softGreen,
                      borderRadius: BorderRadius.circular(18),
                    ),
                    child: Icon(
                      Icons.lock_outline_rounded,
                      size: 30,
                      color: colors.primary,
                    ),
                  ),
                  const SizedBox(height: 24),
                  Text(
                    '¿Olvidaste tu contraseña?',
                    style: TextStyle(
                      fontSize: 24,
                      fontWeight: FontWeight.w800,
                      color: colors.title,
                    ),
                  ),
                  const SizedBox(height: 8),
                  Text(
                    'Ingresa tu correo electrónico y te enviaremos las instrucciones para restablecerla.',
                    style: TextStyle(
                      fontSize: 15,
                      height: 1.5,
                      color: colors.muted,
                    ),
                  ),
                  const SizedBox(height: 28),
                  AppTextField(
                    label: 'Correo electrónico',
                    hint: 'tucorreo@ejemplo.com',
                    controller: _emailController,
                    keyboardType: TextInputType.emailAddress,
                    textInputAction: TextInputAction.done,
                    autofillHints: const [AutofillHints.email],
                    enabled: enabled,
                    validator: _validateEmail,
                    onFieldSubmitted: (_) => _onSubmit(),
                  ),
                  const SizedBox(height: 20),
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
                        : const Text('Enviar instrucciones'),
                  ),
                  const SizedBox(height: 32),
                  Center(
                    child: TextButton(
                      onPressed: enabled
                          ? () => Navigator.of(context).maybePop()
                          : null,
                      child: const Text('Volver a iniciar sesión'),
                    ),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
