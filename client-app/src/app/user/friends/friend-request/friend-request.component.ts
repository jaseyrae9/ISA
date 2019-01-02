import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';
import { Friendship } from 'src/app/model/users/friendship';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-friend-request',
  templateUrl: './friend-request.component.html',
  styleUrls: ['../../../shared/css/inputField.css']
})
export class FriendRequestComponent implements OnInit {
  @Input() request: Friendship;
  @Output() friendRequestEvent: EventEmitter<Object> = new EventEmitter();

  constructor(private userService: UserService) { }

  ngOnInit() {
  }

  acceptFriendRequest() {
    this.userService.acceptFriendRequest(this.request.fromId).subscribe(
      (data) => {
        const ret = {};
        ret['action'] = 0;
        ret['request'] = this.request;
        this.friendRequestEvent.emit(ret);
      },
      (error: any) => {
         console.log(error);
      }
    );
  }

  declineFriendRequest() {
    this.userService.deleteFriendship(this.request.fromId).subscribe(
      (data) => {
        const ret = {};
        ret['action'] = 1;
        ret['request'] = this.request;
        this.friendRequestEvent.emit(ret);
      },
      (error: any) => {
         console.log(error);
      }
    );
  }

}
