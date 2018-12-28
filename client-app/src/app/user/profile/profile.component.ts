import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user/user.service';
import { User } from 'src/app/model/users/user';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css', '../../shared/css/inputField.css']
})
export class ProfileComponent implements OnInit {
  user: User = new User();
  constructor(private userService: UserService) { }

  ngOnInit() {
    this.userService.getCurrentUserProfile().subscribe(
      user => {
        this.user = user;
      }
    );
  }

}
