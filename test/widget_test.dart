import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:provider/provider.dart';

import 'package:monymontymovil/app.dart';
import 'package:monymontymovil/core/api_client.dart';
import 'package:monymontymovil/features/auth/data/auth_repository.dart';
import 'package:monymontymovil/features/auth/state/auth_controller.dart';

/// Repositorio de pruebas: evita cualquier llamada de red real durante el
/// widget test (`checkAuth` normalmente pega contra la API por HTTP).
class _NoNetworkAuthRepository extends AuthRepository {
  _NoNetworkAuthRepository(super.apiClient);

  @override
  Future<bool> checkAuth() async => false;
}

void main() {
  testWidgets('Muestra el formulario de login cuando no hay sesión', (WidgetTester tester) async {
    final authController = AuthController(_NoNetworkAuthRepository(ApiClient.inMemory()));

    await tester.pumpWidget(
      ChangeNotifierProvider.value(
        value: authController,
        child: const MonyMontyApp(),
      ),
    );

    // Sin sesión guardada (ni backend disponible en el test), debe
    // resolver a "no autenticado" y mostrar la pantalla de login.
    await authController.bootstrap();
    await tester.pumpAndSettle();

    expect(find.text('MonyMonty'), findsOneWidget);
    expect(find.widgetWithText(TextFormField, 'Correo electrónico'), findsOneWidget);
    expect(find.text('Iniciar sesión'), findsOneWidget);
  });
}
