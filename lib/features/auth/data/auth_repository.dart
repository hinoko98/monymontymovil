import 'package:dio/dio.dart';

import '../../../core/api_client.dart';
import '../../../core/api_exception.dart';
import '../domain/usuario.dart';

/// Replica en Flutter las mismas llamadas que hace la web en
/// `src/features/auth/logic/useAuth.js`, contra la misma API de sesión.
class AuthRepository {
  AuthRepository(this._apiClient);

  final ApiClient _apiClient;

  Dio get _dio => _apiClient.dio;

  /// Inicia sesión con email/contraseña. Si es correcto, la API deja
  /// establecida la cookie de sesión (la maneja el CookieManager de Dio).
  Future<void> login({required String email, required String password}) async {
    try {
      final response = await _dio.post(
        'auth/login',
        data: {'email': email, 'password': password},
      );

      if (response.statusCode != 201) {
        throw ApiException('No se pudo iniciar sesión');
      }
    } on DioException catch (e) {
      throw ApiException.fromDioException(e);
    }
  }

  /// Crea una cuenta nueva (mismo endpoint `POST user` que usa la web).
  Future<void> register({
    required String nombre,
    required String apellido,
    required DateTime fechaNacimiento,
    required String genero,
    required String email,
    required String password,
    required String planId,
  }) async {
    try {
      final response = await _dio.post(
        'user',
        data: {
          'nombre': nombre,
          'apellido': apellido,
          'fechaNacimiento': fechaNacimiento.toIso8601String(),
          'genero': genero,
          'email': email,
          'password': password,
          'planId': planId,
          'acceptLegal': true,
        },
      );

      if (response.statusCode != 201) {
        throw ApiException('No se pudo crear la cuenta');
      }
    } on DioException catch (e) {
      throw ApiException.fromDioException(e);
    }
  }

  /// Envía el correo con el enlace para restablecer la contraseña.
  Future<void> recuperarCuenta(String email) async {
    try {
      final response = await _dio.post(
        'auth/recuperar',
        data: {'email': email},
      );

      if (response.statusCode != 200) {
        throw ApiException('No se pudo enviar el correo de recuperación');
      }
    } on DioException catch (e) {
      throw ApiException.fromDioException(e);
    }
  }

  /// Verifica si hay una sesión activa (cookie válida en el dispositivo).
  Future<bool> checkAuth() async {
    try {
      final response = await _dio.get('auth/check');
      if (response.statusCode == 200) {
        return response.data['authenticated'] == true;
      }
      return false;
    } on DioException {
      return false;
    }
  }

  /// Obtiene el perfil del usuario autenticado.
  Future<Usuario> getCurrentUser() async {
    try {
      final response = await _dio.get('user/me');
      if (response.statusCode == 200) {
        return Usuario.fromJson(response.data as Map<String, dynamic>);
      }
      throw ApiException('No se pudo obtener el usuario actual');
    } on DioException catch (e) {
      throw ApiException.fromDioException(e);
    }
  }

  Future<void> logout() async {
    try {
      await _dio.get('auth/logout');
    } on DioException {
      // Si falla la llamada igual limpiamos la sesión local.
    } finally {
      await _apiClient.clearSession();
    }
  }
}
