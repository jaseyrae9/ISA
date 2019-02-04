import { FlightReservation } from './../../../../model/air-company/flight-reservation';
import { FriendsPageComponent } from './../../../user/friends/friends-page/friends-page.component';
import { Friendship } from 'src/app/model/users/friendship';
import { Subject } from 'rxjs/Subject';
import { Component, OnInit, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-invite-a-friend',
  templateUrl: './invite-a-friend.component.html',
  styleUrls: ['./invite-a-friend.component.css', '../../../../shared/css/inputField.css']
})
export class InviteAFriendComponent implements OnInit {
  @Input() reservation: FlightReservation = new FlightReservation();
  selectedFriend: Friendship = new Friendship();
  friendships: Friendship[] = [];
  public onClose: Subject<any>;
  constructor(public modalRef: BsModalRef, private userService: UserService) { }

  ngOnInit() {
    this.onClose = new Subject();
    this.userService.getAllFriends().subscribe(
      (data) => {
        this.friendships = data['content'];
        for (const id of this.getFriendsId()) {
          const index = this.friendships.findIndex(f => f.user2Id === id);
          console.log(index);
          if (index !== -1) {
            this.friendships.splice(index, 1);
          }
        }
      }
    );
  }

  getFriendsId(): number[] {
    const invitedFriends: number[] = [];
    for (const ticketReservation of this.reservation.ticketReservations) {
      if (ticketReservation.status === 2) {
        invitedFriends.push(ticketReservation.friend_id);
      }
    }
    return invitedFriends;
  }

  submit() {
    if (this.selectedFriend.user2Id !== -1) {
      this.onClose.next(this.selectedFriend);
    }
    this.modalRef.hide();
  }

  onClick(item) {
    this.selectedFriend = item;
  }
}
