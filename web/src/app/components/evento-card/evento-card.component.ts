import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { EventoResponse } from '../../models/camora.models';

@Component({
  selector: 'app-evento-card',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './evento-card.component.html',
  styleUrl: './evento-card.component.scss'
})
export class EventoCardComponent {
  @Input({ required: true }) evento!: EventoResponse;
  @Input() showSaveButton = false;
  @Output() toggleSalvar = new EventEmitter<EventoResponse>();

  get accentColor(): string {
    return this.evento.tematica?.corHex || this.evento.identidadeVisual?.corDestaque || '#FF930A';
  }

  get coverBackground(): string {
    const c1 = this.evento.identidadeVisual?.corPrincipal || '#471CA8';
    const c2 = this.evento.identidadeVisual?.corSecundaria || '#884AB2';
    return `linear-gradient(135deg, ${c1} 0%, ${c2} 65%, ${this.accentColor} 100%)`;
  }

  get precoResumo(): string {
    if (this.evento.gratuito) return 'Gratuito';
    if (!this.evento.tiposIngresso?.length) return 'Consultar';
    const min = Math.min(...this.evento.tiposIngresso.map(t => Number(t.valor || 0)));
    return min === 0 ? 'Gratuito' : `A partir de R$ ${min.toFixed(2).replace('.', ',')}`;
  }
}
