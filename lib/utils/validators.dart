class Validators {
  static String? validarCampo(String? valor) {
    if (valor == null || valor.isEmpty) {
      return 'Campo obrigatório';
    }

    return null;
  }

  static String? validarEmail(String? valor) {
    if (valor == null || valor.isEmpty) {
      return 'Informe seu e-mail';
    }

    if (!valor.contains('@') || !valor.contains('.')) {
      return 'E-mail inválido';
    }

    return null;
  }

  static String? validarSenha(String? valor) {
    if (valor == null || valor.isEmpty) {
      return 'Informe sua senha';
    }

    if (valor.length < 6) {
      return 'A senha deve ter pelo menos 6 caracteres';
    }

    return null;
  }
}