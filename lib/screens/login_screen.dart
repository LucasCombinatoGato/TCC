
import 'package:flutter/material.dart';
import '../widgets/custom_text_field.dart';
import '../widgets/custom_button.dart';
import '../utils/constants.dart';
import '../utils/app_routes.dart';
import '../app_state.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {

  final TextEditingController emailController = TextEditingController();
  final TextEditingController senhaController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(24),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const Icon(
                Icons.event,
                size: 80,
                color: AppColors.secondary,
              ),

              const SizedBox(height: 20),

              const Text(
                'Plataforma de Eventos',
                style: TextStyle(
                  fontSize: 28,
                  fontWeight: FontWeight.bold,
                  color: AppColors.primary,
                ),
              ),

              const SizedBox(height: 40),

              CustomTextField(
                label: 'E-mail',
                hint: 'Digite seu e-mail',
                controller: emailController,
                keyboardType: TextInputType.emailAddress,
                icon: Icons.email,
              ),

              const SizedBox(height: 20),

              CustomTextField(
                label: 'Senha',
                hint: 'Digite sua senha',
                controller: senhaController,
                obscureText: true,
                icon: Icons.lock,
              ),

              const SizedBox(height: 30),

              CustomButton(
                text: 'Entrar',
                onPressed: () {
                  final ok = AppState.instance.login(
                    emailController.text,
                    senhaController.text,
                  );
                  if (!ok) {
                    ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(
                        content: Text('E-mail ou senha incorretos.'),
                      ),
                    );
                    return;
                  }
                  Navigator.pushNamedAndRemoveUntil(
                    context,
                    AppRoutes.home,
                    (_) => false,
                  );
                },
              ),

              const SizedBox(height: 15),

              TextButton(
                onPressed: () {
                  Navigator.pushNamed(context, AppRoutes.cadastro);
                },
                child: const Text(
                  'Criar conta',
                  style: TextStyle(
                    color: AppColors.secondary,
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
