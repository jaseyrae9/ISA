import { Component, OnInit } from '@angular/core';
import { HttpClient } from "@angular/common/http";

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit {

  isLoggedIn = false;
  loggedUsername = '';

  constructor(private httpClient : HttpClient) { 

  }

  ngOnInit() {
      if (localStorage.getItem('currentUser')) {
        this.isLoggedIn = true;
        this.loggedUsername = localStorage.getItem('currentUser');
      }
  }

}
