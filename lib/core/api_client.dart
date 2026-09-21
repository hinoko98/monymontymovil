import 'package:cookie_jar/cookie_jar.dart';
import 'package:dio/dio.dart';
import 'package:dio_cookie_manager/dio_cookie_manager.dart';
import 'package:path_provider/path_provider.dart';

import 'env.dart';

/// Cliente HTTP único para toda la app.
///
/// La API mantiene la sesión con una cookie (`express-session`), por lo que
/// el cliente necesita comportarse como un navegador: guardar la cookie que
/// llega en el `login` y reenviarla en cada petición posterior. Para eso se
/// usa un [CookieJar] (persistido en disco en la app real).
class ApiClient {
  ApiClient._internal(this.dio, this._cookieJar);

  final Dio dio;
  final CookieJar _cookieJar;

  static ApiClient? _instance;

  static Future<ApiClient> getInstance() async {
    if (_instance != null) return _instance!;

    final appDir = await getApplicationDocumentsDirectory();
    final cookieJar = PersistCookieJar(
      storage: FileStorage('${appDir.path}/.cookies/'),
    );

    _instance = ApiClient._internal(_buildDio(), cookieJar);
    _instance!.dio.interceptors.add(CookieManager(cookieJar));
    return _instance!;
  }

  /// Cliente sin persistencia en disco (cookies solo en memoria), útil para
  /// tests de widgets donde no hay canal de plataforma para `path_provider`.
  factory ApiClient.inMemory() {
    final cookieJar = CookieJar();
    final client = ApiClient._internal(_buildDio(), cookieJar);
    client.dio.interceptors.add(CookieManager(cookieJar));
    return client;
  }

  static Dio _buildDio() {
    return Dio(
      BaseOptions(
        baseUrl: Env.apiBaseUrl,
        // Cortos a propósito: si la API no está levantada (p. ej. probando
        // solo la UI), la app no debe quedarse pegada en el splash.
        connectTimeout: const Duration(seconds: 5),
        receiveTimeout: const Duration(seconds: 5),
        // Igual que `withCredentials: true` en axios: el jar de cookies se
        // encarga de adjuntar la cookie de sesión en cada request.
        validateStatus: (status) => status != null && status < 500,
      ),
    );
  }

  /// Borra todas las cookies guardadas (usado al cerrar sesión).
  Future<void> clearSession() => _cookieJar.deleteAll();
}
