import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

/// Preferencia de tema del usuario: claro (por defecto), oscuro o el del
/// sistema. Se guarda en el dispositivo para recordarla entre sesiones.
class ThemeController extends ChangeNotifier {
  ThemeController._(this._prefs, this._mode);

  static const _key = 'theme_mode';

  final SharedPreferences _prefs;
  ThemeMode _mode;

  ThemeMode get mode => _mode;

  static Future<ThemeController> load() async {
    final prefs = await SharedPreferences.getInstance();
    final mode =
        ThemeMode.values.asNameMap()[prefs.getString(_key)] ?? ThemeMode.light;
    return ThemeController._(prefs, mode);
  }

  Future<void> setMode(ThemeMode mode) async {
    if (mode == _mode) return;
    _mode = mode;
    notifyListeners();
    await _prefs.setString(_key, mode.name);
  }
}
