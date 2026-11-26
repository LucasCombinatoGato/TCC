import 'package:flutter/material.dart';
import 'utils/constants.dart';
import 'utils/app_routes.dart';
import 'screens/login_screen.dart';
import 'screens/cadastro_screen.dart';
import 'screens/home_screen.dart';
import 'screens/eventos_screen.dart';
import 'screens/tematicas_screen.dart';
import 'screens/detalhes_evento_screen.dart';
import 'screens/eventos_salvos_screen.dart';
import 'screens/perfil_screen.dart';
import 'screens/notificacoes_screen.dart';
import 'models/evento.dart';
import 'widgets/bottom_navigation_bar.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Plataforma de Eventos',
      theme: ThemeData(
        useMaterial3: true,
        scaffoldBackgroundColor: AppColors.background,
        colorScheme: ColorScheme.fromSeed(
          seedColor: AppColors.primary,
          primary: AppColors.primary,
          secondary: AppColors.secondary,
          surface: AppColors.background,
        ),
        appBarTheme: const AppBarTheme(
          backgroundColor: AppColors.primary,
          foregroundColor: Colors.white,
        ),
        elevatedButtonTheme: ElevatedButtonThemeData(
          style: ElevatedButton.styleFrom(
            backgroundColor: AppColors.primary,
            foregroundColor: Colors.white,
          ),
        ),
      ),
      initialRoute: AppRoutes.login,
      routes: {
        AppRoutes.login: (_) => LoginScreen(),
        AppRoutes.cadastro: (_) => CadastroScreen(),
        AppRoutes.home: (_) => const MainShell(),
        AppRoutes.tematicas: (_) => const TematicasScreen(),
        AppRoutes.eventos: (_) => const EventosScreen(),
        AppRoutes.eventosSalvos: (_) => const EventosSalvosScreen(),
        AppRoutes.perfil: (_) => const PerfilScreen(),
        AppRoutes.notificacoes: (_) => const NotificacoesScreen(),
      },
      onGenerateRoute: (settings) {
        if (settings.name == AppRoutes.detalhesEvento) {
          final evento = settings.arguments as Evento;

          return MaterialPageRoute(
            builder: (_) => DetalhesEventoScreen(evento: evento),
          );
        }

        return null;
      },
    );
  }
}

class MainShell extends StatefulWidget {
  const MainShell({super.key});

  @override
  State<MainShell> createState() => _MainShellState();
}

class _MainShellState extends State<MainShell> {
  int _selectedIndex = 0;

  @override
  Widget build(BuildContext context) {
    final pages = [
      const HomeScreen(),
      const EventosScreen(showBottomNavigation: false),
      const EventosSalvosScreen(showBottomNavigation: false),
      const PerfilScreen(showBottomNavigation: false),
    ];

    return Scaffold(
      body: IndexedStack(
        index: _selectedIndex,
        children: pages,
      ),
      bottomNavigationBar: CustomBottomNavigationBar(
        selectedIndex: _selectedIndex,
        onTap: (index) => setState(() => _selectedIndex = index),
      ),
    );
  }
}