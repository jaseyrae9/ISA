import { UserService } from 'src/app/services/user/user.service';
import { Invite } from './../../../model/users/invite';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-trip-invites',
  templateUrl: './trip-invites.component.html',
  styleUrls: ['./trip-invites.component.css']
})
export class TripInvitesComponent implements OnInit {
  invites: Invite[] = [];
  constructor(private userService: UserService) { }

  ngOnInit() {
    this.userService.getInvites().subscribe(
      (data) => {
        this.invites = data;
      }
    );
  }

}
