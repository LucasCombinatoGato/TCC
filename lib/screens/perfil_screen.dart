
import 'package:flutter/material.dart';
import '../utils/constants.dart';
import '../app_state.dart';
import '../utils/app_routes.dart';
import 'tematicas_screen.dart';

class PerfilScreen extends StatefulWidget {
  final bool showBottomNavigation;
  const PerfilScreen({super.key, this.showBottomNavigation = true});

  @override
  State<PerfilScreen> createState() => _PerfilScreenState();
}

class _PerfilScreenState extends State<PerfilScreen> {
  Future<void> _editarPerfil() async {
    final usuario = AppState.instance.usuarioLogado;
    if (usuario == null) return;
    final nomeController = TextEditingController(text: usuario.nome);
    final emailController = TextEditingController(text: usuario.email);

    await showDialog<void>(
      context: context,
      builder: (dialogContext) => AlertDialog(
        title: const Text('Editar perfil'),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            TextField(
              controller: nomeController,
              decoration: const InputDecoration(labelText: 'Nome'),
            ),
            TextField(
              controller: emailController,
              decoration: const InputDecoration(labelText: 'E-mail'),
            ),
          ],
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(dialogContext),
            child: const Text('Cancelar'),
          ),
          ElevatedButton(
            onPressed: () {
              if (nomeController.text.trim().isEmpty ||
                  emailController.text.trim().isEmpty) {
                return;
              }
              AppState.instance.atualizarUsuario(
                nomeController.text,
                emailController.text,
              );
              Navigator.pop(dialogContext);
            },
            child: const Text('Salvar'),
          ),
        ],
      ),
    );
    if (mounted) setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Meu perfil'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(20),
        child: Column(
          children: [
            const CircleAvatar(
              radius: 50,
              backgroundColor: AppColors.secondary,
              child: Icon(
                Icons.person,
                size: 50,
                color: Colors.white,
              ),
            ),

            const SizedBox(height: 20),

            Text(
              AppState.instance.usuarioLogado?.nome ?? 'Nome do usuário',
              style: TextStyle(
                fontSize: 22,
                fontWeight: FontWeight.bold,
                color: AppColors.primary,
              ),
            ),

            const SizedBox(height: 10),

            Text(
              AppState.instance.usuarioLogado?.email ?? 'email@exemplo.com',
            ),

            const SizedBox(height: 30),

            ListTile(
              leading: const Icon(Icons.edit, color: AppColors.primary),
              title: const Text('Editar perfil'),
              trailing: const Icon(Icons.arrow_forward_ios),
              onTap: _editarPerfil,
            ),

            ListTile(
              leading: const Icon(Icons.bookmark, color: AppColors.pink),
              title: const Text('Eventos salvos'),
              trailing: const Icon(Icons.arrow_forward_ios),
              onTap: () =>
                  Navigator.pushNamed(context, AppRoutes.eventosSalvos),
            ),

            ListTile(
              leading: const Icon(Icons.notifications, color: AppColors.orange),
              title: const Text('Notificações'),
              trailing: const Icon(Icons.arrow_forward_ios),
              onTap: () =>
                  Navigator.pushNamed(context, AppRoutes.notificacoes),
            ),

            ListTile(
              leading: const Icon(
                Icons.interests,
                color: AppColors.secondary,
              ),
              title: const Text('Preferências de temáticas'),
              trailing: const Icon(Icons.arrow_forward_ios),
              onTap: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (_) => const TematicasScreen(
                      selecionarPreferencias: true,
                    ),
                  ),
                );
              },
            ),

            ListTile(
              leading: const Icon(Icons.logout, color: AppColors.orangeDark),
              title: const Text('Sair'),
              onTap: () {
                AppState.instance.logout();
                Navigator.pushNamedAndRemoveUntil(
                  context,
                  AppRoutes.login,
                  (_) => false,
                );
              },
            ),
          ],
        ),
      ),
    );
  }
}
