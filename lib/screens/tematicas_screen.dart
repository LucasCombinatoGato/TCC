
import 'package:flutter/material.dart';
import '../utils/constants.dart';
import '../app_state.dart';
import '../screens/eventos_screen.dart';

class TematicasScreen extends StatefulWidget {
  final bool selecionarPreferencias;
  const TematicasScreen({super.key, this.selecionarPreferencias = false});

  @override
  State<TematicasScreen> createState() => _TematicasScreenState();
}

class _TematicasScreenState extends State<TematicasScreen> {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Temáticas'),
      ),
      body: ListView.builder(
        padding: const EdgeInsets.all(16),
        itemCount: AppState.instance.tematicas.length,
        itemBuilder: (context, index) {
          final tematica = AppState.instance.tematicas[index];
          final selecionada =
              AppState.instance.preferencias.contains(tematica.id);
          return Card(
            color: AppColors.secondary.withAlpha(25),
            child: ListTile(
              leading: const Icon(
                Icons.category,
                color: AppColors.primary,
              ),
              title: Text(tematica.nome),
              trailing: widget.selecionarPreferencias
                  ? Checkbox(
                      value: selecionada,
                      onChanged: (_) => setState(() {
                        AppState.instance.alternarPreferencia(tematica.id!);
                      }),
                    )
                  : const Icon(Icons.arrow_forward_ios),
              onTap: widget.selecionarPreferencias
                  ? () => setState(() {
                        AppState.instance.alternarPreferencia(tematica.id!);
                      })
                  : () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) => EventosScreen(
                            showBottomNavigation: false,
                            initialTematicaId: tematica.id,
                          ),
                        ),
                      );
                    },
            ),
          );
        },
      ),
    );
  }
}
