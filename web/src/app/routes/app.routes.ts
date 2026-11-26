import { Routes } from '@angular/router';
import { creatorGuard, guestGuard } from './auth.guards';

export const APP_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('../pages/home/home.page').then(m => m.HomePage)
  },
  {
    path: 'login',
    canActivate: [guestGuard],
    loadComponent: () => import('../pages/login/login.page').then(m => m.LoginPage)
  },
  {
    path: 'cadastro',
    canActivate: [guestGuard],
    loadComponent: () => import('../pages/cadastro/cadastro.page').then(m => m.CadastroPage)
  },
  {
    path: 'eventos',
    loadComponent: () => import('../pages/eventos/eventos.page').then(m => m.EventosPage)
  },
  {
    path: 'eventos/:id',
    loadComponent: () => import('../pages/detalhes-evento/detalhes-evento.page').then(m => m.DetalhesEventoPage)
  },
  {
    path: 'ongs',
    loadComponent: () => import('../pages/ongs/ongs.page').then(m => m.OngsPage)
  },
  {
    path: 'area-criador',
    canActivate: [creatorGuard],
    loadComponent: () => import('../pages/area-criador/area-criador.page').then(m => m.AreaCriadorPage)
  },
  {
    path: 'perfil-produtor',
    canActivate: [creatorGuard],
    loadComponent: () => import('../pages/perfil-produtor/perfil-produtor.page').then(m => m.PerfilProdutorPage)
  },
  {
    path: 'produtores/:id',
    loadComponent: () => import('../pages/perfil-produtor/perfil-produtor.page').then(m => m.PerfilProdutorPage)
  },
  {
    path: 'identidade-visual',
    canActivate: [creatorGuard],
    loadComponent: () => import('../pages/identidade-visual/identidade-visual.page').then(m => m.IdentidadeVisualPage)
  },
  {
    path: '**',
    redirectTo: ''
  }
];
