import { Component, OnInit } from '@angular/core';
import { ChangePasswordData } from 'src/app/model/users/changePassword';
import { UserService } from 'src/app/services/user/user.service';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { calcPossibleSecurityContexts } from '@angular/compiler/src/template_parser/binding_parser';

@Component({
  selector: 'app-change-password-form',
  templateUrl: './change-password-form.component.html',
  styleUrls: ['../../shared/css/inputField.css']
})
export class ChangePasswordFormComponent implements OnInit {
  data: ChangePasswordData = new ChangePasswordData();
  errorMessage: String = '';

  constructor(private userService: UserService, private tokenService: TokenStorageService) { }

  ngOnInit() {
  }

  onChangePassword() {
    this.userService.changePassword(this.data).subscribe(
      data => {
        this.tokenService.saveToken(data.token);
        // TODO: Zatvoriti dijalog
      },
      (err: HttpErrorResponse) => {
        this.errorMessage = err.error;
      }
    );
  }
}
