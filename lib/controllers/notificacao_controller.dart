
import 'package:flutter/material.dart';
import '../models/notificacao.dart';

class NotificacaoController extends ChangeNotifier {
  List<Notificacao> notificacoes = [];

  void carregarNotificacoes(List<Notificacao> lista) {
    notificacoes = lista;
    notifyListeners();
  }

  void marcarComoLida(int index) {
    if (index >= 0 && index < notificacoes.length) {
      final notificacao = notificacoes[index];

      notificacoes[index] = Notificacao(
        id: notificacao.id,
        titulo: notificacao.titulo,
        mensagem: notificacao.mensagem,
        data: notificacao.data,
        lida: true,
      );

      notifyListeners();
    }
  }

  void limparNotificacoes() {
    notificacoes = [];
    notifyListeners();
  }
}
