
import 'package:flutter/material.dart';
import '../widgets/custom_text_field.dart';
import '../widgets/custom_button.dart';
import '../utils/constants.dart';
import '../utils/app_routes.dart';
import '../app_state.dart';

class CadastroScreen extends StatefulWidget {
  const CadastroScreen({super.key});

  @override
  State<CadastroScreen> createState() => _CadastroScreenState();
}

class _CadastroScreenState extends State<CadastroScreen> {

  final TextEditingController nomeController = TextEditingController();
  final TextEditingController emailController = TextEditingController();
  final TextEditingController senhaController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Cadastro'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(24),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            CustomTextField(
              label: 'Nome',
              hint: 'Digite seu nome',
              controller: nomeController,
              icon: Icons.person,
            ),

            const SizedBox(height: 20),

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
              text: 'Cadastrar',
              onPressed: () {
                final ok = AppState.instance.cadastrar(
                  nomeController.text,
                  emailController.text,
                  senhaController.text,
                );
                if (!ok) {
                  ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(
                      content: Text(
                        'Preencha os dados corretamente. A senha deve ter 6 caracteres.',
                      ),
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
              color: AppColors.orange,
              textColor: Colors.black,
            ),
          ],
        ),
      ),
    );
  }
}
