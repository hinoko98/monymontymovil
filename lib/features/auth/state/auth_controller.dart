import 'package:flutter/foundation.dart';

import '../../../core/api_exception.dart';
import '../data/auth_repository.dart';
import '../domain/usuario.dart';

enum AuthStatus { unknown, authenticated, unauthenticated }

/// Estado de autenticación compartido por toda la app.
class AuthController extends ChangeNotifier {
  AuthController(this._repository);

  final AuthRepository _repository;

  AuthStatus status = AuthStatus.unknown;
  Usuario? usuario;
  bool loading = false;
  String? errorMessage;

  /// Se llama al arrancar la app: revisa si ya existe una cookie de sesión
  /// válida guardada en el dispositivo de una vez anterior.
  Future<void> bootstrap() async {
    final authenticated = await _repository.checkAuth();
    if (authenticated) {
      await _loadUsuario();
    } else {
      status = AuthStatus.unauthenticated;
      notifyListeners();
    }
  }

  Future<bool> login({required String email, required String password}) async {
    loading = true;
    errorMessage = null;
    notifyListeners();

    try {
      await _repository.login(email: email, password: password);
      await _loadUsuario();
      return true;
    } on ApiException catch (e) {
      status = AuthStatus.unauthenticated;
      errorMessage = e.message;
      return false;
    } finally {
      loading = false;
      notifyListeners();
    }
  }

  Future<void> logout() async {
    loading = true;
    notifyListeners();

    await _repository.logout();

    usuario = null;
    status = AuthStatus.unauthenticated;
    loading = false;
    notifyListeners();
  }

  Future<void> _loadUsuario() async {
    try {
      usuario = await _repository.getCurrentUser();
      status = AuthStatus.authenticated;
    } on ApiException {
      // La sesión (cookie) es válida pero no se pudo cargar el perfil.
      status = AuthStatus.authenticated;
      usuario = null;
    }
    notifyListeners();
  }
}
