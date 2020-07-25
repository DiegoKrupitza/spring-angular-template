import {Injectable, TemplateRef} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  toasts: any[] = [];

  /**
   * shows a toast
   * @param textOrTpl text or template to be shown
   * @param options for the toast
   */
  show(textOrTpl: string | TemplateRef<any>, options: any = {}) {
    this.toasts.push({textOrTpl, ...options});
  }

  /**
   * removes a specific toast
   * @param toast that is removed
   */
  remove(toast) {
    this.toasts = this.toasts.filter(t => t !== toast);
  }

  /**
   * shows a message with standard header and look
   * @param message that is shown
   */
  showStandard(message) {
    this.show(message, {
      autohide: true,
      delay: 30000,
      header: 'Info',
      body: message
    });
  }

  /**
   * shows a message with Success header and look
   * @param message that is shown
   */
  showSuccess(message) {
    this.show(message, {
      classname: 'bg-success text-light',
      autohide: true,
      delay: 30000,
      header: 'Success',
      body: message
    });
  }

  /**
   * shows a message with Error header and look
   * @param message that is shown
   */
  showError(message) {
    this.show(message, {
      classname: 'bg-danger text-light',
      autohide: true,
      delay: 30000,
      header: 'Error',
      body: message
    });
  }

  /**
   * shows a message with warning header and look
   * @param message that is shown
   */
  showWarn(message) {
    this.show(message, {
      classname: 'bg-warning text-light',
      autohide: true,
      delay: 30000,
      header: 'Warning',
      body: message
    });
  }

}
