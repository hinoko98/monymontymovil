/// Datos del usuario devueltos por `GET /user/me`.
class Usuario {
  const Usuario({
    required this.id,
    required this.nombre,
    required this.apellido,
    required this.email,
    this.avatar,
  });

  final String id;
  final String nombre;
  final String apellido;
  final String email;
  final String? avatar;

  String get nombreCompleto =>
      [nombre, apellido].where((p) => p.isNotEmpty).join(' ');

  factory Usuario.fromJson(Map<String, dynamic> json) {
    return Usuario(
      id: (json['_id'] ?? json['id'] ?? '').toString(),
      nombre: (json['nombre'] ?? '').toString(),
      apellido: (json['apellido'] ?? '').toString(),
      email: (json['email'] ?? '').toString(),
      avatar: json['avatar'] as String?,
    );
  }
}
