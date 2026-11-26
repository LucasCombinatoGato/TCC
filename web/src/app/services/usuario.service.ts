import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { EventoSalvoResponse, UsuarioResponse } from '../models/camora.models';

@Injectable({ providedIn: 'root' })
export class UsuarioService {
  private readonly baseUrl = `${environment.apiUrl}/usuarios`;

  constructor(private http: HttpClient) {}

  meuPerfil(): Observable<UsuarioResponse> {
    return this.http.get<UsuarioResponse>(`${this.baseUrl}/me`);
  }

  atualizarPreferencias(tematicasIds: number[]): Observable<UsuarioResponse> {
    return this.http.put<UsuarioResponse>(`${this.baseUrl}/me/preferencias`, { tematicasIds });
  }

  listarEventosSalvos(): Observable<EventoSalvoResponse[]> {
    return this.http.get<EventoSalvoResponse[]>(`${this.baseUrl}/me/eventos-salvos`);
  }

  salvarEvento(eventoId: number): Observable<EventoSalvoResponse> {
    return this.http.post<EventoSalvoResponse>(`${this.baseUrl}/me/eventos-salvos/${eventoId}`, {});
  }

  removerEventoSalvo(eventoId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/me/eventos-salvos/${eventoId}`);
  }
}
