import { AuthService } from './../../auth/auth.service';
import { SignUpInfo } from './../../auth/signup-info';
import { Component, OnInit } from '@angular/core';


@Component({
  selector: 'app-register-form',
  templateUrl: './register-form.component.html',
  styleUrls: ['../../shared/css/inputField.css']
})
export class RegisterFormComponent implements OnInit {

  form: any = {};
  signupInfo: SignUpInfo;
  isSignedUp = false;
  isSignUpFailed = false;
  errorMessage = '';

  constructor(private authService: AuthService) { }

  ngOnInit() {
  }

  onSignup() {
    console.log(this.form);
    this.signupInfo = new SignUpInfo(
      this.form.username,
      this.form.email,
      this.form.firstName,
      this.form.lastName,
      this.form.address,
      this.form.phoneNumber,
      this.form.password,
      this.form.mathcingPassword);

      this.authService.signUp(this.signupInfo).subscribe(
        data => {
          console.log(data);
          this.isSignedUp = true;
          this.isSignUpFailed = false;
        },
        error => {
          console.log(error);
          this.errorMessage = error.error.message;
          this.isSignUpFailed = true;
        }
      );

  }

}
