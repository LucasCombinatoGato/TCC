import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import {
  CAMORA_DEFAULT_IDENTITY,
  IdentidadeVisualDTO,
  TematicaDTO
} from '../../models/camora.models';
import { AuthService } from '../../services/auth.service';
import { TematicaService } from '../../services/tematica.service';
import { ToastService } from '../../services/toast.service';
import { ThemeService } from '../../services/theme.service';

type TipoCadastro = 'PRODUTOR' | 'ONG' | 'USUARIO';

@Component({
  selector: 'app-cadastro-page',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './cadastro.page.html',
  styleUrl: './cadastro.page.scss'
})
export class CadastroPage implements OnInit {
  private readonly auth = inject(AuthService);
  private readonly tematicaService = inject(TematicaService);
  private readonly toast = inject(ToastService);
  private readonly theme = inject(ThemeService);
  private readonly router = inject(Router);

  readonly tipo = signal<TipoCadastro>('PRODUTOR');
  readonly loading = signal(false);
  readonly tematicas = signal<TematicaDTO[]>([]);
  readonly tematicasSelecionadas = signal<number[]>([]);

  // Campos comuns
  email = '';
  senha = '';
  documento = ''; // CPF ou CNPJ
  nome = ''; // Nome ou Nome da Empresa / ONG
  descricaoOng = '';

  // Local
  cep = '';
  logradouro = '';
  numero = '';
  bairro = '';
  cidade = '';
  uf = 'SP';

  // DadosContato
  telefone = '';
  redesSociais = '';
  site = '';

  // White-label inicial
  identidade: IdentidadeVisualDTO = { ...CAMORA_DEFAULT_IDENTITY };

  ngOnInit(): void {
    this.tematicaService.listarTodas().subscribe({
      next: list => this.tematicas.set(list),
      error: () => {}
    });
  }

  toggleTematica(id: number): void {
    this.tematicasSelecionadas.update(curr =>
      curr.includes(id) ? curr.filter(item => item !== id) : [...curr, id]
    );
  }

  cadastrar(): void {
    if (!this.nome.trim() || !this.email.trim() || !this.senha || !this.documento.trim()) {
      this.toast.error('Preencha todos os campos obrigatórios.');
      return;
    }

    const local = {
      cep: this.cep.trim(),
      tipoLogradouro: 'Rua',
      logradouro: this.logradouro.trim(),
      numero: this.numero.trim(),
      bairro: this.bairro.trim(),
      cidade: this.cidade.trim(),
      uf: this.uf.trim().toUpperCase()
    };

    const dadosContato = {
      email: this.email.trim(),
      telefone: this.telefone.trim(),
      redesSociais: this.redesSociais.trim(),
      site: this.site.trim()
    };

    this.loading.set(true);

    if (this.tipo() === 'PRODUTOR') {
      this.auth.registrarProdutor({
        cnpj: this.documento.trim(),
        nomeEmpresa: this.nome.trim(),
        email: this.email.trim(),
        senha: this.senha,
        local,
        dadosContato,
        identidadeVisual: this.identidade
      }).subscribe({
        next: res => this.concluir(res.identidadeVisual, '/area-criador'),
        error: err => this.tratarErro(err)
      });
    } else if (this.tipo() === 'ONG') {
      this.auth.registrarOng({
        cnpj: this.documento.trim(),
        nome: this.nome.trim(),
        descricao: this.descricaoOng.trim(),
        email: this.email.trim(),
        senha: this.senha,
        local,
        dadosContato,
        identidadeVisual: this.identidade
      }).subscribe({
        next: res => this.concluir(res.identidadeVisual, '/area-criador'),
        error: err => this.tratarErro(err)
      });
    } else {
      this.auth.registrarUsuario({
        cpf: this.documento.trim(),
        nome: this.nome.trim(),
        email: this.email.trim(),
        senha: this.senha,
        local,
        tematicasPreferidasIds: this.tematicasSelecionadas()
      }).subscribe({
        next: () => this.concluir(null, '/eventos'),
        error: err => this.tratarErro(err)
      });
    }
  }

  private concluir(iv: IdentidadeVisualDTO | null | undefined, rota: string): void {
    this.loading.set(false);
    if (iv) this.theme.applyIdentity(iv);
    this.toast.success('Conta criada com sucesso!');
    this.router.navigate([rota]);
  }

  private tratarErro(err: any): void {
    this.loading.set(false);
    this.toast.error(err?.error?.mensagem || 'Não foi possível concluir o cadastro.');
  }
}
