
class Notificacao {
  final int? id;
  final String titulo;
  final String mensagem;
  final String? data;
  final bool lida;

  Notificacao({
    this.id,
    required this.titulo,
    required this.mensagem,
    this.data,
    this.lida = false,
  });

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'titulo': titulo,
      'mensagem': mensagem,
      'data': data,
      'lida': lida,
    };
  }

  factory Notificacao.fromJson(Map<String, dynamic> json) {
    return Notificacao(
      id: json['id'],
      titulo: json['titulo'] ?? '',
      mensagem: json['mensagem'] ?? '',
      data: json['data'],
      lida: json['lida'] ?? false,
    );
  }
}
