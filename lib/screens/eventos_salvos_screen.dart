
import 'package:flutter/material.dart';
import '../utils/constants.dart';
import '../app_state.dart';
import '../utils/app_routes.dart';
import '../widgets/event_card.dart';

class EventosSalvosScreen extends StatefulWidget {
  final bool showBottomNavigation;
  const EventosSalvosScreen({
    super.key,
    this.showBottomNavigation = true,
  });

  @override
  State<EventosSalvosScreen> createState() => _EventosSalvosScreenState();
}

class _EventosSalvosScreenState extends State<EventosSalvosScreen> {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Eventos salvos'),
      ),
      body: Builder(builder: (context) {
        final eventos = AppState.instance.eventos
            .where((evento) =>
                AppState.instance.eventosSalvos.contains(evento.id))
            .toList();
        if (eventos.isEmpty) {
          return const Center(
            child: Text(
              'Você ainda não salvou nenhum evento.',
              style: TextStyle(color: AppColors.secondary),
            ),
          );
        }
        return ListView.builder(
          padding: const EdgeInsets.all(16),
          itemCount: eventos.length,
          itemBuilder: (context, index) {
            final evento = eventos[index];
            return Dismissible(
              key: ValueKey(evento.id),
              direction: DismissDirection.endToStart,
              onDismissed: (_) =>
                  AppState.instance.alternarEventoSalvo(evento.id!),
              background: Container(
                alignment: Alignment.centerRight,
                padding: const EdgeInsets.only(right: 20),
                color: AppColors.orangeDark,
                child: const Icon(Icons.delete, color: Colors.white),
              ),
              child: EventCard(
                nome: evento.nome,
                descricao: evento.descricao,
                data: evento.data ?? 'Data a confirmar',
                onTap: () => Navigator.pushNamed(
                  context,
                  AppRoutes.detalhesEvento,
                  arguments: evento,
                ),
              ),
            );
          },
        );
      }),
    );
  }
}
