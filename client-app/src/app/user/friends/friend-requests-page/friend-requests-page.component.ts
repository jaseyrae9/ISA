import { Component, OnInit, Output, EventEmitter} from '@angular/core';
import { Friendship } from 'src/app/model/users/friendship';
import { UserService } from 'src/app/services/user/user.service';
import { NgxNotificationService } from 'ngx-notification';

@Component({
  selector: 'app-friend-requests-page',
  templateUrl: './friend-requests-page.component.html',
  styleUrls: ['./friend-requests-page.component.css']
})
export class FriendRequestsPageComponent implements OnInit {
  requests: Array<Friendship> = new Array();
  pageNumber = 0;
  pages: Array<Number> = new Array();
  @Output() friendRequestAccepted: EventEmitter<Friendship> = new EventEmitter();

  constructor(private userService: UserService, private ngxNotificationService: NgxNotificationService) { }

  ngOnInit() {
    this.loadRequests();
  }

  loadRequests() {
    this.userService.getFriendRequests(this.pageNumber).subscribe(
      (data) => {
        this.requests = data['content'];
        this.pages = new Array(data['totalPages']);
        // vrati sa zadnje stranice na prethodnu, zbog brisanja i prihavatnja
        if (this.requests.length === 0 && this.pageNumber !== 0) {
           this.pageNumber -= 1;
           this.loadRequests();
        }
      }
    );
  }

  changePage(i: number, event: any) {
    this.pageNumber = i;
    this.loadRequests();
  }

  arrowAction(i: number, event: any) {
    if ( this.pageNumber + i >= 0 && this.pageNumber + i <= this.pages.length) {
      this.pageNumber += i;
      this.loadRequests();
    }
  }

  requestEvent(data) {
    const index: number = this.requests.indexOf(data['request']);
    const f: Friendship = data['request'];
    if (data['action'] === 0) {
      this.ngxNotificationService.sendMessage(f.fromFirstname + ' ' + f.fromLastname + ' is now your friend!', 'dark', 'bottom-right');
      this.friendRequestAccepted.emit(f);
    } else {
      this.ngxNotificationService.sendMessage(f.fromFirstname + ' ' + f.fromLastname + ' declined.', 'light', 'bottom-right');
    }
    if (index !== -1) {
        this.loadRequests();
    }
  }

}
