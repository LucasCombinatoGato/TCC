import { Component, EventEmitter, Input, OnInit, Output, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OngResponse, OngUpdateRequest } from '../../models/camora.models';
import { OngService } from '../../services/ong.service';
import { ToastService } from '../../services/toast.service';

@Component({
  selector: 'app-modal-ong',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="modal-backdrop" (click)="fechar.emit()">
      <div class="modal-panel ong-modal" (click)="$event.stopPropagation()">
        <div class="modal-header">
          <div>
            <span class="badge modal-tag">Perfil Institucional ONG</span>
            <h2 class="font-display">Atualizar Dados da ONG</h2>
          </div>
          <button type="button" class="close-btn" (click)="fechar.emit()">&times;</button>
        </div>

        <form class="modal-body" (ngSubmit)="submit()">
          <div class="form-group">
            <label>Nome da Organização *</label>
            <input type="text" [(ngModel)]="nome" name="nome" required />
          </div>

          <div class="form-group">
            <label>Descrição Institucional & Causas</label>
            <textarea [(ngModel)]="descricao" name="descricao" rows="3"></textarea>
          </div>

          <div class="form-grid-2">
            <div class="form-group">
              <label>E-mail de Contato</label>
              <input type="email" [(ngModel)]="contatoEmail" name="contatoEmail" />
            </div>
            <div class="form-group">
              <label>Telefone / WhatsApp</label>
              <input type="text" [(ngModel)]="telefone" name="telefone" />
            </div>
          </div>

          <div class="form-grid-2">
            <div class="form-group">
              <label>Redes Sociais</label>
              <input type="text" [(ngModel)]="redesSociais" name="redesSociais" placeholder="@suaong" />
            </div>
            <div class="form-group">
              <label>Site Oficial</label>
              <input type="url" [(ngModel)]="site" name="site" placeholder="https://..." />
            </div>
          </div>

          <div class="form-grid-3">
            <div class="form-group">
              <label>Cidade</label>
              <input type="text" [(ngModel)]="cidade" name="cidade" />
            </div>
            <div class="form-group">
              <label>UF</label>
              <input type="text" maxlength="2" [(ngModel)]="uf" name="uf" />
            </div>
            <div class="form-group">
              <label>CEP</label>
              <input type="text" [(ngModel)]="cep" name="cep" />
            </div>
          </div>

          <div class="form-grid-2">
            <div class="form-group">
              <label>Logradouro</label>
              <input type="text" [(ngModel)]="logradouro" name="logradouro" />
            </div>
            <div class="form-group">
              <label>Bairro</label>
              <input type="text" [(ngModel)]="bairro" name="bairro" />
            </div>
          </div>

          <div class="modal-footer">
            <button type="button" class="btn btn-outline" (click)="fechar.emit()">Cancelar</button>
            <button type="submit" class="btn btn-primary" [disabled]="saving()">
              {{ saving() ? 'Salvando...' : 'Salvar Dados da ONG' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  `,
  styles: [`
    .ong-modal { width: min(680px, 100%); }
    .modal-header {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      padding: 20px 24px;
      background: linear-gradient(135deg, var(--camora-primary), var(--camora-secondary));
      color: #fff;
      border-radius: var(--radius-lg) var(--radius-lg) 0 0;
      h2 { font-size: 1.9rem; margin-top: 6px; }
    }
    .modal-tag { background: var(--camora-accent); color: #fff; }
    .close-btn {
      background: rgba(255,255,255,0.18); border: none; color: #fff;
      width: 34px; height: 34px; border-radius: 50%; font-size: 1.4rem; cursor: pointer;
    }
    .modal-body { padding: 24px; display: flex; flex-direction: column; gap: 14px; }
    .modal-footer { display: flex; justify-content: flex-end; gap: 10px; margin-top: 8px; }
  `]
})
export class ModalOngComponent implements OnInit {
  private readonly ongService = inject(OngService);
  private readonly toast = inject(ToastService);

  @Input({ required: true }) ong!: OngResponse;
  @Output() fechar = new EventEmitter<void>();
  @Output() salvo = new EventEmitter<OngResponse>();

  readonly saving = signal(false);

  nome = '';
  descricao = '';
  contatoEmail = '';
  telefone = '';
  redesSociais = '';
  site = '';
  cep = '';
  logradouro = '';
  bairro = '';
  cidade = '';
  uf = '';

  ngOnInit(): void {
    this.nome = this.ong.nome;
    this.descricao = this.ong.descricao || '';
    this.contatoEmail = this.ong.dadosContato?.email || '';
    this.telefone = this.ong.dadosContato?.telefone || '';
    this.redesSociais = this.ong.dadosContato?.redesSociais || '';
    this.site = this.ong.dadosContato?.site || '';
    this.cep = this.ong.local?.cep || '';
    this.logradouro = this.ong.local?.logradouro || '';
    this.bairro = this.ong.local?.bairro || '';
    this.cidade = this.ong.local?.cidade || '';
    this.uf = this.ong.local?.uf || '';
  }

  submit(): void {
    if (!this.nome.trim()) {
      this.toast.error('O nome da ONG é obrigatório.');
      return;
    }
    const payload: OngUpdateRequest = {
      nome: this.nome.trim(),
      descricao: this.descricao.trim(),
      dadosContato: {
        email: this.contatoEmail.trim(),
        telefone: this.telefone.trim(),
        redesSociais: this.redesSociais.trim(),
        site: this.site.trim()
      },
      local: {
        ...this.ong.local,
        cep: this.cep.trim(),
        logradouro: this.logradouro.trim(),
        bairro: this.bairro.trim(),
        cidade: this.cidade.trim(),
        uf: this.uf.trim().toUpperCase()
      }
    };

    this.saving.set(true);
    this.ongService.atualizarMeuPerfil(payload).subscribe({
      next: res => {
        this.saving.set(false);
        this.toast.success('Dados da ONG atualizados!');
        this.salvo.emit(res);
      },
      error: err => {
        this.saving.set(false);
        this.toast.error(err?.error?.mensagem || 'Erro ao atualizar dados da ONG.');
      }
    });
  }
}
