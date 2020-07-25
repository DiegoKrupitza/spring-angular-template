import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NotificationService} from "../../services/notification.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  public loginForm: FormGroup = new FormGroup({});

  constructor(private formBuilder: FormBuilder,
              private notificationService: NotificationService) {

    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(8)]]
    });

  }

  ngOnInit(): void {
  }

  /**
   * Performs the login operation to the backend
   */
  performLogin() {
    this.notificationService.showSuccess("Hello1");
    this.notificationService.showStandard("Hello2");
    this.notificationService.showError("Hello3");
    this.notificationService.showWarn("Hello4");
  }
}
