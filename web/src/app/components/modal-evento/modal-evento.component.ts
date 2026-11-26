import { Component, EventEmitter, Input, OnInit, Output, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  EventoRequest,
  EventoResponse,
  InformacoesDiaDTO,
  StatusEvento,
  TematicaDTO,
  TipoAmbito,
  TipoIngressoDTO
} from '../../models/camora.models';
import { EventoService } from '../../services/evento.service';
import { ToastService } from '../../services/toast.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-modal-evento',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './modal-evento.component.html',
  styleUrl: './modal-evento.component.scss'
})
export class ModalEventoComponent implements OnInit {
  private readonly eventoService = inject(EventoService);
  private readonly toast = inject(ToastService);
  private readonly auth = inject(AuthService);

  @Input() eventoEditar: EventoResponse | null = null;
  @Input() tematicas: TematicaDTO[] = [];
  @Input() ambitoInicial: TipoAmbito = 'EVENTO';

  @Output() fechar = new EventEmitter<void>();
  @Output() salvo = new EventEmitter<EventoResponse>();

  readonly saving = signal(false);

  nome = '';
  descricao = '';
  dataInicio = '';
  dataFim = '';
  classificacaoIndicativa = 'Livre';
  estacionamento = false;
  valorEstacionamento = 0;
  imagemCapaUrl = '';
  tipoAmbito: TipoAmbito = 'EVENTO';
  status: StatusEvento = 'PUBLICADO';
  tematicaId: number = 1;

  cep = '';
  tipoLogradouro = 'Rua';
  logradouro = '';
  bairro = '';
  cidade = '';
  uf = 'SP';
  numero = '';
  complemento = '';

  informacoesDias: InformacoesDiaDTO[] = [];
  tiposIngresso: TipoIngressoDTO[] = [];

  ngOnInit(): void {
    if (this.tematicas.length > 0) {
      this.tematicaId = this.tematicas[0].id;
    }
    if (this.eventoEditar) {
      const e = this.eventoEditar;
      this.nome = e.nome;
      this.descricao = e.descricao || '';
      this.dataInicio = this.toInputDateTime(e.dataInicio);
      this.dataFim = this.toInputDateTime(e.dataFim);
      this.classificacaoIndicativa = e.classificacaoIndicativa || 'Livre';
      this.estacionamento = e.estacionamento;
      this.valorEstacionamento = Number(e.valorEstacionamento || 0);
      this.imagemCapaUrl = e.imagemCapaUrl || '';
      this.tipoAmbito = e.tipoAmbito;
      this.status = e.status;
      this.tematicaId = e.tematica?.id ?? this.tematicaId;

      if (e.local) {
        this.cep = e.local.cep || '';
        this.tipoLogradouro = e.local.tipoLogradouro || 'Rua';
        this.logradouro = e.local.logradouro || '';
        this.bairro = e.local.bairro || '';
        this.cidade = e.local.cidade || '';
        this.uf = e.local.uf || 'SP';
        this.numero = e.local.numero || '';
        this.complemento = e.local.complemento || '';
      }

      this.informacoesDias = (e.informacoesDias || []).map(d => ({ ...d }));
      this.tiposIngresso = (e.tiposIngresso || []).map(t => ({ ...t }));
    } else {
      this.tipoAmbito = this.auth.userRole() === 'ROLE_ONG' ? 'ACAO_SOCIAL' : this.ambitoInicial;
      const amanha = new Date(Date.now() + 86400000);
      const isoBase = amanha.toISOString().slice(0, 10);
      this.dataInicio = `${isoBase}T14:00`;
      this.dataFim = `${isoBase}T20:00`;
      this.adicionarIngresso();
    }
  }

  adicionarDia(): void {
    this.informacoesDias.push({
      data: this.dataInicio ? this.dataInicio.slice(0, 10) : new Date().toISOString().slice(0, 10),
      horaInicio: '14:00',
      horaIntervalo: '',
      horaFinal: '20:00'
    });
  }

  removerDia(idx: number): void {
    this.informacoesDias.splice(idx, 1);
  }

  adicionarIngresso(): void {
    this.tiposIngresso.push({
      nome: this.tipoAmbito === 'ACAO_SOCIAL' ? 'Participação / Voluntário' : 'Entrada Geral',
      descricao: '',
      valor: 0,
      quantidadeDisponivel: 100,
      beneficios: '',
      linkCompra: ''
    });
  }

  removerIngresso(idx: number): void {
    this.tiposIngresso.splice(idx, 1);
  }

  submit(): void {
    if (!this.nome.trim() || !this.dataInicio || !this.dataFim || !this.tematicaId) {
      this.toast.error('Preencha nome, temática, data de início e data de término.');
      return;
    }

    const payload: EventoRequest = {
      nome: this.nome.trim(),
      descricao: this.descricao.trim(),
      dataInicio: this.ensureSeconds(this.dataInicio),
      dataFim: this.ensureSeconds(this.dataFim),
      classificacaoIndicativa: this.classificacaoIndicativa,
      estacionamento: this.estacionamento,
      valorEstacionamento: this.estacionamento ? Number(this.valorEstacionamento || 0) : 0,
      imagemCapaUrl: this.imagemCapaUrl.trim() || null,
      tipoAmbito: this.tipoAmbito,
      status: this.status,
      tematicaId: Number(this.tematicaId),
      local: {
        cep: this.cep.trim(),
        tipoLogradouro: this.tipoLogradouro.trim(),
        logradouro: this.logradouro.trim(),
        bairro: this.bairro.trim(),
        cidade: this.cidade.trim(),
        uf: this.uf.trim().toUpperCase(),
        numero: this.numero.trim(),
        complemento: this.complemento.trim()
      },
      informacoesDias: this.informacoesDias
        .filter(d => d.data && d.horaInicio && d.horaFinal)
        .map(d => ({
          ...d,
          horaInicio: this.ensureTimeSeconds(d.horaInicio),
          horaIntervalo: d.horaIntervalo ? this.ensureTimeSeconds(d.horaIntervalo) : null,
          horaFinal: this.ensureTimeSeconds(d.horaFinal)
        })),
      tiposIngresso: this.tiposIngresso
        .filter(t => t.nome.trim())
        .map(t => ({
          ...t,
          nome: t.nome.trim(),
          valor: Number(t.valor || 0)
        }))
    };

    this.saving.set(true);
    const req$ = this.eventoEditar
      ? this.eventoService.atualizar(this.eventoEditar.id, payload)
      : this.eventoService.criar(payload);

    req$.subscribe({
      next: res => {
        this.saving.set(false);
        this.toast.success(this.eventoEditar ? 'Projeto atualizado com sucesso!' : 'Projeto criado com sucesso!');
        this.salvo.emit(res);
      },
      error: err => {
        this.saving.set(false);
        this.toast.error(err?.error?.mensagem || 'Não foi possível salvar o evento.');
      }
    });
  }

  private toInputDateTime(iso: string): string {
    return iso ? iso.slice(0, 16) : '';
  }

  private ensureSeconds(dt: string): string {
    return dt.length === 16 ? `${dt}:00` : dt;
  }

  private ensureTimeSeconds(time: string): string {
    return time.length === 5 ? `${time}:00` : time;
  }
}
