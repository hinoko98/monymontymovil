import 'package:flutter/material.dart';

/// Paleta de la app. Hay una versión para tema claro y otra para oscuro;
/// usa `AppColors.of(context)` para obtener la que corresponde.
@immutable
class AppColors {
  const AppColors._({
    required this.brand,
    required this.primary,
    required this.secondary,
    required this.amber,
    required this.blue,
    required this.softGreen,
    required this.softAmber,
    required this.softBlue,
    required this.background,
    required this.field,
    required this.border,
    required this.chip,
    required this.title,
    required this.muted,
  });

  /// Naranja del logo.
  final Color brand;

  /// Verde de los botones principales y enlaces.
  final Color primary;

  /// Verde claro del botón secundario ("Crear Cuenta").
  final Color secondary;

  final Color amber;
  final Color blue;

  /// Fondos suaves de las tarjetas del onboarding.
  final Color softGreen;
  final Color softAmber;
  final Color softBlue;

  final Color background;

  /// Relleno de los campos de texto.
  final Color field;
  final Color border;

  /// Fondo de los botones cuadrados (flecha de volver).
  final Color chip;

  final Color title;
  final Color muted;

  static const light = AppColors._(
    brand: Color(0xFFEE5D3C),
    primary: Color(0xFF1A7A52),
    secondary: Color(0xFF4CBE86),
    amber: Color(0xFFDF9A2C),
    blue: Color(0xFF2E7FD4),
    softGreen: Color(0xFFE9F5EE),
    softAmber: Color(0xFFFBEECB),
    softBlue: Color(0xFFE8F1FC),
    background: Color(0xFFFFFFFF),
    field: Color(0xFFF5F6F8),
    border: Color(0xFFECEEF1),
    chip: Color(0xFFF4F5F7),
    title: Color(0xFF1C1F23),
    muted: Color(0xFF6B7280),
  );

  static const dark = AppColors._(
    brand: Color(0xFFF2714F),
    primary: Color(0xFF22A06B),
    secondary: Color(0xFF3FAF7E),
    amber: Color(0xFFE5A83E),
    blue: Color(0xFF4A93E0),
    softGreen: Color(0xFF15281F),
    softAmber: Color(0xFF2B2216),
    softBlue: Color(0xFF16212F),
    background: Color(0xFF0F1216),
    field: Color(0xFF1A1F26),
    border: Color(0xFF2A313B),
    chip: Color(0xFF1E242C),
    title: Color(0xFFF2F4F7),
    muted: Color(0xFF9AA4B2),
  );

  static AppColors of(BuildContext context) =>
      Theme.of(context).brightness == Brightness.dark ? dark : light;
}

class AppTheme {
  static ThemeData light() => _build(Brightness.light, AppColors.light);

  static ThemeData dark() => _build(Brightness.dark, AppColors.dark);

  static ThemeData _build(Brightness brightness, AppColors colors) {
    final scheme =
        ColorScheme.fromSeed(
          seedColor: colors.primary,
          brightness: brightness,
        ).copyWith(
          primary: colors.primary,
          surface: colors.background,
          error: brightness == Brightness.dark
              ? const Color(0xFFFF6B6B)
              : const Color(0xFFDC2626),
        );

    OutlineInputBorder outline(Color color, [double width = 1]) =>
        OutlineInputBorder(
          borderRadius: BorderRadius.circular(14),
          borderSide: BorderSide(color: color, width: width),
        );

    return ThemeData(
      useMaterial3: true,
      brightness: brightness,
      colorScheme: scheme,
      scaffoldBackgroundColor: colors.background,
      appBarTheme: AppBarTheme(
        backgroundColor: colors.background,
        surfaceTintColor: Colors.transparent,
        elevation: 0,
      ),
      inputDecorationTheme: InputDecorationTheme(
        filled: true,
        fillColor: colors.field,
        contentPadding: const EdgeInsets.symmetric(
          horizontal: 16,
          vertical: 18,
        ),
        hintStyle: TextStyle(color: colors.muted, fontSize: 15),
        prefixIconColor: colors.muted,
        suffixIconColor: colors.muted,
        border: outline(colors.border),
        enabledBorder: outline(colors.border),
        focusedBorder: outline(colors.primary, 1.6),
        errorBorder: outline(scheme.error),
        focusedErrorBorder: outline(scheme.error, 1.6),
      ),
      filledButtonTheme: FilledButtonThemeData(
        style: FilledButton.styleFrom(
          backgroundColor: colors.primary,
          foregroundColor: Colors.white,
          minimumSize: const Size.fromHeight(56),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(14),
          ),
          textStyle: const TextStyle(fontSize: 16, fontWeight: FontWeight.w700),
        ),
      ),
      textButtonTheme: TextButtonThemeData(
        style: TextButton.styleFrom(
          foregroundColor: colors.primary,
          textStyle: const TextStyle(fontSize: 14, fontWeight: FontWeight.w600),
        ),
      ),
      checkboxTheme: CheckboxThemeData(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(5)),
        side: BorderSide(color: colors.muted, width: 1.5),
      ),
      snackBarTheme: const SnackBarThemeData(
        behavior: SnackBarBehavior.floating,
      ),
    );
  }
}
