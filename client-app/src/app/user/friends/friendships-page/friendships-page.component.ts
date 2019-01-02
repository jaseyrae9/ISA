import { Component, OnInit } from '@angular/core';
import { Friendship } from 'src/app/model/users/friendship';
import { UserService } from 'src/app/services/user/user.service';
import { NgxNotificationService } from 'ngx-notification';

@Component({
  selector: 'app-friendships-page',
  templateUrl: './friendships-page.component.html',
  styleUrls: ['./friendships-page.component.css']
})
export class FriendshipsPageComponent implements OnInit {
  friendships: Array<Friendship> = new Array();
  pageNumber = 0;
  pages: Array<Number> = new Array();

  constructor(private userService: UserService, private ngxNotificationService: NgxNotificationService) { }

  ngOnInit() {
    this.loadFriends();
  }

  loadFriends() {
    this.userService.getFriends(this.pageNumber).subscribe(
      (data) => {
        this.friendships = data['content'];
        this.pages = new Array(data['totalPages']);
        if (this.friendships.length === 0 && this.pageNumber !== 0) {
          this.pageNumber -= 1;
          this.loadFriends();
       }
      }
    );
  }

  changePage(i: number, event: any) {
    this.pageNumber = i;
    this.loadFriends();
  }

  arrowAction(i: number, event: any) {
    if ( this.pageNumber + i >= 0 && this.pageNumber + i <= this.pages.length) {
      this.pageNumber += i;
      this.loadFriends();
    }
  }

  friendRemoved(data) {
    // tslint:disable-next-line:max-line-length
    this.ngxNotificationService.sendMessage(data.fromFirstname + ' ' + data.fromLastname + ' is no longer your friend.', 'dark', 'bottom-right');
    const index: number = this.friendships.indexOf(data);
    if (index !== -1) {
        this.loadFriends();
    }
  }


}
