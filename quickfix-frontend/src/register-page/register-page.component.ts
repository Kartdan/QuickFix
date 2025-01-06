import { Component } from '@angular/core';

@Component({
  selector: 'app-register-page',
  standalone: false,
  
  templateUrl: './register-page.component.html',
  styleUrl: './register-page.component.scss'
})
export class RegisterPageComponent {
  offersServices = false;

  toggleServiceOffering(event: Event): void {
    const checkbox = event.target as HTMLInputElement;
    this.offersServices = checkbox.checked;
  }
}
