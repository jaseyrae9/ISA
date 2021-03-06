import { AuthService } from 'src/app/auth/auth.service';
import { Component, OnInit } from '@angular/core';
import { SignUpInfo } from 'src/app/auth/signup-info';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-register-form',
  templateUrl: './register-form.component.html',
  styleUrls: ['../../../shared/css/inputField.css']
})
export class RegisterFormComponent implements OnInit {

  // form: any = {};
  signupInfo: SignUpInfo;
  isSignedUp = false;
  isSignUpFailed = false;
  errorMessage = '';
  registerForm: FormGroup;


  constructor(private authService: AuthService,
    private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.registerForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      address: ['', [Validators.required]],
      phoneNumber: ['', [Validators.required]],
      password: ['', [Validators.required]],
      matchingPassword: ['', [Validators.required]]
    });

  }

  onSignup() {
    const pass = this.registerForm.value.password;
    const confirmPass = this.registerForm.value.matchingPassword;
    if (pass !== confirmPass) {
      this.errorMessage = 'Your password does not match';
      return;
    }

    console.log(this.registerForm.value);
    this.signupInfo = new SignUpInfo(
      this.registerForm.value.email,
      this.registerForm.value.firstName,
      this.registerForm.value.lastName,
      this.registerForm.value.address,
      this.registerForm.value.phoneNumber,
      this.registerForm.value.password
    );

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
