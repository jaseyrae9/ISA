import { JwtResponse } from './../../../auth/jwt-response';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Component, OnInit, Input, TemplateRef } from '@angular/core';
import { ChangePasswordData } from 'src/app/model/users/changePassword';
import { UserService } from 'src/app/services/user/user.service';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { HttpErrorResponse } from '@angular/common/http';
import { ViewChild, ElementRef} from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { config } from 'rxjs';
import { TokenPayload } from 'src/app/model/token-payload';
import * as decode from 'jwt-decode';

@Component({
  selector: 'app-change-password-form',
  templateUrl: './change-password-form.component.html',
  styleUrls: ['../../../shared/css/inputField.css']
})
export class ChangePasswordFormComponent implements OnInit {
  editForm: FormGroup;
  data: ChangePasswordData = new ChangePasswordData();
  errorMessage: String = '';
  jwtToken: String = '';
  modalRef: BsModalRef;
  config = {
    keyboard: true,
    ignoreBackdropClick: true
  };

  @ViewChild('changePasswordModal') changePasswordModal;

  constructor(private formBuilder: FormBuilder, private userService: UserService,
    private tokenService: TokenStorageService, private modalService: BsModalService) { }

  ngOnInit() {
    this.editForm = this.formBuilder.group({
      newPassword: ['', [Validators.required]],
      confirmNewPassword: ['', [Validators.required]],
      oldPassword: ['', [Validators.required]]
    });
  }

  checkPasswords(group: FormGroup) { // here we have the 'passwords' group
  const pass = group.controls.newPassword.value;
  const confirmPass = group.controls.confirmNewPassword.value;

  return pass === confirmPass ? null : { notSame: true };
}

  openModal() {
    this.modalRef = this.modalService.show(this.changePasswordModal, this.config);
  }

  openModalWithToken(jwtToken: String) {
    this.jwtToken = jwtToken;
    if (this.jwtToken.length > 0) {
      config['keyboard'] = false;
      config['ignoreBackdropClick'] = false;
    }
    this.modalRef = this.modalService.show(this.changePasswordModal, this.config);
  }

  onChangePassword() {
    this.userService.changePassword(this.editForm.value, this.jwtToken).subscribe(
      (data: JwtResponse)  => {
        this.tokenService.saveToken(data.token);
        const tokenPayload: TokenPayload = decode(data.token);
        this.tokenService.saveRoles(tokenPayload.roles);
        this.modalRef.hide();
      },
      (err: HttpErrorResponse) => {
        // interceptor je hendlovao ove zahteve
        if (err.status === 401 || err.status === 403 || err.status === 404) {
          this.modalRef.hide();
        }
        this.errorMessage = err.error.details;
      }
    );
  }
}
