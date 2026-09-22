/// Datos del usuario devueltos por `GET /user/me`.
class Usuario {
  const Usuario({
    required this.id,
    required this.nombre,
    required this.email,
    this.avatar,
  });

  final String id;
  final String nombre;
  final String email;
  final String? avatar;

  factory Usuario.fromJson(Map<String, dynamic> json) {
    return Usuario(
      id: (json['_id'] ?? json['id'] ?? '').toString(),
      nombre: (json['nombre'] ?? '').toString(),
      email: (json['email'] ?? '').toString(),
      avatar: json['avatar'] as String?,
    );
  }
}
