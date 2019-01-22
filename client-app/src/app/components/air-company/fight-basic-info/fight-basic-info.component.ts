import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from 'src/app/auth/token-storage.service';

@Component({
  selector: 'app-fight-basic-info',
  templateUrl: './fight-basic-info.component.html',
  styleUrls: ['./fight-basic-info.component.css', '../../../shared/css/inputField.css']
})
export class FightBasicInfoComponent implements OnInit {

  constructor(public tokenService: TokenStorageService) { }

  ngOnInit() {
  }
}
