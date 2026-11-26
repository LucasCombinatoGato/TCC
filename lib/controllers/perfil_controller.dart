
import 'package:flutter/material.dart';
import '../models/usuario.dart';

class PerfilController extends ChangeNotifier {
  Usuario? usuario;

  void carregarUsuario(Usuario dados) {
    usuario = dados;
    notifyListeners();
  }

  void atualizarUsuario(Usuario dados) {
    usuario = dados;
    notifyListeners();
  }

  void limparPerfil() {
    usuario = null;
    notifyListeners();
  }
}
