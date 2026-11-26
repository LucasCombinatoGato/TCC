
class Ong {
  final int? id;
  final String nome;
  final String email;
  final String? descricao;

  Ong({
    this.id,
    required this.nome,
    required this.email,
    this.descricao,
  });

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'nome': nome,
      'email': email,
      'descricao': descricao,
    };
  }

  factory Ong.fromJson(Map<String, dynamic> json) {
    return Ong(
      id: json['id'],
      nome: json['nome'] ?? '',
      email: json['email'] ?? '',
      descricao: json['descricao'],
    );
  }
}
