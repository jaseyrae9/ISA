import { Component, OnInit, Input, ViewChild, ElementRef, Output, EventEmitter } from '@angular/core';
import { Car } from 'src/app/model/rent-a-car-company/car';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-edit-car-form',
  templateUrl: './edit-car-form.component.html',
  styleUrls: ['./edit-car-form.component.css',  '../../../shared/css/inputField.css']
})
export class EditCarFormComponent implements OnInit {
  @Output() carEdited: EventEmitter<Car> = new EventEmitter();
  @ViewChild('closeBtn') closeBtn: ElementRef;
  @Input() car: Car;
  companyId: string;

  constructor(private route: ActivatedRoute, private carCompanyService: RentACarCompanyService) { }

  ngOnInit() {
    const companyId = this.route.snapshot.paramMap.get('id');
    this.companyId = companyId;
    console.log("car type" + this.car.type);

  }

  onCarEdit() {
    this.carCompanyService.editCar(this.car, this.companyId).subscribe(
      data => {
        this.carEdited.emit(data);
        this.closeBtn.nativeElement.click();
      },
      error => {
        console.log(error.error.message);
      }
    );
  }

}
