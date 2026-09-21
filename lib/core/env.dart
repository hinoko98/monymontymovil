import 'dart:io' show Platform;

/// Configuración del entorno de la app.
///
/// La API usa autenticación por cookie de sesión (express-session), no por
/// Bearer token, así que el cliente HTTP debe enviar y persistir cookies
/// igual que lo hace el frontend web (axios con `withCredentials: true`).
class Env {
  Env._();

  /// URL base de la API.
  ///
  /// Se puede sobreescribir en tiempo de compilación/ejecución con:
  /// `flutter run --dart-define=API_BASE_URL=http://192.168.1.10:3000/`
  ///
  /// Por defecto, en desarrollo:
  /// - Emulador Android: `localhost` de la máquina host se alcanza en `10.0.2.2`.
  /// - iOS Simulator: `localhost` funciona directo.
  /// - Dispositivo físico: usa `--dart-define=API_BASE_URL=...` con la IP de tu
  ///   PC en la red local, y corre la API con `EXPRESS_HOST=0.0.0.0`.
  static String get apiBaseUrl {
    const override = String.fromEnvironment('API_BASE_URL');
    if (override.isNotEmpty) return override;

    if (Platform.isAndroid) {
      return 'http://10.0.2.2:3000/';
    }
    return 'http://localhost:3000/';
  }
}
