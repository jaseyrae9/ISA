import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { Friendship } from 'src/app/model/users/friendship';
import { UserService } from 'src/app/services/user/user.service';
import { AlertService } from 'ngx-alerts';

@Component({
  selector: 'app-friendships-page',
  templateUrl: './friendships-page.component.html',
  styleUrls: ['./friendships-page.component.css']
})
export class FriendshipsPageComponent implements OnInit {
  friendships: Array<Friendship> = new Array();
  pageNumber = 0;
  pages: Array<Number> = new Array();
  sort  = 'key.from.firstName';
  @Output() friendshipEvent: EventEmitter<Object> = new EventEmitter();

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.loadFriends();
  }

  loadFriends() {
    this.userService.getFriends(this.pageNumber, this.sort).subscribe(
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

  onSortChange(value) {
    this.sort = value;
    this.loadFriends();
  }

  changePage(i: number, event: any) {
    this.pageNumber = i;
    this.loadFriends();
  }

  arrowAction(i: number, event: any) {
    if ( this.pageNumber + i >= 0 && this.pageNumber + i < this.pages.length) {
      this.pageNumber += i;
      this.loadFriends();
    }
  }

  friendRemoved(data) {
    this.reloadIfNeeded(data);
    this.friendshipEvent.emit(data);
  }

  /**
   * Ucitavanje ako je potrebno
   */
  reloadIfNeeded(data) {
    if ( data['event'] === 0 || data['event'] === 1) {
      this.loadFriends();
    }
  }


}
