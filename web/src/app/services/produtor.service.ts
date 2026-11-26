import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { IdentidadeVisualDTO, ProdutorResponse, ProdutorUpdateRequest } from '../models/camora.models';

@Injectable({ providedIn: 'root' })
export class ProdutorService {
  private readonly baseUrl = `${environment.apiUrl}/produtores`;

  constructor(private http: HttpClient) {}

  listarTodos(): Observable<ProdutorResponse[]> {
    return this.http.get<ProdutorResponse[]>(this.baseUrl);
  }

  buscarPorId(id: number): Observable<ProdutorResponse> {
    return this.http.get<ProdutorResponse>(`${this.baseUrl}/${id}`);
  }

  meuPerfil(): Observable<ProdutorResponse> {
    return this.http.get<ProdutorResponse>(`${this.baseUrl}/me`);
  }

  atualizarMeuPerfil(payload: ProdutorUpdateRequest): Observable<ProdutorResponse> {
    return this.http.put<ProdutorResponse>(`${this.baseUrl}/me`, payload);
  }

  atualizarIdentidadeVisual(dto: IdentidadeVisualDTO): Observable<IdentidadeVisualDTO> {
    return this.http.put<IdentidadeVisualDTO>(`${this.baseUrl}/me/identidade-visual`, dto);
  }

  restaurarIdentidadePadrao(): Observable<IdentidadeVisualDTO> {
    return this.http.post<IdentidadeVisualDTO>(`${this.baseUrl}/me/identidade-visual/padrao`, {});
  }
}
