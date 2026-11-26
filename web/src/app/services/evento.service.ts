import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { EventoFiltros, EventoRequest, EventoResponse } from '../models/camora.models';

@Injectable({ providedIn: 'root' })
export class EventoService {
  private readonly baseUrl = `${environment.apiUrl}/eventos`;

  constructor(private http: HttpClient) {}

  listarPublicos(filtros: EventoFiltros = {}): Observable<EventoResponse[]> {
    let params = new HttpParams();
    if (filtros.q?.trim()) params = params.set('q', filtros.q.trim());
    if (filtros.tematicaId) params = params.set('tematicaId', String(filtros.tematicaId));
    if (filtros.cidade?.trim()) params = params.set('cidade', filtros.cidade.trim());
    if (filtros.uf?.trim()) params = params.set('uf', filtros.uf.trim().toUpperCase());
    if (filtros.gratuito !== null && filtros.gratuito !== undefined) {
      params = params.set('gratuito', String(filtros.gratuito));
    }
    if (filtros.dataInicio) params = params.set('dataInicio', filtros.dataInicio);
    if (filtros.dataFim) params = params.set('dataFim', filtros.dataFim);
    if (filtros.tipoAmbito) params = params.set('tipoAmbito', filtros.tipoAmbito);
    if (filtros.ongId) params = params.set('ongId', String(filtros.ongId));
    if (filtros.produtorId) params = params.set('produtorId', String(filtros.produtorId));

    return this.http.get<EventoResponse[]>(this.baseUrl, { params });
  }

  listarMeusEventos(): Observable<EventoResponse[]> {
    return this.http.get<EventoResponse[]>(`${this.baseUrl}/meus`);
  }

  buscarPorId(id: number): Observable<EventoResponse> {
    return this.http.get<EventoResponse>(`${this.baseUrl}/${id}`);
  }

  criar(payload: EventoRequest): Observable<EventoResponse> {
    return this.http.post<EventoResponse>(this.baseUrl, payload);
  }

  atualizar(id: number, payload: EventoRequest): Observable<EventoResponse> {
    return this.http.put<EventoResponse>(`${this.baseUrl}/${id}`, payload);
  }

  publicar(id: number): Observable<EventoResponse> {
    return this.http.patch<EventoResponse>(`${this.baseUrl}/${id}/publicar`, {});
  }

  despublicar(id: number): Observable<EventoResponse> {
    return this.http.patch<EventoResponse>(`${this.baseUrl}/${id}/despublicar`, {});
  }

  cancelar(id: number): Observable<EventoResponse> {
    return this.http.patch<EventoResponse>(`${this.baseUrl}/${id}/cancelar`, {});
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
