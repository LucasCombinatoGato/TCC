
class PreferenciaUsuario {
  final int? id;
  final int idUsuario;
  final int idTematica;

  PreferenciaUsuario({
    this.id,
    required this.idUsuario,
    required this.idTematica,
  });

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'idUsuario': idUsuario,
      'idTematica': idTematica,
    };
  }

  factory PreferenciaUsuario.fromJson(Map<String, dynamic> json) {
    return PreferenciaUsuario(
      id: json['id'],
      idUsuario: json['idUsuario'] ?? 0,
      idTematica: json['idTematica'] ?? 0,
    );
  }
}
