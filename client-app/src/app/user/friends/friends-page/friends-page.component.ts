import { Component, OnInit, ViewChild } from '@angular/core';
import { FriendshipsPageComponent } from '../friendships-page/friendships-page.component';
import { Friendship } from 'src/app/model/users/friendship';
import { FriendRequestsPageComponent } from '../friend-requests-page/friend-requests-page.component';
import { FindFriendsPageComponent } from '../find-friends-page/find-friends-page.component';

@Component({
  selector: 'app-friends-page',
  templateUrl: './friends-page.component.html',
  styleUrls: ['./friends-page.component.css']
})
export class FriendsPageComponent implements OnInit {
  @ViewChild(FriendshipsPageComponent)
  private friendshipsPage: FriendshipsPageComponent;
  @ViewChild(FriendRequestsPageComponent)
  private requestPage: FriendRequestsPageComponent;
  @ViewChild(FindFriendsPageComponent)
  private findFriendsPage: FindFriendsPageComponent;

  constructor() { }

  ngOnInit() { }

  requestPageEvent(data) {
    this.friendshipsPage.reloadIfNeeded(data);
    this.findFriendsPage.reloadIfNeeded(data);
  }

  friendPageEvent(data) {
    this.findFriendsPage.reloadIfNeeded(data);
  }

  searchPageEvent(data) {
    this.friendshipsPage.reloadIfNeeded(data);
    this.requestPage.reloadIfNeeded(data);
  }

}
