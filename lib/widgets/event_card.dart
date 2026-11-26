import 'package:flutter/material.dart';
import '../utils/constants.dart';

class EventCard extends StatelessWidget {
  final String nome;
  final String descricao;
  final String data;
  final VoidCallback onTap;

  const EventCard({
    super.key,
    required this.nome,
    required this.descricao,
    required this.data,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: const EdgeInsets.symmetric(vertical: 8),
      child: ListTile(
        leading: const Icon(
          Icons.event,
          color: AppColors.primary,
          size: 35,
        ),
        title: Text(nome),
        subtitle: Text('$data\n$descricao'),
        onTap: onTap,
      ),
    );
  }
}