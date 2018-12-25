import { AuthService } from './../../auth/auth.service';
import { AuthLoginInfo } from './../../auth/login-info';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['../../shared/css/inputField.css']
})
export class LoginFormComponent implements OnInit {

  form: any = {};
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';

  private loginInfo: AuthLoginInfo;

  constructor(private authService: AuthService) { }

  ngOnInit() {
  }

  onLogin() {
    console.log(this.form);

    this.loginInfo = new AuthLoginInfo(
      this.form.username,
      this.form.password
    );

    this.authService.attemptAuth(this.loginInfo).subscribe(
      data => {
       // this.tokenStorage.saveToken(data.accessToken);
      //  this.tokenStorage.saveUsername(data.username);
      //  this.tokenStorage.saveAuthorities(data.authorities);

        this.isLoginFailed = false;
        this.isLoggedIn = true;
        this.reloadPage();
      },
      error => {
        console.log(error);
        this.errorMessage = error.error.message;
        this.isLoginFailed = true;
      }
    );

  }

  reloadPage() {
    window.location.reload();
  }

}
