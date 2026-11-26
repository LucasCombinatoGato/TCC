
class Produtor {
  final int? id;
  final String nome;
  final String email;
  final String? telefone;

  Produtor({
    this.id,
    required this.nome,
    required this.email,
    this.telefone,
  });

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'nome': nome,
      'email': email,
      'telefone': telefone,
    };
  }

  factory Produtor.fromJson(Map<String, dynamic> json) {
    return Produtor(
      id: json['id'],
      nome: json['nome'] ?? '',
      email: json['email'] ?? '',
      telefone: json['telefone'],
    );
  }
}
