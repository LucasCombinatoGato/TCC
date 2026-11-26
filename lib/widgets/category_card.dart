import 'package:flutter/material.dart';
import '../utils/constants.dart';

class CategoryCard extends StatelessWidget {
  final String nome;
  final IconData icone;
  final VoidCallback onTap;

  const CategoryCard({
    super.key,
    required this.nome,
    required this.icone,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return Card(
      color: AppColors.secondary.withValues(alpha: 0.1),
      child: ListTile(
        leading: Icon(
          icone,
          color: AppColors.primary,
        ),
        title: Text(nome),
        onTap: onTap,
      ),
    );
  }
}