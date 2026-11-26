import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { NotificacaoService } from '../../services/notificacao.service';
import { NotificacaoResponse } from '../../models/camora.models';
import { ThemeService } from '../../services/theme.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent implements OnInit {
  readonly auth = inject(AuthService);
  readonly theme = inject(ThemeService);
  private readonly notificacaoService = inject(NotificacaoService);

  readonly menuOpen = signal(false);
  readonly notifOpen = signal(false);
  readonly notificacoes = signal<NotificacaoResponse[]>([]);
  readonly naoLidas = signal(0);

  readonly roleBadge = computed(() => {
    switch (this.auth.userRole()) {
      case 'ROLE_PRODUCER': return 'Produtor';
      case 'ROLE_ONG': return 'ONG';
      case 'ROLE_USER': return 'Explorador';
      default: return '';
    }
  });

  ngOnInit(): void {
    if (this.auth.userRole() === 'ROLE_USER') {
      this.carregarNotificacoes();
    }
  }

  toggleNotif(): void {
    const next = !this.notifOpen();
    this.notifOpen.set(next);
    if (next) {
      this.carregarNotificacoes();
    }
  }

  carregarNotificacoes(): void {
    if (this.auth.userRole() !== 'ROLE_USER') return;
    this.notificacaoService.listar().subscribe({
      next: list => {
        this.notificacoes.set(list);
        this.naoLidas.set(list.filter(n => !n.lida).length);
      },
      error: () => {}
    });
  }

  marcarLida(n: NotificacaoResponse): void {
    if (n.lida) return;
    this.notificacaoService.marcarComoLida(n.id).subscribe({
      next: updated => {
        this.notificacoes.update(list => list.map(item => item.id === updated.id ? updated : item));
        this.naoLidas.update(c => Math.max(0, c - 1));
      }
    });
  }

  marcarTodas(): void {
    this.notificacaoService.marcarTodasComoLidas().subscribe({
      next: () => {
        this.notificacoes.update(list => list.map(n => ({ ...n, lida: true })));
        this.naoLidas.set(0);
      }
    });
  }

  logout(): void {
    this.menuOpen.set(false);
    this.notifOpen.set(false);
    this.theme.resetToDefault();
    this.auth.logout();
  }
}
