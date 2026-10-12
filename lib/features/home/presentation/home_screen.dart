import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../../core/theme_mode_button.dart';
import '../../auth/state/auth_controller.dart';

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final auth = context.watch<AuthController>();
    final usuario = auth.usuario;

    return Scaffold(
      appBar: AppBar(
        title: const Text('MonyMonty'),
        actions: [
          const ThemeModeButton(),
          IconButton(
            tooltip: 'Cerrar sesión',
            icon: const Icon(Icons.logout),
            onPressed: auth.loading
                ? null
                : () => context.read<AuthController>().logout(),
          ),
        ],
      ),
      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(24),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              CircleAvatar(
                radius: 40,
                child: Text(
                  (usuario?.nombre.isNotEmpty == true
                          ? usuario!.nombre[0]
                          : '?')
                      .toUpperCase(),
                  style: const TextStyle(fontSize: 32),
                ),
              ),
              const SizedBox(height: 16),
              Text(
                usuario?.nombre.isNotEmpty == true
                    ? '¡Hola, ${usuario!.nombre}!'
                    : '¡Sesión iniciada!',
                style: Theme.of(context).textTheme.titleLarge,
                textAlign: TextAlign.center,
              ),
              if (usuario?.email.isNotEmpty == true) ...[
                const SizedBox(height: 4),
                Text(
                  usuario!.email,
                  style: Theme.of(context).textTheme.bodyMedium,
                ),
              ],
            ],
          ),
        ),
      ),
    );
  }
}
