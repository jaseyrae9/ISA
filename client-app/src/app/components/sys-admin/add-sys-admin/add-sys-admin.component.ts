import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { HttpErrorResponse } from '@angular/common/http';
import { User } from 'src/app/model/users/user';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-add-sys-admin',
  templateUrl: './add-sys-admin.component.html',
  styleUrls: ['./add-sys-admin.component.css', '../../../shared/css/inputField.css']
})
export class AddSysAdminComponent implements OnInit {
  errorMessage: String = '';
  private user: User = new User();
  newSysAdminForm: FormGroup;

  constructor(private userService: UserService,
    public modalRef: BsModalRef,
    private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.newSysAdminForm = this.formBuilder.group({
      email: [this.user.email, [Validators.required]],
      firstName: [this.user.firstName, [Validators.required]],
      lastName: [this.user.lastName, [Validators.required]],
      address: [this.user.address, [Validators.required]],
      phoneNumber: [this.user.phoneNumber, [Validators.required]],
    });
  }

  onSysAdminAdd() {
    console.log(this.newSysAdminForm.value);

    this.user.password = 'changeme';
    this.user.email = this.newSysAdminForm.value.email;
    this.user.firstName = this.newSysAdminForm.value.firstName;
    this.user.lastName = this.newSysAdminForm.value.lastName;
    this.user.address = this.newSysAdminForm.value.address;
    this.user.phoneNumber = this.newSysAdminForm.value.phoneNumber;

    this.userService.addSysAdmin(this.user).subscribe(
      data => {
        console.log(data);
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
