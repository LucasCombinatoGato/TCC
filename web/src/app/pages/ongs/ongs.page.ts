import { Component, OnDestroy, OnInit, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { EventoResponse, OngResponse } from '../../models/camora.models';
import { OngService } from '../../services/ong.service';
import { EventoService } from '../../services/evento.service';
import { ThemeService } from '../../services/theme.service';
import { OngCardComponent } from '../../components/ong-card/ong-card.component';
import { EventoCardComponent } from '../../components/evento-card/evento-card.component';

@Component({
  selector: 'app-ongs-page',
  standalone: true,
  imports: [CommonModule, FormsModule, OngCardComponent, EventoCardComponent],
  templateUrl: './ongs.page.html',
  styleUrl: './ongs.page.scss'
})
export class OngsPage implements OnInit, OnDestroy {
  private readonly ongService = inject(OngService);
  private readonly eventoService = inject(EventoService);
  private readonly theme = inject(ThemeService);
  private readonly route = inject(ActivatedRoute);

  readonly loading = signal(true);
  readonly ongs = signal<OngResponse[]>([]);
  readonly ongSelecionada = signal<OngResponse | null>(null);
  readonly acoesOng = signal<EventoResponse[]>([]);
  readonly loadingAcoes = signal(false);

  termoBusca = '';
  readonly ongsFiltradas = computed(() => {
    const q = this.termoBusca.trim().toLowerCase();
    if (!q) return this.ongs();
    return this.ongs().filter(o =>
      o.nome.toLowerCase().includes(q)
      || (o.descricao || '').toLowerCase().includes(q)
      || (o.local?.cidade || '').toLowerCase().includes(q)
    );
  });

  ngOnInit(): void {
    this.theme.resetToDefault();
    this.ongService.listarTodas().subscribe({
      next: list => {
        this.ongs.set(list);
        this.loading.set(false);
        const ongId = Number(this.route.snapshot.queryParamMap.get('ongId'));
        if (ongId) {
          const found = list.find(o => o.id === ongId);
          if (found) this.selecionarOng(found);
        }
      },
      error: () => this.loading.set(false)
    });
  }

  ngOnDestroy(): void {
    this.theme.resetToDefault();
  }

  selecionarOng(ong: OngResponse): void {
    this.ongSelecionada.set(ong);
    this.theme.applyIdentity(ong.identidadeVisual);
    this.loadingAcoes.set(true);
    this.eventoService.listarPublicos({ ongId: ong.id }).subscribe({
      next: acoes => {
        this.acoesOng.set(acoes);
        this.loadingAcoes.set(false);
      },
      error: () => this.loadingAcoes.set(false)
    });
  }

  voltarLista(): void {
    this.ongSelecionada.set(null);
    this.acoesOng.set([]);
    this.theme.resetToDefault();
  }
}
