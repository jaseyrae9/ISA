import { Component, OnInit, Output, EventEmitter, Input} from '@angular/core';
import { Friendship } from 'src/app/model/users/friendship';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-friendship',
  templateUrl: './friendship.component.html',
  styleUrls: ['../../../shared/css/inputField.css']
})
export class FriendshipComponent implements OnInit {
  @Input() friendship: Friendship;
  @Output() friendshipRemoved: EventEmitter<Object> = new EventEmitter();

  constructor(private userService: UserService) { }

  ngOnInit() {
  }

  deleteFriendRequest() {
    this.userService.deleteFriendship(this.friendship.fromId).subscribe(
      (data) => {
        this.friendshipRemoved.emit(this.friendship);
      },
      (error: any) => {
         console.log(error);
      }
    );
  }
}
