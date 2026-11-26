
class Evento {
  final int? id;
  final String nome;
  final String descricao;
  final String? imagem;
  final String? data;
  final String? horario;
  final int? idTematica;

  Evento({
    this.id,
    required this.nome,
    required this.descricao,
    this.imagem,
    this.data,
    this.horario,
    this.idTematica,
  });

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'nome': nome,
      'descricao': descricao,
      'imagem': imagem,
      'data': data,
      'horario': horario,
      'idTematica': idTematica,
    };
  }

  factory Evento.fromJson(Map<String, dynamic> json) {
    return Evento(
      id: json['id'],
      nome: json['nome'] ?? '',
      descricao: json['descricao'] ?? '',
      imagem: json['imagem'],
      data: json['data'],
      horario: json['horario'],
      idTematica: json['idTematica'],
    );
  }
}
