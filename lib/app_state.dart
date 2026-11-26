import 'package:flutter/material.dart';

import 'models/evento.dart';
import 'models/notificacao.dart';
import 'models/tematica.dart';
import 'models/usuario.dart';

class AppState extends ChangeNotifier {
  AppState._();

  static final AppState instance = AppState._();

  final List<Usuario> usuarios = [];
  Usuario? usuarioLogado;

  final List<Tematica> tematicas = [
    Tematica(id: 1, nome: 'Música'),
    Tematica(id: 2, nome: 'Esportes'),
    Tematica(id: 3, nome: 'Educação'),
    Tematica(id: 4, nome: 'Cultura'),
    Tematica(id: 5, nome: 'Tecnologia'),
    Tematica(id: 6, nome: 'Gastronomia'),
    Tematica(id: 7, nome: 'Arte'),
    Tematica(id: 8, nome: 'Entretenimento'),
  ];

  final List<int> preferencias = [];
  final Set<int> eventosSalvos = {};

  final List<Evento> eventos = [
    Evento(
      id: 1,
      nome: 'Festival de Música da Cidade',
      descricao: 'Uma noite com artistas locais, food trucks e apresentações.',
      data: '30/12/2026',
      horario: '19h',
      idTematica: 1,
    ),
    Evento(
      id: 2,
      nome: 'Maratona Comunitária',
      descricao: 'Percurso para todas as idades reunindo esporte e solidariedade.',
      data: '12/01/2027',
      horario: '07h',
      idTematica: 2,
    ),
    Evento(
      id: 3,
      nome: 'Encontro de Tecnologia',
      descricao: 'Palestras sobre programação, inovação e carreira em tecnologia.',
      data: '20/02/2027',
      horario: '14h',
      idTematica: 5,
    ),
    Evento(
      id: 4,
      nome: 'Mostra de Arte Urbana',
      descricao: 'Exposição ao ar livre com artistas independentes da região.',
      data: '05/03/2027',
      horario: '16h',
      idTematica: 7,
    ),
  ];

  final List<Notificacao> notificacoes = [
    Notificacao(
      id: 1,
      titulo: 'Novos eventos disponíveis',
      mensagem: 'Confira as novidades e encontre seu próximo evento.',
      data: 'Hoje',
    ),
  ];

  bool cadastrar(String nome, String email, String senha) {
    final emailNormalizado = email.trim().toLowerCase();
    if (nome.trim().isEmpty ||
        emailNormalizado.isEmpty ||
        senha.length < 6 ||
        usuarios.any((usuario) => usuario.email.toLowerCase() == emailNormalizado)) {
      return false;
    }

    final usuario = Usuario(
      id: usuarios.length + 1,
      nome: nome.trim(),
      email: emailNormalizado,
      senha: senha,
    );
    usuarios.add(usuario);
    usuarioLogado = usuario;
    notifyListeners();
    return true;
  }

  bool login(String email, String senha) {
    final usuario = usuarios.where((item) =>
        item.email.toLowerCase() == email.trim().toLowerCase() &&
        item.senha == senha).toList();
    if (usuario.isEmpty) return false;
    usuarioLogado = usuario.first;
    notifyListeners();
    return true;
  }

  void atualizarUsuario(String nome, String email) {
    if (usuarioLogado == null) return;
    usuarioLogado = Usuario(
      id: usuarioLogado!.id,
      nome: nome.trim(),
      email: email.trim(),
      senha: usuarioLogado!.senha,
    );
    final index =
        usuarios.indexWhere((usuario) => usuario.id == usuarioLogado!.id);
    if (index >= 0) usuarios[index] = usuarioLogado!;
    notifyListeners();
  }

  void logout() {
    usuarioLogado = null;
    preferencias.clear();
    notifyListeners();
  }

  void alternarPreferencia(int idTematica) {
    if (preferencias.contains(idTematica)) {
      preferencias.remove(idTematica);
    } else {
      preferencias.add(idTematica);
    }
    notifyListeners();
  }

  void alternarEventoSalvo(int idEvento) {
    if (eventosSalvos.contains(idEvento)) {
      eventosSalvos.remove(idEvento);
    } else {
      eventosSalvos.add(idEvento);
    }
    notifyListeners();
  }

  void marcarNotificacaoComoLida(int index) {
    if (index < 0 || index >= notificacoes.length) return;
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
