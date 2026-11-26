
import 'package:flutter/material.dart';
import '../utils/constants.dart';
import '../utils/app_routes.dart';

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Plataforma de Eventos'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(20),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              'Olá!',
              style: TextStyle(
                fontSize: 24,
                fontWeight: FontWeight.bold,
                color: AppColors.primary,
              ),
            ),

            const SizedBox(height: 10),

            const Text('Encontre eventos perto de você.'),

            const SizedBox(height: 25),

            const Text(
              'Explore eventos',
              style: TextStyle(
                fontSize: 20,
                fontWeight: FontWeight.bold,
                color: AppColors.secondary,
              ),
            ),

            const SizedBox(height: 15),

            ListTile(
              leading: const Icon(Icons.event, color: AppColors.primary),
              title: const Text('Todos os eventos'),
              trailing: const Icon(Icons.arrow_forward_ios),
              onTap: () =>
                  Navigator.pushNamed(context, AppRoutes.eventos),
            ),

            ListTile(
              leading: const Icon(Icons.category, color: AppColors.orange),
              title: const Text('Temáticas'),
              trailing: const Icon(Icons.arrow_forward_ios),
              onTap: () =>
                  Navigator.pushNamed(context, AppRoutes.tematicas),
            ),

            ListTile(
              leading: const Icon(Icons.bookmark, color: AppColors.pink),
              title: const Text('Eventos salvos'),
              trailing: const Icon(Icons.arrow_forward_ios),
              onTap: () =>
                  Navigator.pushNamed(context, AppRoutes.eventosSalvos),
            ),
          ],
        ),
      ),
    );
  }
}
