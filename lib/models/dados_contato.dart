
class DadosContato {
  final int? id;
  final String email;
  final String? telefone;
  final String? site;

  DadosContato({
    this.id,
    required this.email,
    this.telefone,
    this.site,
  });

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'email': email,
      'telefone': telefone,
      'site': site,
    };
  }

  factory DadosContato.fromJson(Map<String, dynamic> json) {
    return DadosContato(
      id: json['id'],
      email: json['email'] ?? '',
      telefone: json['telefone'],
      site: json['site'],
    );
  }
}
