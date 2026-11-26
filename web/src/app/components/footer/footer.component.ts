import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [RouterLink],
  template: `
    <footer class="camora-footer">
      <div class="container footer-grid">
        <div class="footer-brand">
          <div class="brand-row">
            <img src="assets/camora-chameleon.svg" alt="Mascote Camaleão Camora" width="42" height="42">
            <span class="font-display brand-name">CAMORA</span>
          </div>
          <p>
            Diferentes cores, formas e experiências em constante descoberta.
            Conectando público, produtores independentes e ONGs em um único ecossistema.
          </p>
        </div>

        <div class="footer-col">
          <h4>Explorar</h4>
          <a routerLink="/eventos">Vitrine de Eventos</a>
          <a routerLink="/ongs">ONGs & Ações Sociais</a>
          <a routerLink="/cadastro">Criar Conta</a>
        </div>

        <div class="footer-col">
          <h4>Organizadores</h4>
          <a routerLink="/area-criador">Área do Criador</a>
          <a routerLink="/identidade-visual">Identidade White-Label</a>
          <a routerLink="/perfil-produtor">Perfil da Organização</a>
        </div>
      </div>

      <div class="footer-bottom">
        <div class="container bottom-inner">
          <span>Camora &copy; 2026 — Descoberta, Divulgação e Organização de Eventos e Ações Sociais</span>
          <span class="palette-dots">
            <i style="background:#471CA8"></i>
            <i style="background:#884AB2"></i>
            <i style="background:#FF930A"></i>
            <i style="background:#F24B04"></i>
            <i style="background:#D1105A"></i>
          </span>
        </div>
      </div>
    </footer>
  `,
  styles: [`
    .camora-footer {
      background: #1B0A45;
      color: rgba(255, 255, 255, 0.88);
      margin-top: 72px;
      border-top: 4px solid var(--camora-accent);
    }
    .footer-grid {
      display: grid;
      grid-template-columns: 2fr 1fr 1fr;
      gap: 32px;
      padding: 48px 0 36px;

      @media (max-width: 768px) {
        grid-template-columns: 1fr;
      }
    }
    .brand-row {
      display: flex;
      align-items: center;
      gap: 12px;
      margin-bottom: 12px;
    }
    .brand-name {
      font-size: 2rem;
      color: #fff;
      letter-spacing: 0.08em;
    }
    .footer-brand p {
      margin: 0;
      max-width: 420px;
      color: rgba(255, 255, 255, 0.72);
      font-size: 0.92rem;
    }
    .footer-col {
      display: flex;
      flex-direction: column;
      gap: 9px;

      h4 {
        margin: 0 0 6px;
        font-size: 0.9rem;
        text-transform: uppercase;
        letter-spacing: 0.08em;
        color: #FFBE0B;
      }
      a {
        color: rgba(255, 255, 255, 0.78);
        font-size: 0.92rem;
        transition: color 0.15s ease;
        &:hover { color: #FF930A; }
      }
    }
    .footer-bottom {
      background: rgba(0, 0, 0, 0.24);
      padding: 16px 0;
      font-size: 0.83rem;
      color: rgba(255, 255, 255, 0.65);
    }
    .bottom-inner {
      display: flex;
      justify-content: space-between;
      align-items: center;
      gap: 12px;
      flex-wrap: wrap;
    }
    .palette-dots {
      display: flex;
      gap: 6px;
      i {
        display: inline-block;
        width: 11px;
        height: 11px;
        border-radius: 50%;
      }
    }
  `]
})
export class FooterComponent {}
