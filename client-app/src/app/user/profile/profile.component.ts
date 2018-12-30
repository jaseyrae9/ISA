import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user/user.service';
import { User } from 'src/app/model/users/user';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css', '../../shared/css/inputField.css']
})
export class ProfileComponent implements OnInit {
  user: User;
  constructor(private userService: UserService) { }

  ngOnInit() {
    this.userService.getCurrentUserProfile().subscribe(
      user => {
        this.user = user;
      }
    );
  }

  profileChanged(data: User) {
    this.user.address = data.address;
    this.user.lastName = data.lastName;
    this.user.firstName = data.firstName;
    this.user.phoneNumber = data.phoneNumber;
  }
}
