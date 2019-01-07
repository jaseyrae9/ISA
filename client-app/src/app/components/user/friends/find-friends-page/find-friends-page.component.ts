import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { UserService } from 'src/app/services/user/user.service';
import { Friendship } from 'src/app/model/users/friendship';

@Component({
  selector: 'app-find-friends-page',
  templateUrl: './find-friends-page.component.html',
  styleUrls: ['./find-friends-page.component.css']
})
export class FindFriendsPageComponent implements OnInit {
  searchTerm = '';
  searchResult: Array<Friendship> = new Array();
  pageNumber = 0;
  pages: Array<Number> = new Array();
  @Output() friendshipEvent: EventEmitter<Object> = new EventEmitter();

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.search();
  }

  /**
   * Izvrsi pretragu
   */
  search() {
    this.userService.searchCustomers(this.searchTerm, this.pageNumber).subscribe(
      (data) => {
        this.searchResult = data['content'];
        this.pages = new Array(data['totalPages']);
      }
    );
  }

  changePage(i: number, event: any) {
    this.pageNumber = i;
    this.search();
  }

  arrowAction(i: number, event: any) {
    if ( this.pageNumber + i >= 0 && this.pageNumber + i < this.pages.length) {
      this.pageNumber += i;
      this.search();
    }
  }

  /**
   * Promeni prikaz izmenjenog prijatelja
   */
  reloadIfNeeded(data) {
    const f = data['friendship'];
    const res: Friendship[]  = this.searchResult.filter(friendship => friendship.user2Id === f.user2Id);
    if ( res.length > 0) {
      res[0].status = f.status;
    }
  }

  friendEvent(data) {
    this.friendshipEvent.emit(data);
  }

}
