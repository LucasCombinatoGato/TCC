import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { NotificacaoResponse } from '../models/camora.models';

@Injectable({ providedIn: 'root' })
export class NotificacaoService {
  private readonly baseUrl = `${environment.apiUrl}/notificacoes`;

  constructor(private http: HttpClient) {}

  listar(): Observable<NotificacaoResponse[]> {
    return this.http.get<NotificacaoResponse[]>(this.baseUrl);
  }

  contarNaoLidas(): Observable<{ naoLidas: number }> {
    return this.http.get<{ naoLidas: number }>(`${this.baseUrl}/nao-lidas/count`);
  }

  marcarComoLida(id: number): Observable<NotificacaoResponse> {
    return this.http.patch<NotificacaoResponse>(`${this.baseUrl}/${id}/lida`, {});
  }

  marcarTodasComoLidas(): Observable<void> {
    return this.http.patch<void>(`${this.baseUrl}/ler-todas`, {});
  }
}
