
class Tematica {
  final int? id;
  final String nome;
  final String? descricao;

  Tematica({
    this.id,
    required this.nome,
    this.descricao,
  });

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'nome': nome,
      'descricao': descricao,
    };
  }

  factory Tematica.fromJson(Map<String, dynamic> json) {
    return Tematica(
      id: json['id'],
      nome: json['nome'] ?? '',
      descricao: json['descricao'],
    );
  }
}
