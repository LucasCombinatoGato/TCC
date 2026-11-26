
import 'package:flutter/material.dart';

class AuthController extends ChangeNotifier {
  bool carregando = false;
  bool autenticado = false;

  Future<void> login(String email, String senha) async {
    carregando = true;
    notifyListeners();

    // A autenticação será conectada à API posteriormente.

    carregando = false;
    notifyListeners();
  }

  Future<void> cadastro(
    String nome,
    String email,
    String senha,
  ) async {
    carregando = true;
    notifyListeners();

    // O cadastro será conectado à API posteriormente.

    carregando = false;
    notifyListeners();
  }

  void logout() {
    autenticado = false;
    notifyListeners();
  }
}
