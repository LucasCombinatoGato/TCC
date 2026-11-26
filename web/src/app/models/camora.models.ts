export type Role = 'ROLE_USER' | 'ROLE_PRODUCER' | 'ROLE_ONG';

export type StatusEvento = 'RASCUNHO' | 'PUBLICADO' | 'CANCELADO';

export type TipoAmbito = 'EVENTO' | 'ACAO_SOCIAL';

export interface IdentidadeVisualDTO {
  id?: number;
  logo?: string | null;
  corPrincipal: string;
  corSecundaria: string;
  corDestaque: string;
  usarIdentidadePadrao: boolean;
}

export interface DadosContatoDTO {
  id?: number;
  email?: string;
  telefone?: string;
  redesSociais?: string;
  site?: string;
}

export interface LocalDTO {
  id?: number;
  cep?: string;
  tipoLogradouro?: string;
  logradouro?: string;
  bairro?: string;
  cidade?: string;
  uf?: string;
  numero?: string;
  complemento?: string;
}

export interface TematicaDTO {
  id: number;
  nome: string;
  descricao?: string;
  corHex?: string;
}

export interface InformacoesDiaDTO {
  id?: number;
  data: string;
  horaInicio: string;
  horaIntervalo?: string | null;
  horaFinal: string;
}

export interface TipoIngressoDTO {
  id?: number;
  nome: string;
  descricao?: string;
  valor: number;
  quantidadeDisponivel?: number | null;
  beneficios?: string;
  linkCompra?: string;
}

export interface AuthResponse {
  token: string;
  id: number;
  nome: string;
  email: string;
  role: Role;
  identidadeVisual?: IdentidadeVisualDTO | null;
}

export interface LoginRequest {
  email: string;
  senha: string;
  tipoPerfil?: Role | null;
}

export interface RegistroUsuarioRequest {
  cpf: string;
  nome: string;
  email: string;
  senha: string;
  local?: LocalDTO;
  tematicasPreferidasIds?: number[];
}

export interface RegistroProdutorRequest {
  cnpj: string;
  nomeEmpresa: string;
  email: string;
  senha: string;
  local?: LocalDTO;
  dadosContato?: DadosContatoDTO;
  identidadeVisual?: IdentidadeVisualDTO;
}

export interface RegistroOngRequest {
  cnpj: string;
  nome: string;
  descricao?: string;
  email: string;
  senha: string;
  local?: LocalDTO;
  dadosContato?: DadosContatoDTO;
  identidadeVisual?: IdentidadeVisualDTO;
}

export interface EventoRequest {
  nome: string;
  descricao?: string;
  dataInicio: string;
  dataFim: string;
  classificacaoIndicativa?: string;
  estacionamento: boolean;
  valorEstacionamento?: number | null;
  imagemCapaUrl?: string | null;
  tipoAmbito?: TipoAmbito;
  status?: StatusEvento;
  tematicaId: number;
  local?: LocalDTO;
  informacoesDias?: InformacoesDiaDTO[];
  tiposIngresso?: TipoIngressoDTO[];
}

export interface EventoResponse {
  id: number;
  nome: string;
  descricao?: string;
  dataInicio: string;
  dataFim: string;
  classificacaoIndicativa?: string;
  estacionamento: boolean;
  valorEstacionamento: number;
  imagemCapaUrl?: string | null;
  tipoAmbito: TipoAmbito;
  status: StatusEvento;
  tematica?: TematicaDTO;
  local?: LocalDTO;
  organizadorId?: number;
  organizadorNome?: string;
  organizadorTipo?: 'PRODUTOR' | 'ONG' | string;
  organizadorContato?: DadosContatoDTO;
  identidadeVisual?: IdentidadeVisualDTO;
  informacoesDias: InformacoesDiaDTO[];
  tiposIngresso: TipoIngressoDTO[];
  gratuito: boolean;
  priorizado: boolean;
  salvoPeloUsuario: boolean;
  dataCriacao: string;
  dataAtualizacao?: string;
}

export interface EventoSalvoResponse {
  id: number;
  dataSalvamento: string;
  evento: EventoResponse;
}

export interface ProdutorResponse {
  id: number;
  cnpj: string;
  nomeEmpresa: string;
  email: string;
  local?: LocalDTO;
  dadosContato?: DadosContatoDTO;
  identidadeVisual?: IdentidadeVisualDTO;
}

export interface ProdutorUpdateRequest {
  nomeEmpresa?: string;
  local?: LocalDTO;
  dadosContato?: DadosContatoDTO;
  identidadeVisual?: IdentidadeVisualDTO;
}

export interface OngResponse {
  id: number;
  cnpj: string;
  nome: string;
  descricao?: string;
  email: string;
  local?: LocalDTO;
  dadosContato?: DadosContatoDTO;
  identidadeVisual?: IdentidadeVisualDTO;
}

export interface OngUpdateRequest {
  nome?: string;
  descricao?: string;
  local?: LocalDTO;
  dadosContato?: DadosContatoDTO;
  identidadeVisual?: IdentidadeVisualDTO;
}

export interface UsuarioResponse {
  id: number;
  cpf: string;
  nome: string;
  email: string;
  local?: LocalDTO;
  preferencias: TematicaDTO[];
}

export interface NotificacaoResponse {
  id: number;
  eventoId: number;
  eventoNome: string;
  mensagem: string;
  dataCriacao: string;
  lida: boolean;
}

export interface EventoFiltros {
  q?: string;
  tematicaId?: number | null;
  cidade?: string;
  uf?: string;
  gratuito?: boolean | null;
  dataInicio?: string;
  dataFim?: string;
  tipoAmbito?: TipoAmbito | null;
  ongId?: number | null;
  produtorId?: number | null;
}

export const CAMORA_DEFAULT_IDENTITY: IdentidadeVisualDTO = {
  logo: null,
  corPrincipal: '#471CA8',
  corSecundaria: '#884AB2',
  corDestaque: '#FF930A',
  usarIdentidadePadrao: true
};
