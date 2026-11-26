import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { ToastService } from '../../services/toast.service';
import { ThemeService } from '../../services/theme.service';
import { Role } from '../../models/camora.models';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.page.html',
  styleUrl: './login.page.scss'
})
export class LoginPage {
  private readonly auth = inject(AuthService);
  private readonly toast = inject(ToastService);
  private readonly theme = inject(ThemeService);
  private readonly router = inject(Router);

  email = '';
  senha = '';
  tipoPerfil: Role | '' = '';
  readonly loading = signal(false);

  entrar(): void {
    if (!this.email.trim() || !this.senha) {
      this.toast.error('Informe seu e-mail e senha.');
      return;
    }

    this.loading.set(true);
    this.auth.login({
      email: this.email.trim(),
      senha: this.senha,
      tipoPerfil: this.tipoPerfil || null
    }).subscribe({
      next: res => {
        this.loading.set(false);
        if (res.identidadeVisual) {
          this.theme.applyIdentity(res.identidadeVisual);
        }
        this.toast.success(`Bem-vindo(a), ${res.nome}!`);
        if (res.role === 'ROLE_PRODUCER' || res.role === 'ROLE_ONG') {
          this.router.navigate(['/area-criador']);
        } else {
          this.router.navigate(['/eventos']);
        }
      },
      error: err => {
        this.loading.set(false);
        this.toast.error(err?.error?.mensagem || 'E-mail ou senha inválidos.');
      }
    });
  }

  preencherDemo(email: string, role: Role): void {
    this.email = email;
    this.senha = 'Camora123!';
    this.tipoPerfil = role;
  }
}
