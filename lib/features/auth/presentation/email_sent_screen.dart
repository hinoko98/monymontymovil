import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../../core/app_theme.dart';
import '../state/auth_controller.dart';

/// Confirmación de que se envió el correo de recuperación.
class EmailSentScreen extends StatelessWidget {
  const EmailSentScreen({super.key, required this.email});

  final String email;

  Future<void> _resend(BuildContext context) async {
    final auth = context.read<AuthController>();
    final messenger = ScaffoldMessenger.of(context);
    final success = await auth.recuperarCuenta(email);

    messenger.showSnackBar(
      SnackBar(
        content: Text(
          success
              ? 'Te reenviamos el correo de recuperación.'
              : auth.errorMessage ?? 'Error al enviar correo',
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final auth = context.watch<AuthController>();
    final colors = AppColors.of(context);
    final enabled = !auth.loading;

    return Scaffold(
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.fromLTRB(24, 24, 24, 24),
          child: Center(
            child: ConstrainedBox(
              constraints: const BoxConstraints(maxWidth: 420),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  const Spacer(),
                  Center(
                    child: Container(
                      width: 190,
                      height: 190,
                      decoration: BoxDecoration(
                        color: colors.softGreen,
                        shape: BoxShape.circle,
                      ),
                      child: Center(
                        child: Container(
                          width: 130,
                          height: 130,
                          decoration: BoxDecoration(
                            color: colors.primary,
                            shape: BoxShape.circle,
                          ),
                          child: const Icon(
                            Icons.check_rounded,
                            size: 68,
                            color: Colors.white,
                          ),
                        ),
                      ),
                    ),
                  ),
                  const SizedBox(height: 36),
                  Text(
                    '¡Revisa tu correo!',
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      fontSize: 26,
                      fontWeight: FontWeight.w800,
                      color: colors.title,
                    ),
                  ),
                  const SizedBox(height: 12),
                  Text(
                    'Enviamos un enlace de recuperación a tu correo electrónico. Sigue las instrucciones para crear una nueva contraseña.',
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      fontSize: 15,
                      height: 1.55,
                      color: colors.muted,
                    ),
                  ),
                  const Spacer(),
                  FilledButton(
                    onPressed: enabled
                        ? () => Navigator.of(context).maybePop()
                        : null,
                    child: const Text('Volver a iniciar sesión'),
                  ),
                  const SizedBox(height: 8),
                  Center(
                    child: GestureDetector(
                      onTap: enabled ? () => _resend(context) : null,
                      child: Text.rich(
                        TextSpan(
                          style: TextStyle(fontSize: 14, color: colors.muted),
                          children: [
                            const TextSpan(text: '¿No recibiste el correo? '),
                            TextSpan(
                              text: auth.loading ? 'Enviando…' : 'Reenviar',
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
                  const SizedBox(height: 8),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
