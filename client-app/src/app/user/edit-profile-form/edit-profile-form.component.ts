import { Component, OnInit, Input } from '@angular/core';
import { User } from 'src/app/model/users/user';

@Component({
  selector: 'app-edit-profile-form',
  templateUrl: './edit-profile-form.component.html',
  styleUrls: ['../../shared/css/inputField.css']
})
export class EditProfileFormComponent implements OnInit {
  @Input() user: User;
  constructor() { }

  ngOnInit() {
  }

}
