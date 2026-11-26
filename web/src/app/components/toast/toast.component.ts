import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToastService } from '../../services/toast.service';

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="toast-stack" aria-live="polite">
      @for (toast of toastService.toasts(); track toast.id) {
        <div class="toast-item" [class]="'toast-' + toast.type" (click)="toastService.dismiss(toast.id)">
          <span class="toast-dot"></span>
          <span>{{ toast.text }}</span>
        </div>
      }
    </div>
  `,
  styles: [`
    .toast-stack {
      position: fixed;
      bottom: 24px;
      right: 24px;
      display: flex;
      flex-direction: column;
      gap: 10px;
      z-index: 2000;
      max-width: 380px;
    }
    .toast-item {
      display: flex;
      align-items: center;
      gap: 10px;
      padding: 13px 18px;
      border-radius: 14px;
      color: #fff;
      font-weight: 500;
      font-size: 0.9rem;
      cursor: pointer;
      box-shadow: 0 12px 28px rgba(26, 18, 48, 0.28);
      animation: slideIn 0.2s ease-out;
    }
    .toast-success { background: #15803D; }
    .toast-error { background: #B91C1C; }
    .toast-info { background: #471CA8; }
    .toast-dot {
      width: 9px;
      height: 9px;
      border-radius: 50%;
      background: #FFBE0B;
      flex-shrink: 0;
    }
    @keyframes slideIn {
      from { opacity: 0; transform: translateX(20px); }
      to { opacity: 1; transform: translateX(0); }
    }
  `]
})
export class ToastComponent {
  readonly toastService = inject(ToastService);
}
