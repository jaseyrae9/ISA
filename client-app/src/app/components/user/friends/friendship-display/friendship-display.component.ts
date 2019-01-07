import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';
import { Friendship } from 'src/app/model/users/friendship';
import { UserService } from 'src/app/services/user/user.service';
import { NgxNotificationService } from 'ngx-notification';

@Component({
  selector: 'app-friendship-display',
  templateUrl: './friendship-display.component.html',
  styleUrls: ['./../../../../shared/css/inputField.css']
})
export class FriendshipDisplayComponent implements OnInit {
  @Input() friendship: Friendship;
  @Output() friendshipEvent: EventEmitter<Object> = new EventEmitter();

  constructor(private userService: UserService, private ngxNotificationService: NgxNotificationService) { }

  ngOnInit() {
  }

  /**
   * Prihavatanje zahteva za prijateljstvo
   */
  acceptFriendRequest() {
    this.userService.acceptFriendRequest(this.friendship.user2Id).subscribe(
      (data) => {
        this.friendship.status = 0; // prihvacen zahtev
        const res = {'event' : 0, 'friendship': this.friendship};
        this.friendshipEvent.emit(res);
         // tslint:disable-next-line:max-line-length
        this.ngxNotificationService.sendMessage(this.friendship.user2Firstname + ' ' + this.friendship.user2Lastname + ' is now your friend!', 'dark', 'bottom-right');
      },
      (error: any) => {
         console.log(error);
      }
    );
  }

   /**
   * Brisanje zahteva za prijateljsvo/prijateljstva
   */
  deleteFriendship() {
    this.userService.deleteFriendship(this.friendship.user2Id).subscribe(
      (data) => {
        if (this.friendship.status === 0) {
           // tslint:disable-next-line:max-line-length
          this.ngxNotificationService.sendMessage(this.friendship.user2Firstname + ' ' + this.friendship.user2Lastname + ' is no longer your friend.', 'dark', 'bottom-right');
        } else {
           // tslint:disable-next-line:max-line-length
          this.ngxNotificationService.sendMessage('Friendship request from ' + this.friendship.user2Firstname + ' ' + this.friendship.user2Lastname + ' declined.', 'dark', 'bottom-right');
        }
        this.friendship.status = 3; // posle brisanja nisu vise nista
        const res = {'event' : 1, 'friendship': this.friendship};
        this.friendshipEvent.emit(res);
      },
      (error: any) => {
         console.log(error);
      }
    );
  }

   /**
   * Slanje zahteva za prijateljsvo/prijateljstva
   */
  sendRequest() {
    this.userService.sendRequest(this.friendship.user2Id).subscribe(
      (data) => {
        this.friendship.status = 2; // zahtev poslan
        const res = {'event' : 2, 'friendship': this.friendship};
        this.friendshipEvent.emit(res);
         // tslint:disable-next-line:max-line-length
         this.ngxNotificationService.sendMessage('Request sent to ' + this.friendship.user2Firstname + ' ' + this.friendship.user2Lastname + '!', 'dark', 'bottom-right');
      },
      (error: any) => {
         console.log(error);
      }
    );
  }

}
