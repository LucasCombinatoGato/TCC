import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { EventoResponse, OngResponse, TematicaDTO, TipoAmbito } from '../../models/camora.models';
import { EventoService } from '../../services/evento.service';
import { TematicaService } from '../../services/tematica.service';
import { AuthService } from '../../services/auth.service';
import { OngService } from '../../services/ong.service';
import { ProdutorService } from '../../services/produtor.service';
import { ThemeService } from '../../services/theme.service';
import { ToastService } from '../../services/toast.service';
import { ModalEventoComponent } from '../../components/modal-evento/modal-evento.component';
import { ModalOngComponent } from '../../components/modal-ong/modal-ong.component';

@Component({
  selector: 'app-area-criador-page',
  standalone: true,
  imports: [CommonModule, RouterLink, ModalEventoComponent, ModalOngComponent],
  templateUrl: './area-criador.page.html',
  styleUrl: './area-criador.page.scss'
})
export class AreaCriadorPage implements OnInit {
  private readonly eventoService = inject(EventoService);
  private readonly tematicaService = inject(TematicaService);
  readonly auth = inject(AuthService);
  private readonly ongService = inject(OngService);
  private readonly produtorService = inject(ProdutorService);
  private readonly theme = inject(ThemeService);
  private readonly toast = inject(ToastService);

  readonly loading = signal(true);
  readonly eventos = signal<EventoResponse[]>([]);
  readonly tematicas = signal<TematicaDTO[]>([]);
  readonly ongPerfil = signal<OngResponse | null>(null);

  readonly modalEventoAberto = signal(false);
  readonly eventoEmEdicao = signal<EventoResponse | null>(null);
  readonly ambitoModal = signal<TipoAmbito>('EVENTO');
  readonly modalOngAberto = signal(false);

  readonly totalPublicados = computed(() => this.eventos().filter(e => e.status === 'PUBLICADO').length);
  readonly totalRascunhos = computed(() => this.eventos().filter(e => e.status === 'RASCUNHO').length);
  readonly totalCancelados = computed(() => this.eventos().filter(e => e.status === 'CANCELADO').length);

  ngOnInit(): void {
    this.carregarDados();
  }

  carregarDados(): void {
    this.loading.set(true);
    this.tematicaService.listarTodas().subscribe({
      next: list => this.tematicas.set(list),
      error: () => {}
    });

    this.eventoService.listarMeusEventos().subscribe({
      next: list => {
        this.eventos.set(list);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.toast.error('Erro ao carregar seus projetos.');
      }
    });

    if (this.auth.userRole() === 'ROLE_ONG') {
      this.ongService.meuPerfil().subscribe({
        next: ong => {
          this.ongPerfil.set(ong);
          this.theme.applyIdentity(ong.identidadeVisual);
        },
        error: () => {}
      });
    } else if (this.auth.userRole() === 'ROLE_PRODUCER') {
      this.produtorService.meuPerfil().subscribe({
        next: prod => this.theme.applyIdentity(prod.identidadeVisual),
        error: () => {}
      });
    }
  }

  abrirNovo(ambito: TipoAmbito): void {
    this.eventoEmEdicao.set(null);
    this.ambitoModal.set(ambito);
    this.modalEventoAberto.set(true);
  }

  abrirEdicao(evento: EventoResponse): void {
    this.eventoEmEdicao.set(evento);
    this.ambitoModal.set(evento.tipoAmbito);
    this.modalEventoAberto.set(true);
  }

  aoSalvarEvento(salvo: EventoResponse): void {
    this.modalEventoAberto.set(false);
    this.eventoEmEdicao.set(null);
    this.eventos.update(list => {
      const exists = list.some(e => e.id === salvo.id);
      return exists ? list.map(e => e.id === salvo.id ? salvo : e) : [salvo, ...list];
    });
  }

  publicar(ev: EventoResponse): void {
    this.eventoService.publicar(ev.id).subscribe({
      next: updated => {
        this.substituirEvento(updated);
        this.toast.success(`"${updated.nome}" publicado na vitrine!`);
      },
      error: err => this.toast.error(err?.error?.mensagem || 'Falha ao publicar.')
    });
  }

  despublicar(ev: EventoResponse): void {
    this.eventoService.despublicar(ev.id).subscribe({
      next: updated => {
        this.substituirEvento(updated);
        this.toast.show(`"${updated.nome}" movido para rascunho.`, 'info');
      },
      error: err => this.toast.error(err?.error?.mensagem || 'Falha ao despublicar.')
    });
  }

  cancelar(ev: EventoResponse): void {
    this.eventoService.cancelar(ev.id).subscribe({
      next: updated => {
        this.substituirEvento(updated);
        this.toast.show(`"${updated.nome}" cancelado. Seguidores foram notificados.`, 'info');
      },
      error: err => this.toast.error(err?.error?.mensagem || 'Falha ao cancelar.')
    });
  }

  excluir(ev: EventoResponse): void {
    if (!confirm(`Deseja realmente excluir "${ev.nome}"?`)) return;
    this.eventoService.excluir(ev.id).subscribe({
      next: () => {
        this.eventos.update(list => list.filter(e => e.id !== ev.id));
        this.toast.success('Projeto excluído.');
      },
      error: err => this.toast.error(err?.error?.mensagem || 'Erro ao excluir projeto.')
    });
  }

  aoSalvarOng(ong: OngResponse): void {
    this.ongPerfil.set(ong);
    this.modalOngAberto.set(false);
    this.auth.atualizarIdentidadeSessao(ong.identidadeVisual || this.theme.activeIdentity(), ong.nome);
  }

  private substituirEvento(updated: EventoResponse): void {
    this.eventos.update(list => list.map(e => e.id === updated.id ? updated : e));
  }
}
