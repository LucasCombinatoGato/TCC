
import 'package:flutter/material.dart';
import '../models/evento.dart';

class HomeController extends ChangeNotifier {
  List<Evento> eventos = [];

  bool carregando = false;

  void atualizarEventos(List<Evento> lista) {
    eventos = lista;
    notifyListeners();
  }

  void limparEventos() {
    eventos = [];
    notifyListeners();
  }
}
