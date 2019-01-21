import { EditProfileFormComponent } from './../edit-profile-form/edit-profile-form.component';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { BsModalService } from 'ngx-bootstrap/modal';
import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user/user.service';
import { User } from 'src/app/model/users/user';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css', '../../../shared/css/inputField.css']
})
export class ProfileComponent implements OnInit {
  user: User = new User();
  modalRef: BsModalRef;

  constructor(private modalService: BsModalService, private userService: UserService) { }

  ngOnInit() {
    this.userService.getCurrentUserProfile().subscribe(
      user => {
        this.user = user;
      }
    );
  }

  openEditModal() {
    const initialState = {
      user: this.user
    };
    this.modalRef = this.modalService.show(EditProfileFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(user => {
      this.user = user;
    });
  }
}
