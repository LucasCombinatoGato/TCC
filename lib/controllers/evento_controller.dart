
import 'package:flutter/material.dart';
import '../models/evento.dart';

class EventoController extends ChangeNotifier {
  List<Evento> eventos = [];
  List<Evento> eventosFiltrados = [];

  String pesquisa = '';

  void carregarEventos(List<Evento> lista) {
    eventos = lista;
    eventosFiltrados = lista;
    notifyListeners();
  }

  void pesquisar(String texto) {
    pesquisa = texto;

    eventosFiltrados = eventos.where((evento) {
      return evento.nome.toLowerCase().contains(
        texto.toLowerCase(),
      );
    }).toList();

    notifyListeners();
  }

  void limparPesquisa() {
    pesquisa = '';
    eventosFiltrados = eventos;
    notifyListeners();
  }
}
