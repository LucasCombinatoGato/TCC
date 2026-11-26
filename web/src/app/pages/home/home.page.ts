import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { EventoResponse, OngResponse, TematicaDTO } from '../../models/camora.models';
import { EventoService } from '../../services/evento.service';
import { TematicaService } from '../../services/tematica.service';
import { OngService } from '../../services/ong.service';
import { ThemeService } from '../../services/theme.service';
import { EventoCardComponent } from '../../components/evento-card/evento-card.component';
import { OngCardComponent } from '../../components/ong-card/ong-card.component';

@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, EventoCardComponent, OngCardComponent],
  templateUrl: './home.page.html',
  styleUrl: './home.page.scss'
})
export class HomePage implements OnInit {
  private readonly eventoService = inject(EventoService);
  private readonly tematicaService = inject(TematicaService);
  private readonly ongService = inject(OngService);
  private readonly theme = inject(ThemeService);
  private readonly router = inject(Router);

  readonly loading = signal(true);
  readonly tematicas = signal<TematicaDTO[]>([]);
  readonly eventosDestaque = signal<EventoResponse[]>([]);
  readonly ongsDestaque = signal<OngResponse[]>([]);

  buscaRapida = '';

  ngOnInit(): void {
    this.theme.resetToDefault();
    this.tematicaService.listarTodas().subscribe({
      next: list => this.tematicas.set(list),
      error: () => {}
    });

    this.eventoService.listarPublicos().subscribe({
      next: list => {
        this.eventosDestaque.set(list.slice(0, 6));
        this.loading.set(false);
      },
      error: () => this.loading.set(false)
    });

    this.ongService.listarTodas().subscribe({
      next: list => this.ongsDestaque.set(list.slice(0, 3)),
      error: () => {}
    });
  }

  pesquisar(): void {
    this.router.navigate(['/eventos'], {
      queryParams: this.buscaRapida.trim() ? { q: this.buscaRapida.trim() } : {}
    });
  }

  irParaTematica(tematicaId: number): void {
    this.router.navigate(['/eventos'], { queryParams: { tematicaId } });
  }

  irParaOng(ong: OngResponse): void {
    this.router.navigate(['/ongs'], { queryParams: { ongId: ong.id } });
  }
}
