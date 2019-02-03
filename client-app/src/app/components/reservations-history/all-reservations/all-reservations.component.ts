import { Component, OnInit } from '@angular/core';
import { UserService} from 'src/app/services/user/user.service';
import { Reservation } from 'src/app/model/users/reservation';

@Component({
  selector: 'app-all-reservations',
  templateUrl: './all-reservations.component.html',
  styleUrls: ['./all-reservations.component.css']
})
export class AllReservationsComponent implements OnInit {
  reservations: Reservation[] = [];

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.userService.getReservations().subscribe(
      (data) => {
        this.reservations = data;
        console.log('Rezervacije: ', this.reservations);
      }
    );
  }

}
