
import 'package:flutter/material.dart';
import '../utils/constants.dart';
import '../models/evento.dart';
import '../app_state.dart';

class DetalhesEventoScreen extends StatefulWidget {
  final Evento evento;
  const DetalhesEventoScreen({super.key, required this.evento});

  @override
  State<DetalhesEventoScreen> createState() => _DetalhesEventoScreenState();
}

class _DetalhesEventoScreenState extends State<DetalhesEventoScreen> {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Detalhes do evento'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(20),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              widget.evento.nome,
              style: TextStyle(
                fontSize: 24,
                fontWeight: FontWeight.bold,
                color: AppColors.primary,
              ),
            ),

            const SizedBox(height: 20),

            Text(widget.evento.descricao),

            const SizedBox(height: 20),

            ListTile(
              leading: const Icon(Icons.calendar_month, color: AppColors.orange),
              title: Text(
                '${widget.evento.data ?? 'Data a confirmar'}'
                '${widget.evento.horario != null ? ' - ${widget.evento.horario}' : ''}',
              ),
            ),

            const ListTile(
              leading: Icon(Icons.location_on, color: AppColors.orangeDark),
              title: Text('Local a confirmar'),
            ),

            const ListTile(
              leading: Icon(Icons.person, color: AppColors.secondary),
              title: Text('Organizador'),
            ),

            const SizedBox(height: 20),

            ElevatedButton(
              style: ElevatedButton.styleFrom(
                backgroundColor: AppColors.pink,
                foregroundColor: Colors.white,
              ),
              onPressed: () {
                AppState.instance.alternarEventoSalvo(widget.evento.id!);
                setState(() {});
              },
              child: Text(
                AppState.instance.eventosSalvos.contains(widget.evento.id)
                    ? 'Remover dos favoritos'
                    : 'Salvar evento',
              ),
            ),
          ],
        ),
      ),
    );
  }
}
