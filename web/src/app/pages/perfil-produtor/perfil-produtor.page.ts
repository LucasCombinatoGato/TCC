import { Component, OnDestroy, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { EventoResponse, OngResponse, ProdutorResponse } from '../../models/camora.models';
import { ProdutorService } from '../../services/produtor.service';
import { OngService } from '../../services/ong.service';
import { EventoService } from '../../services/evento.service';
import { AuthService } from '../../services/auth.service';
import { ThemeService } from '../../services/theme.service';
import { ToastService } from '../../services/toast.service';
import { EventoCardComponent } from '../../components/evento-card/evento-card.component';

@Component({
  selector: 'app-perfil-produtor-page',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, EventoCardComponent],
  templateUrl: './perfil-produtor.page.html',
  styleUrl: './perfil-produtor.page.scss'
})
export class PerfilProdutorPage implements OnInit, OnDestroy {
  private readonly route = inject(ActivatedRoute);
  private readonly produtorService = inject(ProdutorService);
  private readonly ongService = inject(OngService);
  private readonly eventoService = inject(EventoService);
  readonly auth = inject(AuthService);
  private readonly theme = inject(ThemeService);
  private readonly toast = inject(ToastService);

  readonly loading = signal(true);
  readonly saving = signal(false);
  readonly modoPublico = signal(false);
  readonly eventosPublicos = signal<EventoResponse[]>([]);

  produtor: ProdutorResponse | null = null;
  ong: OngResponse | null = null;

  nome = '';
  descricao = '';
  contatoEmail = '';
  telefone = '';
  redesSociais = '';
  site = '';
  cep = '';
  logradouro = '';
  numero = '';
  bairro = '';
  cidade = '';
  uf = '';

  ngOnInit(): void {
    const publicId = this.route.snapshot.paramMap.get('id');
    if (publicId) {
      this.modoPublico.set(true);
      const id = Number(publicId);
      this.produtorService.buscarPorId(id).subscribe({
        next: p => {
          this.produtor = p;
          this.theme.applyIdentity(p.identidadeVisual);
          this.preencherFormulario(p.nomeEmpresa, '', p.dadosContato, p.local);
          this.eventoService.listarPublicos({ produtorId: p.id }).subscribe({
            next: evs => this.eventosPublicos.set(evs)
          });
          this.loading.set(false);
        },
        error: () => {
          this.loading.set(false);
          this.toast.error('Produtor não encontrado.');
        }
      });
      return;
    }

    if (this.auth.userRole() === 'ROLE_ONG') {
      this.ongService.meuPerfil().subscribe({
        next: o => {
          this.ong = o;
          this.theme.applyIdentity(o.identidadeVisual);
          this.preencherFormulario(o.nome, o.descricao || '', o.dadosContato, o.local);
          this.loading.set(false);
        },
        error: () => this.loading.set(false)
      });
    } else {
      this.produtorService.meuPerfil().subscribe({
        next: p => {
          this.produtor = p;
          this.theme.applyIdentity(p.identidadeVisual);
          this.preencherFormulario(p.nomeEmpresa, '', p.dadosContato, p.local);
          this.loading.set(false);
        },
        error: () => this.loading.set(false)
      });
    }
  }

  ngOnDestroy(): void {
    if (this.modoPublico()) {
      this.theme.resetToDefault();
    }
  }

  salvar(): void {
    const dadosContato = {
      email: this.contatoEmail.trim(),
      telefone: this.telefone.trim(),
      redesSociais: this.redesSociais.trim(),
      site: this.site.trim()
    };
    const local = {
      cep: this.cep.trim(),
      tipoLogradouro: 'Rua',
      logradouro: this.logradouro.trim(),
      numero: this.numero.trim(),
      bairro: this.bairro.trim(),
      cidade: this.cidade.trim(),
      uf: this.uf.trim().toUpperCase()
    };

    this.saving.set(true);

    if (this.auth.userRole() === 'ROLE_ONG') {
      this.ongService.atualizarMeuPerfil({
        nome: this.nome.trim(),
        descricao: this.descricao.trim(),
        dadosContato,
        local
      }).subscribe({
        next: res => {
          this.ong = res;
          this.saving.set(false);
          this.auth.atualizarIdentidadeSessao(res.identidadeVisual || this.theme.activeIdentity(), res.nome);
          this.toast.success('Perfil da ONG atualizado!');
        },
        error: () => {
          this.saving.set(false);
          this.toast.error('Erro ao salvar perfil.');
        }
      });
    } else {
      this.produtorService.atualizarMeuPerfil({
        nomeEmpresa: this.nome.trim(),
        dadosContato,
        local
      }).subscribe({
        next: res => {
          this.produtor = res;
          this.saving.set(false);
          this.auth.atualizarIdentidadeSessao(res.identidadeVisual || this.theme.activeIdentity(), res.nomeEmpresa);
          this.toast.success('Perfil do Produtor atualizado!');
        },
        error: () => {
          this.saving.set(false);
          this.toast.error('Erro ao salvar perfil.');
        }
      });
    }
  }

  private preencherFormulario(nome: string, desc: string, contato: any, local: any): void {
    this.nome = nome;
    this.descricao = desc;
    this.contatoEmail = contato?.email || '';
    this.telefone = contato?.telefone || '';
    this.redesSociais = contato?.redesSociais || '';
    this.site = contato?.site || '';
    this.cep = local?.cep || '';
    this.logradouro = local?.logradouro || '';
    this.numero = local?.numero || '';
    this.bairro = local?.bairro || '';
    this.cidade = local?.cidade || '';
    this.uf = local?.uf || '';
  }
}
