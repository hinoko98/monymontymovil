import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';

import 'package:monymontymovil/app.dart';
import 'package:monymontymovil/core/api_client.dart';
import 'package:monymontymovil/core/theme_controller.dart';
import 'package:monymontymovil/features/auth/data/auth_repository.dart';
import 'package:monymontymovil/features/auth/presentation/email_sent_screen.dart';
import 'package:monymontymovil/features/auth/presentation/forgot_password_screen.dart';
import 'package:monymontymovil/features/auth/presentation/register_screen.dart';
import 'package:monymontymovil/features/auth/state/auth_controller.dart';
import 'package:monymontymovil/features/onboarding/state/onboarding_controller.dart';

/// Repositorio de pruebas: evita cualquier llamada de red real durante el
/// widget test (`checkAuth` normalmente pega contra la API por HTTP).
class _NoNetworkAuthRepository extends AuthRepository {
  _NoNetworkAuthRepository(super.apiClient);

  @override
  Future<bool> checkAuth() async => false;
}

void main() {
  /// Tamaño de un celular, para detectar desbordes de layout reales.
  void usePhoneScreen(WidgetTester tester) {
    tester.view.physicalSize = const Size(390 * 3, 844 * 3);
    tester.view.devicePixelRatio = 3;
    addTearDown(tester.view.resetPhysicalSize);
    addTearDown(tester.view.resetDevicePixelRatio);
  }

  Future<AuthController> pumpApp(
    WidgetTester tester, {
    bool onboardingSeen = true,
  }) async {
    SharedPreferences.setMockInitialValues({'onboarding_seen': onboardingSeen});
    usePhoneScreen(tester);

    final auth = AuthController(_NoNetworkAuthRepository(ApiClient.inMemory()));
    final theme = await ThemeController.load();
    final onboarding = await OnboardingController.load();

    await tester.pumpWidget(
      MultiProvider(
        providers: [
          ChangeNotifierProvider.value(value: auth),
          ChangeNotifierProvider.value(value: theme),
          ChangeNotifierProvider.value(value: onboarding),
        ],
        child: const MonyMontyApp(),
      ),
    );

    await auth.bootstrap();
    await tester.pumpAndSettle();
    return auth;
  }

  /// Muestra una pantalla suelta, para probarla sin navegar hasta ella.
  Future<void> pumpScreen(
    WidgetTester tester,
    Widget screen, {
    Brightness brightness = Brightness.light,
  }) async {
    SharedPreferences.setMockInitialValues({});
    usePhoneScreen(tester);

    final auth = AuthController(_NoNetworkAuthRepository(ApiClient.inMemory()));

    await tester.pumpWidget(
      MultiProvider(
        providers: [
          ChangeNotifierProvider.value(value: auth),
          ChangeNotifierProvider.value(value: await ThemeController.load()),
        ],
        child: MaterialApp(
          theme: ThemeData(brightness: brightness),
          home: screen,
        ),
      ),
    );
    await tester.pumpAndSettle();
  }

  testWidgets('El onboarding recorre las 3 pantallas y termina en el login', (
    tester,
  ) async {
    await pumpApp(tester, onboardingSeen: false);

    expect(
      find.textContaining('bajo control', findRichText: true),
      findsOneWidget,
    );
    expect(find.text('Omitir'), findsOneWidget);

    await tester.tap(find.text('Siguiente'));
    await tester.pumpAndSettle();
    expect(
      find.textContaining('a tu medida', findRichText: true),
      findsOneWidget,
    );

    await tester.tap(find.text('Siguiente'));
    await tester.pumpAndSettle();
    expect(
      find.textContaining('inteligencia', findRichText: true),
      findsOneWidget,
    );
    // En la última pantalla ya no se ofrece omitir.
    expect(find.text('Omitir'), findsNothing);

    await tester.tap(find.text('Comenzar'));
    await tester.pumpAndSettle();
    expect(find.text('Bienvenido de nuevo'), findsOneWidget);
  });

  testWidgets('"Omitir" salta directo al login', (tester) async {
    await pumpApp(tester, onboardingSeen: false);

    await tester.tap(find.text('Omitir'));
    await tester.pumpAndSettle();

    expect(find.text('Bienvenido de nuevo'), findsOneWidget);
  });

  testWidgets('El login lleva a crear cuenta y a recuperar contraseña', (
    tester,
  ) async {
    await pumpApp(tester);

    expect(find.text('Iniciar Sesión'), findsOneWidget);

    await tester.tap(find.text('Crear Cuenta'));
    await tester.pumpAndSettle();
    expect(find.text('Crea tu cuenta'), findsOneWidget);

    await tester.tap(find.byIcon(Icons.arrow_back));
    await tester.pumpAndSettle();
    expect(find.text('Bienvenido de nuevo'), findsOneWidget);

    await tester.tap(find.text('¿Olvidaste tu contraseña?'));
    await tester.pumpAndSettle();
    expect(find.text('Enviar instrucciones'), findsOneWidget);
  });

  testWidgets('El registro valida los campos obligatorios', (tester) async {
    await pumpScreen(tester, const RegisterScreen());

    await tester.ensureVisible(find.text('Crear Cuenta'));
    await tester.pumpAndSettle();
    await tester.tap(find.text('Crear Cuenta'));
    await tester.pumpAndSettle();

    expect(find.text('El nombre es obligatorio'), findsOneWidget);
    expect(find.text('La fecha es obligatoria'), findsOneWidget);
    expect(find.text('Debes seleccionar un plan'), findsOneWidget);
    expect(find.text('Debes seleccionar un género'), findsOneWidget);
    expect(
      find.text(
        'Debes aceptar los términos y la política de privacidad para continuar',
      ),
      findsOneWidget,
    );
  });

  testWidgets('Recuperar contraseña valida el correo', (tester) async {
    await pumpScreen(tester, const ForgotPasswordScreen());

    await tester.tap(find.text('Enviar instrucciones'));
    await tester.pumpAndSettle();

    expect(
      find.text('Por favor, ingresa tu correo electrónico.'),
      findsOneWidget,
    );
  });

  for (final brightness in Brightness.values) {
    testWidgets('Las pantallas caben en un celular (${brightness.name})', (
      tester,
    ) async {
      await pumpScreen(tester, const RegisterScreen(), brightness: brightness);
      expect(find.text('Crea tu cuenta'), findsOneWidget);

      await pumpScreen(
        tester,
        const ForgotPasswordScreen(),
        brightness: brightness,
      );
      expect(find.text('Enviar instrucciones'), findsOneWidget);

      await pumpScreen(
        tester,
        const EmailSentScreen(email: 'test@monymonty.com'),
        brightness: brightness,
      );
      expect(find.text('¡Revisa tu correo!'), findsOneWidget);
      expect(find.text('Volver a iniciar sesión'), findsOneWidget);
    });
  }
}
