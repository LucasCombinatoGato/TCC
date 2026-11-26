
class InformacoesDia {
  final int? id;
  final String data;
  final String horarioInicio;
  final String horarioFim;

  InformacoesDia({
    this.id,
    required this.data,
    required this.horarioInicio,
    required this.horarioFim,
  });

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'data': data,
      'horarioInicio': horarioInicio,
      'horarioFim': horarioFim,
    };
  }

  factory InformacoesDia.fromJson(Map<String, dynamic> json) {
    return InformacoesDia(
      id: json['id'],
      data: json['data'] ?? '',
      horarioInicio: json['horarioInicio'] ?? '',
      horarioFim: json['horarioFim'] ?? '',
    );
  }
}
