import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import 'theme_controller.dart';

/// Botón que abre un selector para elegir Sistema / Claro / Oscuro.
class ThemeModeButton extends StatelessWidget {
  const ThemeModeButton({super.key});

  static IconData _icon(ThemeMode mode) => switch (mode) {
    ThemeMode.system => Icons.brightness_auto_outlined,
    ThemeMode.light => Icons.light_mode_outlined,
    ThemeMode.dark => Icons.dark_mode_outlined,
  };

  @override
  Widget build(BuildContext context) {
    final mode = context.watch<ThemeController>().mode;

    return IconButton(
      tooltip: 'Cambiar tema',
      icon: Icon(_icon(mode)),
      onPressed: () => showModalBottomSheet<void>(
        context: context,
        showDragHandle: true,
        builder: (sheetContext) => const _ThemeSheet(),
      ),
    );
  }
}

class _ThemeSheet extends StatelessWidget {
  const _ThemeSheet();

  @override
  Widget build(BuildContext context) {
    final controller = context.watch<ThemeController>();

    Widget option(ThemeMode mode, String title, String subtitle) {
      final selected = controller.mode == mode;
      return ListTile(
        leading: Icon(ThemeModeButton._icon(mode)),
        title: Text(title),
        subtitle: Text(subtitle),
        trailing: selected
            ? Icon(
                Icons.check_circle,
                color: Theme.of(context).colorScheme.primary,
              )
            : null,
        onTap: () {
          controller.setMode(mode);
          Navigator.of(context).pop();
        },
      );
    }

    return SafeArea(
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Padding(
            padding: const EdgeInsets.only(bottom: 8),
            child: Text(
              'Apariencia',
              style: Theme.of(context).textTheme.titleMedium,
            ),
          ),
          option(
            ThemeMode.system,
            'Automático',
            'Usar el tema del dispositivo',
          ),
          option(ThemeMode.light, 'Claro', 'Siempre en modo claro'),
          option(ThemeMode.dark, 'Oscuro', 'Siempre en modo oscuro'),
          const SizedBox(height: 8),
        ],
      ),
    );
  }
}
