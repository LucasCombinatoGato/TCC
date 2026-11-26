
import 'package:flutter/material.dart';
import '../utils/constants.dart';
import '../app_state.dart';
import '../models/evento.dart';
import '../utils/app_routes.dart';
import '../widgets/event_card.dart';

class EventosScreen extends StatefulWidget {
  final bool showBottomNavigation;
  final int? initialTematicaId;

  const EventosScreen({
    super.key,
    this.showBottomNavigation = true,
    this.initialTematicaId,
  });

  @override
  State<EventosScreen> createState() => _EventosScreenState();
}

class _EventosScreenState extends State<EventosScreen> {
  final TextEditingController _searchController = TextEditingController();
  int? _tematicaId;

  @override
  void initState() {
    super.initState();
    _tematicaId = widget.initialTematicaId;
  }

  List<Evento> get _eventosFiltrados {
    final pesquisa = _searchController.text.trim().toLowerCase();
    return AppState.instance.eventos.where((evento) {
      final matchesSearch = pesquisa.isEmpty ||
          evento.nome.toLowerCase().contains(pesquisa) ||
          evento.descricao.toLowerCase().contains(pesquisa);
      final matchesTheme =
          _tematicaId == null || evento.idTematica == _tematicaId;
      return matchesSearch && matchesTheme;
    }).toList();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Eventos'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            TextField(
              controller: _searchController,
              onChanged: (_) => setState(() {}),
              decoration: InputDecoration(
                hintText: 'Buscar eventos',
                prefixIcon: const Icon(
                  Icons.search,
                  color: AppColors.primary,
                ),
                focusedBorder: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(12),
                  borderSide: const BorderSide(
                    color: AppColors.primary,
                    width: 2,
                  ),
                ),
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(12),
                ),
              ),
            ),

            const SizedBox(height: 20),

            Expanded(
              child: _eventosFiltrados.isEmpty
                  ? const Center(child: Text('Nenhum evento disponível.'))
                  : ListView.builder(
                      itemCount: _eventosFiltrados.length,
                      itemBuilder: (context, index) {
                        final evento = _eventosFiltrados[index];
                        return EventCard(
                          nome: evento.nome,
                          descricao: evento.descricao,
                          data: evento.data ?? 'Data a confirmar',
                          onTap: () => Navigator.pushNamed(
                            context,
                            AppRoutes.detalhesEvento,
                            arguments: evento,
                          ),
                        );
                      },
                    ),
            ),
          ],
        ),
      ),
    );
  }
}
