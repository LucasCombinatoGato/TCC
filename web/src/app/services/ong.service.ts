import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { IdentidadeVisualDTO, OngResponse, OngUpdateRequest } from '../models/camora.models';

@Injectable({ providedIn: 'root' })
export class OngService {
  private readonly baseUrl = `${environment.apiUrl}/ongs`;

  constructor(private http: HttpClient) {}

  listarTodas(): Observable<OngResponse[]> {
    return this.http.get<OngResponse[]>(this.baseUrl);
  }

  buscarPorId(id: number): Observable<OngResponse> {
    return this.http.get<OngResponse>(`${this.baseUrl}/${id}`);
  }

  meuPerfil(): Observable<OngResponse> {
    return this.http.get<OngResponse>(`${this.baseUrl}/me`);
  }

  atualizarMeuPerfil(payload: OngUpdateRequest): Observable<OngResponse> {
    return this.http.put<OngResponse>(`${this.baseUrl}/me`, payload);
  }

  atualizarIdentidadeVisual(dto: IdentidadeVisualDTO): Observable<IdentidadeVisualDTO> {
    return this.http.put<IdentidadeVisualDTO>(`${this.baseUrl}/me/identidade-visual`, dto);
  }

  restaurarIdentidadePadrao(): Observable<IdentidadeVisualDTO> {
    return this.http.post<IdentidadeVisualDTO>(`${this.baseUrl}/me/identidade-visual/padrao`, {});
  }
}
