import { Component, OnInit, ViewChild } from '@angular/core';
import { FriendshipsPageComponent } from '../friendships-page/friendships-page.component';

@Component({
  selector: 'app-friends-page',
  templateUrl: './friends-page.component.html',
  styleUrls: ['./friends-page.component.css']
})
export class FriendsPageComponent implements OnInit {
  @ViewChild(FriendshipsPageComponent)
  private friendshipsPage: FriendshipsPageComponent;

  constructor() { }

  ngOnInit() { }

  friendAdded() {
    this.friendshipsPage.loadFriends();
  }

}
