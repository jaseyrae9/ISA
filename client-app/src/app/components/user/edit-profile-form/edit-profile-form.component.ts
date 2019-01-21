import { Subject } from 'rxjs';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import { User } from 'src/app/model/users/user';
import { UserService } from 'src/app/services/user/user.service';
import { HttpErrorResponse } from '@angular/common/http';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
  selector: 'app-edit-profile-form',
  templateUrl: './edit-profile-form.component.html',
  styleUrls: ['../../../shared/css/inputField.css']
})
export class EditProfileFormComponent implements OnInit {
  @Output() profileChanged: EventEmitter<User> = new EventEmitter();
  @Input() user: User;
  errorMessage: String = '';
  editForm: FormGroup;
  public onClose: Subject<User>;

  constructor(public modalRef: BsModalRef, private formBuilder: FormBuilder, private userService: UserService) { }

  ngOnInit() {
    this.onClose = new Subject();
    this.editForm = this.formBuilder.group({
      firstName: [this.user.firstName, [Validators.required]],
      lastName: [this.user.lastName, [Validators.required]],
      address: [this.user.address, [Validators.required]],
      phoneNumber: [this.user.phoneNumber, [Validators.required]]
    });
  }

  onUpdateProfile() {
    const value = this.editForm.value;
    value.email = this.user.email;
    this.userService.updateProfile(value).subscribe(
      data => {
        this.onClose.next(data);
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
