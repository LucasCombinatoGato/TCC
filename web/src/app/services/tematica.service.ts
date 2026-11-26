import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { TematicaDTO } from '../models/camora.models';

@Injectable({ providedIn: 'root' })
export class TematicaService {
  private readonly baseUrl = `${environment.apiUrl}/tematicas`;

  constructor(private http: HttpClient) {}

  listarTodas(): Observable<TematicaDTO[]> {
    return this.http.get<TematicaDTO[]>(this.baseUrl);
  }
}
