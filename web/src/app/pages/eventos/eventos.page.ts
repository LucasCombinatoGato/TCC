import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { EventoFiltros, EventoResponse, TematicaDTO, TipoAmbito } from '../../models/camora.models';
import { EventoService } from '../../services/evento.service';
import { TematicaService } from '../../services/tematica.service';
import { UsuarioService } from '../../services/usuario.service';
import { AuthService } from '../../services/auth.service';
import { ToastService } from '../../services/toast.service';
import { ThemeService } from '../../services/theme.service';
import { EventoCardComponent } from '../../components/evento-card/evento-card.component';

@Component({
  selector: 'app-eventos-page',
  standalone: true,
  imports: [CommonModule, FormsModule, EventoCardComponent],
  templateUrl: './eventos.page.html',
  styleUrl: './eventos.page.scss'
})
export class EventosPage implements OnInit {
  private readonly eventoService = inject(EventoService);
  private readonly tematicaService = inject(TematicaService);
  private readonly usuarioService = inject(UsuarioService);
  readonly auth = inject(AuthService);
  private readonly toast = inject(ToastService);
  private readonly theme = inject(ThemeService);
  private readonly route = inject(ActivatedRoute);

  readonly loading = signal(true);
  readonly eventos = signal<EventoResponse[]>([]);
  readonly tematicas = signal<TematicaDTO[]>([]);

  q = '';
  tematicaId: number | null = null;
  cidade = '';
  uf = '';
  gratuito: '' | 'true' | 'false' = '';
  tipoAmbito: TipoAmbito | '' = '';
  dataInicio = '';
  dataFim = '';

  ngOnInit(): void {
    this.theme.resetToDefault();
    this.tematicaService.listarTodas().subscribe({
      next: list => this.tematicas.set(list),
      error: () => {}
    });

    this.route.queryParams.subscribe(params => {
      if (params['q']) this.q = params['q'];
      if (params['tematicaId']) this.tematicaId = Number(params['tematicaId']);
      this.buscar();
    });
  }

  buscar(): void {
    const filtros: EventoFiltros = {
      q: this.q,
      tematicaId: this.tematicaId,
      cidade: this.cidade,
      uf: this.uf,
      gratuito: this.gratuito === '' ? null : this.gratuito === 'true',
      tipoAmbito: this.tipoAmbito || null,
      dataInicio: this.dataInicio || undefined,
      dataFim: this.dataFim || undefined
    };

    this.loading.set(true);
    this.eventoService.listarPublicos(filtros).subscribe({
      next: list => {
        this.eventos.set(list);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.toast.error('Erro ao carregar eventos.');
      }
    });
  }

  limparFiltros(): void {
    this.q = '';
    this.tematicaId = null;
    this.cidade = '';
    this.uf = '';
    this.gratuito = '';
    this.tipoAmbito = '';
    this.dataInicio = '';
    this.dataFim = '';
    this.buscar();
  }

  alternarSalvar(evento: EventoResponse): void {
    if (this.auth.userRole() !== 'ROLE_USER') {
      this.toast.error('Entre como Usuário para salvar eventos.');
      return;
    }

    if (evento.salvoPeloUsuario) {
      this.usuarioService.removerEventoSalvo(evento.id).subscribe({
        next: () => {
          this.eventos.update(list => list.map(e => e.id === evento.id ? { ...e, salvoPeloUsuario: false } : e));
          this.toast.show('Evento removido dos salvos.', 'info');
        }
      });
    } else {
      this.usuarioService.salvarEvento(evento.id).subscribe({
        next: () => {
          this.eventos.update(list => list.map(e => e.id === evento.id ? { ...e, salvoPeloUsuario: true } : e));
          this.toast.success('Evento salvo! Você receberá avisos se houver alterações.');
        }
      });
    }
  }
}
