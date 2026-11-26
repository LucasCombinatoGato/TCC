
import 'package:flutter/material.dart';
import '../utils/constants.dart';
import '../app_state.dart';

class NotificacoesScreen extends StatefulWidget {
  const NotificacoesScreen({super.key});

  @override
  State<NotificacoesScreen> createState() => _NotificacoesScreenState();
}

class _NotificacoesScreenState extends State<NotificacoesScreen> {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Notificações'),
      ),
      body: AppState.instance.notificacoes.isEmpty
          ? const Center(
              child: Text(
                'Você não possui notificações.',
                style: TextStyle(color: AppColors.secondary),
              ),
            )
          : ListView.builder(
              itemCount: AppState.instance.notificacoes.length,
              itemBuilder: (context, index) {
                final notificacao =
                    AppState.instance.notificacoes[index];
                return ListTile(
                  leading: Icon(
                    notificacao.lida
                        ? Icons.notifications_none
                        : Icons.notifications_active,
                    color: AppColors.orange,
                  ),
                  title: Text(notificacao.titulo),
                  subtitle: Text(
                    '${notificacao.mensagem}\n${notificacao.data ?? ''}',
                  ),
                  isThreeLine: true,
                  onTap: () {
                    AppState.instance.marcarNotificacaoComoLida(index);
                    setState(() {});
                  },
                );
              },
            ),
    );
  }
}
