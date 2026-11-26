
class TipoIngresso {
  final int? id;
  final String nome;
  final double valor;
  final int quantidade;

  TipoIngresso({
    this.id,
    required this.nome,
    required this.valor,
    required this.quantidade,
  });

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'nome': nome,
      'valor': valor,
      'quantidade': quantidade,
    };
  }

  factory TipoIngresso.fromJson(Map<String, dynamic> json) {
    return TipoIngresso(
      id: json['id'],
      nome: json['nome'] ?? '',
      valor: (json['valor'] as num?)?.toDouble() ?? 0.0,
      quantidade: json['quantidade'] ?? 0,
    );
  }
}
