import { Component, OnInit, Input } from '@angular/core';
import { Friendship } from 'src/app/model/users/friendship';

@Component({
  selector: 'app-friend-request',
  templateUrl: './friend-request.component.html',
  styleUrls: ['./friend-request.component.css']
})
export class FriendRequestComponent implements OnInit {
  @Input() request: Friendship;

  constructor() { }

  ngOnInit() {
  }

}
