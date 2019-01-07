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
  forEditing: User = new User();
  constructor(private userService: UserService) { }

  ngOnInit() {
    this.userService.getCurrentUserProfile().subscribe(
      user => {
        this.user = user;
        // tslint:disable-next-line:max-line-length
        this.forEditing = new User(null, this.user.firstName, this.user.lastName, this.user.email, this.user.phoneNumber, this.user.address);
      }
    );
  }

  profileChanged(data: User) {
    this.user.address = data.address;
    this.user.lastName = data.lastName;
    this.user.firstName = data.firstName;
    this.user.phoneNumber = data.phoneNumber;
    this.forEditing = new User(null, this.user.firstName, this.user.lastName, this.user.email, this.user.phoneNumber, this.user.address);
  }
}
