import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CAMORA_DEFAULT_IDENTITY, IdentidadeVisualDTO } from '../../models/camora.models';

@Component({
  selector: 'app-seletor-identidade-visual',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './seletor-identidade-visual.component.html',
  styleUrl: './seletor-identidade-visual.component.scss'
})
export class SeletorIdentidadeVisualComponent implements OnChanges {
  @Input() identidade: IdentidadeVisualDTO = { ...CAMORA_DEFAULT_IDENTITY };
  @Input() organizacaoNome = 'Sua Organização';
  @Input() showSaveButton = true;
  @Input() saving = false;

  @Output() identidadeChange = new EventEmitter<IdentidadeVisualDTO>();
  @Output() salvar = new EventEmitter<IdentidadeVisualDTO>();
  @Output() restaurarPadrao = new EventEmitter<void>();

  model: IdentidadeVisualDTO = { ...CAMORA_DEFAULT_IDENTITY };

  readonly presets: Array<{ nome: string; principal: string; secundaria: string; destaque: string }> = [
    { nome: 'Camora Oficial', principal: '#471CA8', secundaria: '#884AB2', destaque: '#FF930A' },
    { nome: 'Festival Neon', principal: '#8338EC', secundaria: '#FF006E', destaque: '#FFBE0B' },
    { nome: 'Impacto Social', principal: '#1E3A8A', secundaria: '#3A86FF', destaque: '#FB5607' },
    { nome: 'Energia Solar', principal: '#5F0F40', secundaria: '#D1105A', destaque: '#FF930A' }
  ];

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['identidade'] && this.identidade) {
      this.model = {
        ...CAMORA_DEFAULT_IDENTITY,
        ...this.identidade
      };
    }
  }

  onTogglePadrao(usarPadrao: boolean): void {
    if (usarPadrao) {
      this.model = {
        ...this.model,
        usarIdentidadePadrao: true,
        corPrincipal: CAMORA_DEFAULT_IDENTITY.corPrincipal,
        corSecundaria: CAMORA_DEFAULT_IDENTITY.corSecundaria,
        corDestaque: CAMORA_DEFAULT_IDENTITY.corDestaque
      };
    } else {
      this.model = { ...this.model, usarIdentidadePadrao: false };
    }
    this.emitChange();
  }

  aplicarPreset(preset: { principal: string; secundaria: string; destaque: string }): void {
    this.model = {
      ...this.model,
      usarIdentidadePadrao: false,
      corPrincipal: preset.principal,
      corSecundaria: preset.secundaria,
      corDestaque: preset.destaque
    };
    this.emitChange();
  }

  onLogoUpload(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;
    const reader = new FileReader();
    reader.onload = () => {
      this.model.logo = String(reader.result || '');
      this.emitChange();
    };
    reader.readAsDataURL(file);
  }

  emitChange(): void {
    this.identidadeChange.emit({ ...this.model });
  }

  onSubmit(): void {
    this.salvar.emit({ ...this.model });
  }
}
