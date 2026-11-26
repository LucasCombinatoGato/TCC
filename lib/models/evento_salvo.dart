
class EventoSalvo {
  final int? id;
  final int idUsuario;
  final int idEvento;

  EventoSalvo({
    this.id,
    required this.idUsuario,
    required this.idEvento,
  });

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'idUsuario': idUsuario,
      'idEvento': idEvento,
    };
  }

  factory EventoSalvo.fromJson(Map<String, dynamic> json) {
    return EventoSalvo(
      id: json['id'],
      idUsuario: json['idUsuario'] ?? 0,
      idEvento: json['idEvento'] ?? 0,
    );
  }
}
