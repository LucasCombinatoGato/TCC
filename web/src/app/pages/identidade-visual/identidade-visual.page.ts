import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CAMORA_DEFAULT_IDENTITY, IdentidadeVisualDTO } from '../../models/camora.models';
import { AuthService } from '../../services/auth.service';
import { ProdutorService } from '../../services/produtor.service';
import { OngService } from '../../services/ong.service';
import { ThemeService } from '../../services/theme.service';
import { ToastService } from '../../services/toast.service';
import { SeletorIdentidadeVisualComponent } from '../../components/seletor-identidade-visual/seletor-identidade-visual.component';

@Component({
  selector: 'app-identidade-visual-page',
  standalone: true,
  imports: [CommonModule, RouterLink, SeletorIdentidadeVisualComponent],
  templateUrl: './identidade-visual.page.html',
  styleUrl: './identidade-visual.page.scss'
})
export class IdentidadeVisualPage implements OnInit {
  readonly auth = inject(AuthService);
  private readonly produtorService = inject(ProdutorService);
  private readonly ongService = inject(OngService);
  private readonly theme = inject(ThemeService);
  private readonly toast = inject(ToastService);

  readonly loading = signal(true);
  readonly saving = signal(false);
  readonly identidade = signal<IdentidadeVisualDTO>({ ...CAMORA_DEFAULT_IDENTITY });

  ngOnInit(): void {
    const req$ = this.auth.userRole() === 'ROLE_ONG'
      ? this.ongService.meuPerfil()
      : this.produtorService.meuPerfil();

    req$.subscribe({
      next: perfil => {
        const iv = perfil.identidadeVisual || { ...CAMORA_DEFAULT_IDENTITY };
        this.identidade.set(iv);
        this.theme.applyIdentity(iv);
        this.loading.set(false);
      },
      error: () => this.loading.set(false)
    });
  }

  aoAlterarPreview(iv: IdentidadeVisualDTO): void {
    this.identidade.set(iv);
    this.theme.applyIdentity(iv);
  }

  salvar(iv: IdentidadeVisualDTO): void {
    this.saving.set(true);
    const req$ = this.auth.userRole() === 'ROLE_ONG'
      ? this.ongService.atualizarIdentidadeVisual(iv)
      : this.produtorService.atualizarIdentidadeVisual(iv);

    req$.subscribe({
      next: updated => {
        this.saving.set(false);
        this.identidade.set(updated);
        this.theme.applyIdentity(updated);
        this.auth.atualizarIdentidadeSessao(updated);
        this.toast.success('Identidade visual White-Label aplicada com sucesso!');
      },
      error: () => {
        this.saving.set(false);
        this.toast.error('Erro ao salvar identidade visual.');
      }
    });
  }

  restaurarPadrao(): void {
    this.saving.set(true);
    const req$ = this.auth.userRole() === 'ROLE_ONG'
      ? this.ongService.restaurarIdentidadePadrao()
      : this.produtorService.restaurarIdentidadePadrao();

    req$.subscribe({
      next: updated => {
        this.saving.set(false);
        this.identidade.set(updated);
        this.theme.applyIdentity(updated);
        this.auth.atualizarIdentidadeSessao(updated);
        this.toast.success('Identidade padrão Camora restaurada!');
      },
      error: () => {
        this.saving.set(false);
        this.toast.error('Erro ao restaurar identidade padrão.');
      }
    });
  }
}
