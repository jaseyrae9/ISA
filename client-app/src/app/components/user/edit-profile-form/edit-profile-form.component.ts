import { Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import { User } from 'src/app/model/users/user';
import { UserService } from 'src/app/services/user/user.service';
import { HttpErrorResponse } from '@angular/common/http';
import { ViewChild, ElementRef} from '@angular/core';

@Component({
  selector: 'app-edit-profile-form',
  templateUrl: './edit-profile-form.component.html',
  styleUrls: ['../../../shared/css/inputField.css']
})
export class EditProfileFormComponent implements OnInit {
  @Output() profileChanged: EventEmitter<User> = new EventEmitter();
  @Input() user: User;
  errorMessage: String = '';
  @ViewChild('closeBtn') closeBtn: ElementRef;

  constructor(private userService: UserService) { }

  ngOnInit() {
  }

  onUpdateProfile() {
    this.userService.updateProfile(this.user).subscribe(
      data => {
        this.profileChanged.emit(data);
        this.closeBtn.nativeElement.click();
      },
      (err: HttpErrorResponse) => {
        this.errorMessage = err.error;
      }
    );
  }


}
