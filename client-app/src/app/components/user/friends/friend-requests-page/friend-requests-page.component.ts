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
  sort  = 'key.from.firstName';
  pageNumber = 0;
  pages: Array<Number> = new Array();
  @Output() friendRequestEvent: EventEmitter<Object> = new EventEmitter();

  constructor(private userService: UserService, private ngxNotificationService: NgxNotificationService) { }

  ngOnInit() {
    this.loadRequests();
  }

  loadRequests() {
    this.userService.getFriendRequests(this.pageNumber, this.sort).subscribe(
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

  onSortChange(value) {
    this.sort = value;
    this.loadRequests();
  }

  changePage(i: number, event: any) {
    this.pageNumber = i;
    this.loadRequests();
  }

  arrowAction(i: number, event: any) {
    if ( this.pageNumber + i >= 0 && this.pageNumber + i < this.pages.length) {
      this.pageNumber += i;
      this.loadRequests();
    }
  }

  /**
   * Reakcija na prihvatanje/odbijanje zahteva na stranici
   */
  requestEvent(data) {
    this.reloadIfNeeded(data);
    this.friendRequestEvent.emit(data);
  }

  /**
   * Ucitavanje ponovo, ako se u okviru prikaza nalazilo dato prijateljstvo
   */
  reloadIfNeeded(data) {
    if (data['event'] === 0 || data['event'] === 1) {
      this.loadRequests();
    }
  }
}
