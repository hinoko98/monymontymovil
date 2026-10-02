import 'dart:convert';
import 'dart:io';

import 'package:flutter_test/flutter_test.dart';

import 'package:monymontymovil/core/api_client.dart';
import 'package:monymontymovil/features/auth/data/auth_repository.dart';
import 'package:monymontymovil/features/auth/state/auth_controller.dart';

/// Servidor local que imita los contratos reales de MonyMontyApi:
/// mismas rutas, mismos códigos de estado y mismas formas de error
/// (`{message}` para negocio/auth, `{errors:[{msg}]}` para validación).
///
/// Sirve para comprobar que el cliente móvil habla el mismo idioma que la
/// API —incluida la cookie de sesión— sin depender de la base de datos.
class _FakeApi {
  _FakeApi(this._server) {
    _server.listen(_handle);
  }

  final HttpServer _server;
  static const _cookieName = 'monymonty.sid';

  String get baseUrl => 'http://${_server.address.host}:${_server.port}/';

  /// Sesiones "emitidas": se llena al hacer login y se vacía al cerrar sesión.
  final _sesionesValidas = <String>{};

  static Future<_FakeApi> start() async =>
      _FakeApi(await HttpServer.bind(InternetAddress.loopbackIPv4, 0));

  Future<void> stop() => _server.close(force: true);

  bool _tieneSesion(HttpRequest req) {
    final cookie = req.cookies
        .where((c) => c.name == _cookieName)
        .map((c) => c.value);
    return cookie.isNotEmpty && _sesionesValidas.contains(cookie.first);
  }

  Future<void> _responder(
    HttpRequest req,
    int status,
    Map<String, dynamic> body,
  ) async {
    req.response.statusCode = status;
    req.response.headers.contentType = ContentType.json;
    req.response.write(jsonEncode(body));
    await req.response.close();
  }

  Future<void> _handle(HttpRequest req) async {
    final ruta = '${req.method} ${req.uri.path}';
    final cuerpo = req.method == 'POST'
        ? jsonDecode(await utf8.decoder.bind(req).join())
              as Map<String, dynamic>
        : <String, dynamic>{};

    switch (ruta) {
      case 'POST /auth/login':
        if (cuerpo['password'] != 'Correcta1!') {
          return _responder(req, 401, {'message': 'Credenciales inválidas'});
        }
        _sesionesValidas.add('sesion-1');
        req.response.cookies.add(Cookie(_cookieName, 'sesion-1')..path = '/');
        return _responder(req, 201, {'token': 'jwt-de-prueba'});

      case 'GET /auth/check':
        return _tieneSesion(req)
            ? _responder(req, 200, {'authenticated': true})
            : _responder(req, 401, {'authenticated': false});

      case 'GET /user/me':
        if (!_tieneSesion(req)) {
          return _responder(req, 401, {'message': 'Not authenticated'});
        }
        return _responder(req, 200, {
          '_id': '65f0a1',
          'nombre': 'Johame',
          'apellido': 'Elias',
          'email': 'johame@monymonty.com',
          'avatar': null,
        });

      case 'GET /auth/logout':
        _sesionesValidas.clear();
        return _responder(req, 200, {
          'message': 'Sesión cerrada correctamente',
        });

      case 'POST /user':
        if (cuerpo['email'] == 'repetido@monymonty.com') {
          return _responder(req, 400, {
            'message': 'El correo ya está registrado.',
          });
        }
        if (cuerpo['genero'] == 'Otro') {
          return _responder(req, 400, {
            'errors': [
              {'msg': 'Género debe ser Femenino o Masculino'},
            ],
          });
        }
        return _responder(req, 201, {'_id': 'nuevo'});

      case 'POST /auth/recuperar':
        if (cuerpo['email'] == 'nadie@monymonty.com') {
          return _responder(req, 404, {'message': 'Usuario no encontrado'});
        }
        return _responder(req, 200, {
          'message': 'Correo de recuperación enviado',
        });

      default:
        return _responder(req, 404, {'message': 'Ruta no encontrada'});
    }
  }
}

void main() {
  late _FakeApi api;
  late AuthController auth;

  setUp(() async {
    api = await _FakeApi.start();
    auth = AuthController(
      AuthRepository(ApiClient.inMemory(baseUrl: api.baseUrl)),
    );
  });

  tearDown(() => api.stop());

  test('El login guarda la cookie de sesión y carga el perfil', () async {
    final ok = await auth.login(
      email: 'johame@monymonty.com',
      password: 'Correcta1!',
    );

    expect(ok, isTrue);
    expect(auth.status, AuthStatus.authenticated);
    // Que /user/me haya respondido significa que la cookie de sesión viajó
    // en la segunda petición, igual que hace el navegador en la web.
    expect(auth.usuario?.nombreCompleto, 'Johame Elias');
    expect(auth.usuario?.email, 'johame@monymonty.com');
  });

  test('Un login incorrecto muestra el mensaje real de la API', () async {
    final ok = await auth.login(
      email: 'johame@monymonty.com',
      password: 'malaclave',
    );

    expect(ok, isFalse);
    expect(auth.errorMessage, 'Credenciales inválidas');
    expect(auth.status, AuthStatus.unauthenticated);
  });

  test('Cerrar sesión borra la cookie guardada', () async {
    await auth.login(email: 'johame@monymonty.com', password: 'Correcta1!');
    await auth.logout();

    expect(auth.status, AuthStatus.unauthenticated);
    expect(auth.usuario, isNull);

    // Sin cookie, la app arranca como no autenticada.
    await auth.bootstrap();
    expect(auth.status, AuthStatus.unauthenticated);
  });

  test('El registro muestra el mensaje de correo ya registrado', () async {
    final ok = await auth.register(
      nombre: 'Johame',
      apellido: 'Elias',
      fechaNacimiento: DateTime(1998, 5, 20),
      genero: 'Masculino',
      email: 'repetido@monymonty.com',
      password: 'Correcta1!',
      planId: 'free',
    );

    expect(ok, isFalse);
    expect(auth.errorMessage, 'El correo ya está registrado.');
  });

  test('El registro muestra los errores de validación de la API', () async {
    final ok = await auth.register(
      nombre: 'Johame',
      apellido: 'Elias',
      fechaNacimiento: DateTime(1998, 5, 20),
      genero: 'Otro',
      email: 'nuevo@monymonty.com',
      password: 'Correcta1!',
      planId: 'free',
    );

    expect(ok, isFalse);
    expect(auth.errorMessage, 'Género debe ser Femenino o Masculino');
  });

  test('Un registro válido se completa', () async {
    final ok = await auth.register(
      nombre: 'Johame',
      apellido: 'Elias',
      fechaNacimiento: DateTime(1998, 5, 20),
      genero: 'Masculino',
      email: 'nuevo@monymonty.com',
      password: 'Correcta1!',
      planId: 'free',
    );

    expect(ok, isTrue);
    expect(auth.errorMessage, isNull);
  });

  test('Recuperar contraseña propaga el resultado de la API', () async {
    expect(await auth.recuperarCuenta('johame@monymonty.com'), isTrue);

    expect(await auth.recuperarCuenta('nadie@monymonty.com'), isFalse);
    expect(auth.errorMessage, 'Usuario no encontrado');
  });
}
