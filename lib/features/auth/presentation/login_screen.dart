import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../../core/app_theme.dart';
import '../state/auth_controller.dart';

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
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text(auth.errorMessage!)),
      );
    }
  }

  void _comingSoon(String feature) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text('$feature estará disponible próximamente.')),
    );
  }

  @override
  Widget build(BuildContext context) {
    final auth = context.watch<AuthController>();
    final textTheme = Theme.of(context).textTheme;

    return Scaffold(
      body: DecoratedBox(
        decoration: const BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
            colors: [Color(0xFF1C1712), AppColors.background],
            stops: [0, 0.45],
          ),
        ),
        child: SafeArea(
          child: Center(
            child: SingleChildScrollView(
              keyboardDismissBehavior: ScrollViewKeyboardDismissBehavior.onDrag,
              padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 32),
              child: ConstrainedBox(
                constraints: const BoxConstraints(maxWidth: 420),
                child: Form(
                  key: _formKey,
                  autovalidateMode: AutovalidateMode.onUserInteraction,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    children: [
                      Center(
                        child: Image.asset('assets/images/logo.png', width: 180),
                      ),
                      const SizedBox(height: 20),
                      Text(
                        'Tu camino hacia la libertad financiera comienza aquí.',
                        textAlign: TextAlign.center,
                        style: textTheme.bodyMedium?.copyWith(
                          color: AppColors.accent,
                          fontStyle: FontStyle.italic,
                          height: 1.4,
                        ),
                      ),
                      const SizedBox(height: 32),
                      Text(
                        'Bienvenido de nuevo',
                        style: textTheme.headlineSmall?.copyWith(fontWeight: FontWeight.w800),
                      ),
                      const SizedBox(height: 4),
                      Text(
                        'Inicia sesión para gestionar tus finanzas.',
                        style: textTheme.bodyMedium?.copyWith(color: AppColors.textMuted),
                      ),
                      const SizedBox(height: 24),
                      TextFormField(
                        controller: _emailController,
                        keyboardType: TextInputType.emailAddress,
                        textInputAction: TextInputAction.next,
                        autofillHints: const [AutofillHints.email],
                        enabled: !auth.loading,
                        decoration: const InputDecoration(
                          hintText: 'Correo electrónico',
                          prefixIcon: Icon(Icons.mail_outline_rounded),
                        ),
                        validator: _validateEmail,
                      ),
                      const SizedBox(height: 14),
                      TextFormField(
                        controller: _passwordController,
                        obscureText: _obscurePassword,
                        textInputAction: TextInputAction.done,
                        autofillHints: const [AutofillHints.password],
                        enabled: !auth.loading,
                        decoration: InputDecoration(
                          hintText: 'Contraseña',
                          prefixIcon: const Icon(Icons.lock_outline_rounded),
                          suffixIcon: IconButton(
                            tooltip: _obscurePassword ? 'Mostrar contraseña' : 'Ocultar contraseña',
                            icon: Icon(
                              _obscurePassword ? Icons.visibility_outlined : Icons.visibility_off_outlined,
                            ),
                            onPressed: () => setState(() => _obscurePassword = !_obscurePassword),
                          ),
                        ),
                        validator: _validatePassword,
                        onFieldSubmitted: (_) => _onSubmit(),
                      ),
                      Align(
                        alignment: Alignment.centerRight,
                        child: TextButton(
                          onPressed: auth.loading ? null : () => _comingSoon('Recuperar contraseña'),
                          style: TextButton.styleFrom(foregroundColor: AppColors.primary),
                          child: const Text('¿Olvidaste tu contraseña?'),
                        ),
                      ),
                      const SizedBox(height: 8),
                      FilledButton(
                        onPressed: auth.loading ? null : _onSubmit,
                        style: FilledButton.styleFrom(backgroundColor: AppColors.primary),
                        child: auth.loading
                            ? const SizedBox(
                                height: 22,
                                width: 22,
                                child: CircularProgressIndicator(strokeWidth: 2.4, color: Colors.white),
                              )
                            : const Text('Iniciar sesión'),
                      ),
                      const SizedBox(height: 12),
                      OutlinedButton(
                        onPressed: auth.loading ? null : () => _comingSoon('Crear cuenta'),
                        child: const Text('Crear cuenta'),
                      ),
                      const SizedBox(height: 32),
                      const _FeatureRow(
                        icon: Icons.person_search_outlined,
                        title: 'Asesorías personalizadas',
                        subtitle: 'Orientación adaptada a tus necesidades.',
                      ),
                      const _FeatureRow(
                        icon: Icons.groups_2_outlined,
                        title: 'Asesorías grupales',
                        subtitle: 'Aprende y comparte en sesiones interactivas.',
                      ),
                      const _FeatureRow(
                        icon: Icons.trending_up_rounded,
                        title: 'Gestión moderna',
                        subtitle: 'Teorías económicas para ahorrar e invertir.',
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}

class _FeatureRow extends StatelessWidget {
  const _FeatureRow({required this.icon, required this.title, required this.subtitle});

  final IconData icon;
  final String title;
  final String subtitle;

  @override
  Widget build(BuildContext context) {
    final textTheme = Theme.of(context).textTheme;

    return Padding(
      padding: const EdgeInsets.only(bottom: 14),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Container(
            padding: const EdgeInsets.all(10),
            decoration: BoxDecoration(
              color: AppColors.accent.withValues(alpha: 0.12),
              borderRadius: BorderRadius.circular(12),
            ),
            child: Icon(icon, size: 20, color: AppColors.accent),
          ),
          const SizedBox(width: 14),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(title, style: textTheme.titleSmall?.copyWith(fontWeight: FontWeight.w700)),
                const SizedBox(height: 2),
                Text(subtitle, style: textTheme.bodySmall?.copyWith(color: AppColors.textMuted)),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
