import { Component, OnDestroy, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { EventoResponse } from '../../models/camora.models';
import { EventoService } from '../../services/evento.service';
import { UsuarioService } from '../../services/usuario.service';
import { AuthService } from '../../services/auth.service';
import { ThemeService } from '../../services/theme.service';
import { ToastService } from '../../services/toast.service';

@Component({
  selector: 'app-detalhes-evento-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './detalhes-evento.page.html',
  styleUrl: './detalhes-evento.page.scss'
})
export class DetalhesEventoPage implements OnInit, OnDestroy {
  private readonly route = inject(ActivatedRoute);
  private readonly eventoService = inject(EventoService);
  private readonly usuarioService = inject(UsuarioService);
  readonly auth = inject(AuthService);
  private readonly theme = inject(ThemeService);
  private readonly toast = inject(ToastService);

  readonly loading = signal(true);
  readonly evento = signal<EventoResponse | null>(null);

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.eventoService.buscarPorId(id).subscribe({
      next: ev => {
        this.evento.set(ev);
        this.theme.applyIdentity(ev.identidadeVisual);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.toast.error('Evento não encontrado ou indisponível.');
      }
    });
  }

  ngOnDestroy(): void {
    this.theme.resetToDefault();
  }

  alternarSalvar(): void {
    const ev = this.evento();
    if (!ev) return;

    if (this.auth.userRole() !== 'ROLE_USER') {
      this.toast.error('Faça login como Usuário para salvar eventos.');
      return;
    }

    if (ev.salvoPeloUsuario) {
      this.usuarioService.removerEventoSalvo(ev.id).subscribe({
        next: () => {
          this.evento.set({ ...ev, salvoPeloUsuario: false });
          this.toast.show('Evento removido dos salvos.', 'info');
        }
      });
    } else {
      this.usuarioService.salvarEvento(ev.id).subscribe({
        next: () => {
          this.evento.set({ ...ev, salvoPeloUsuario: true });
          this.toast.success('Evento salvo para acompanhamento!');
        }
      });
    }
  }
}
