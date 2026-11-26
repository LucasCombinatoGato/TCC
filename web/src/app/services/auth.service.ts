import { Injectable, computed, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import {
  AuthResponse,
  IdentidadeVisualDTO,
  LoginRequest,
  RegistroOngRequest,
  RegistroProdutorRequest,
  RegistroUsuarioRequest,
  Role
} from '../models/camora.models';

const STORAGE_KEY = 'camora_auth_session';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly baseUrl = `${environment.apiUrl}/auth`;
  private readonly sessionSignal = signal<AuthResponse | null>(this.loadSession());

  readonly currentUser = this.sessionSignal.asReadonly();
  readonly isLoggedIn = computed(() => this.sessionSignal() !== null);
  readonly userRole = computed<Role | null>(() => this.sessionSignal()?.role ?? null);
  readonly isCreator = computed(() => {
    const role = this.userRole();
    return role === 'ROLE_PRODUCER' || role === 'ROLE_ONG';
  });

  constructor(private http: HttpClient, private router: Router) {}

  login(payload: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/login`, payload).pipe(
      tap(res => this.saveSession(res))
    );
  }

  registrarUsuario(payload: RegistroUsuarioRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/register/usuario`, payload).pipe(
      tap(res => this.saveSession(res))
    );
  }

  registrarProdutor(payload: RegistroProdutorRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/register/produtor`, payload).pipe(
      tap(res => this.saveSession(res))
    );
  }

  registrarOng(payload: RegistroOngRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/register/ong`, payload).pipe(
      tap(res => this.saveSession(res))
    );
  }

  atualizarIdentidadeSessao(identidade: IdentidadeVisualDTO, novoNome?: string): void {
    const current = this.sessionSignal();
    if (!current) return;
    const updated: AuthResponse = {
      ...current,
      nome: novoNome ?? current.nome,
      identidadeVisual: identidade
    };
    this.saveSession(updated);
  }

  logout(): void {
    localStorage.removeItem(STORAGE_KEY);
    this.sessionSignal.set(null);
    this.router.navigate(['/']);
  }

  getToken(): string | null {
    return this.sessionSignal()?.token ?? null;
  }

  private saveSession(auth: AuthResponse): void {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(auth));
    this.sessionSignal.set(auth);
  }

  private loadSession(): AuthResponse | null {
    try {
      const raw = localStorage.getItem(STORAGE_KEY);
      return raw ? (JSON.parse(raw) as AuthResponse) : null;
    } catch {
      return null;
    }
  }
}
