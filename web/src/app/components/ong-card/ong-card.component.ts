import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OngResponse } from '../../models/camora.models';

@Component({
  selector: 'app-ong-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './ong-card.component.html',
  styleUrl: './ong-card.component.scss'
})
export class OngCardComponent {
  @Input({ required: true }) ong!: OngResponse;
  @Output() selecionar = new EventEmitter<OngResponse>();

  get primaryColor(): string {
    return this.ong.identidadeVisual?.usarIdentidadePadrao === false
      ? (this.ong.identidadeVisual?.corPrincipal || '#471CA8')
      : '#471CA8';
  }

  get secondaryColor(): string {
    return this.ong.identidadeVisual?.usarIdentidadePadrao === false
      ? (this.ong.identidadeVisual?.corSecundaria || '#884AB2')
      : '#884AB2';
  }

  get accentColor(): string {
    return this.ong.identidadeVisual?.usarIdentidadePadrao === false
      ? (this.ong.identidadeVisual?.corDestaque || '#FF930A')
      : '#FF930A';
  }

  get initials(): string {
    return (this.ong.nome || 'ON')
      .split(' ')
      .slice(0, 2)
      .map(p => p[0])
      .join('')
      .toUpperCase();
  }
}
