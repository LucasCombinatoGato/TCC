
class IdentidadeVisual {
  final int? id;
  final String? imagem;
  final String? corPrincipal;
  final String? corSecundaria;

  IdentidadeVisual({
    this.id,
    this.imagem,
    this.corPrincipal,
    this.corSecundaria,
  });

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'imagem': imagem,
      'corPrincipal': corPrincipal,
      'corSecundaria': corSecundaria,
    };
  }

  factory IdentidadeVisual.fromJson(Map<String, dynamic> json) {
    return IdentidadeVisual(
      id: json['id'],
      imagem: json['imagem'],
      corPrincipal: json['corPrincipal'],
      corSecundaria: json['corSecundaria'],
    );
  }
}
