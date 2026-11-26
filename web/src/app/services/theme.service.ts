import { Injectable, signal } from '@angular/core';
import { CAMORA_DEFAULT_IDENTITY, IdentidadeVisualDTO } from '../models/camora.models';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  readonly activeIdentity = signal<IdentidadeVisualDTO>(CAMORA_DEFAULT_IDENTITY);

  applyIdentity(identity?: IdentidadeVisualDTO | null): void {
    const resolved: IdentidadeVisualDTO = (!identity || identity.usarIdentidadePadrao)
      ? CAMORA_DEFAULT_IDENTITY
      : {
          ...CAMORA_DEFAULT_IDENTITY,
          ...identity,
          corPrincipal: identity.corPrincipal || CAMORA_DEFAULT_IDENTITY.corPrincipal,
          corSecundaria: identity.corSecundaria || CAMORA_DEFAULT_IDENTITY.corSecundaria,
          corDestaque: identity.corDestaque || CAMORA_DEFAULT_IDENTITY.corDestaque,
          usarIdentidadePadrao: false
        };

    this.activeIdentity.set(resolved);
    const root = document.documentElement;
    root.style.setProperty('--camora-primary', resolved.corPrincipal);
    root.style.setProperty('--camora-secondary', resolved.corSecundaria);
    root.style.setProperty('--camora-accent', resolved.corDestaque);
  }

  resetToDefault(): void {
    this.applyIdentity(CAMORA_DEFAULT_IDENTITY);
  }
}
