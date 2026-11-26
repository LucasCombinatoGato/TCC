
class Local {
  final int? id;
  final String nome;
  final String endereco;
  final String cidade;
  final String? estado;

  Local({
    this.id,
    required this.nome,
    required this.endereco,
    required this.cidade,
    this.estado,
  });

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'nome': nome,
      'endereco': endereco,
      'cidade': cidade,
      'estado': estado,
    };
  }

  factory Local.fromJson(Map<String, dynamic> json) {
    return Local(
      id: json['id'],
      nome: json['nome'] ?? '',
      endereco: json['endereco'] ?? '',
      cidade: json['cidade'] ?? '',
      estado: json['estado'],
    );
  }
}
