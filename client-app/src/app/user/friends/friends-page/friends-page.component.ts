import { Component, OnInit } from '@angular/core';
import { Friendship } from 'src/app/model/users/friendship';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-friends-page',
  templateUrl: './friends-page.component.html',
  styleUrls: ['./friends-page.component.css']
})
export class FriendsPageComponent implements OnInit {
  requests: Friendship[] = [];
  friends: Friendship[] = [];

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.userService.getFriendRequests().subscribe(
      (data) => this.requests = data
    );
  }

}
