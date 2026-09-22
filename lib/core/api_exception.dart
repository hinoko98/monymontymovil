import 'package:dio/dio.dart';

/// Excepción con un mensaje ya listo para mostrar al usuario.
///
/// La API responde errores de dos formas distintas:
/// - Validación (400): `{ "errors": [{ "msg": "..." }, ...] }`
/// - Negocio/auth (401, 404, etc.): `{ "message": "..." }`
class ApiException implements Exception {
  ApiException(this.message);

  final String message;

  factory ApiException.fromDioException(DioException e) {
    final data = e.response?.data;

    if (data is Map) {
      final errors = data['errors'];
      if (errors is List && errors.isNotEmpty) {
        final first = errors.first;
        if (first is Map && first['msg'] != null) {
          return ApiException(first['msg'].toString());
        }
      }

      final message = data['message'];
      if (message is String && message.isNotEmpty) {
        return ApiException(message);
      }
    }

    if (e.type == DioExceptionType.connectionTimeout ||
        e.type == DioExceptionType.receiveTimeout ||
        e.type == DioExceptionType.connectionError) {
      return ApiException(
        'No se pudo conectar con el servidor. Verifica tu conexión.',
      );
    }

    return ApiException(
      'Ocurrió un error inesperado. Intenta nuevamente más tarde.',
    );
  }

  @override
  String toString() => message;
}
