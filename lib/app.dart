import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import 'core/app_theme.dart';
import 'features/auth/presentation/login_screen.dart';
import 'features/auth/state/auth_controller.dart';
import 'features/home/presentation/home_screen.dart';

class MonyMontyApp extends StatelessWidget {
  const MonyMontyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'MonyMonty',
      debugShowCheckedModeBanner: false,
      theme: AppTheme.dark(),
      themeMode: ThemeMode.dark,
      home: const _AuthGate(),
    );
  }
}

/// Decide qué pantalla mostrar según el estado de autenticación:
/// splash mientras se verifica la sesión guardada, login o home.
class _AuthGate extends StatelessWidget {
  const _AuthGate();

  @override
  Widget build(BuildContext context) {
    final auth = context.watch<AuthController>();

    switch (auth.status) {
      case AuthStatus.unknown:
        return const Scaffold(body: Center(child: CircularProgressIndicator()));
      case AuthStatus.authenticated:
        return const HomeScreen();
      case AuthStatus.unauthenticated:
        return const LoginScreen();
    }
  }
}
