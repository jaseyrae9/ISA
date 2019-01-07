import { Component, OnInit, ViewChild, ElementRef, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Car } from 'src/app/model/rent-a-car-company/car';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';

@Component({
  selector: 'app-new-car-form',
  templateUrl: './new-car-form.component.html',
  styleUrls: ['./new-car-form.component.css', '../../../shared/css/inputField.css']
})

export class NewCarFormComponent implements OnInit {
  @Output() carCreated: EventEmitter<Car> = new EventEmitter();
  form: any = {};
  @ViewChild('closeBtn') closeBtn: ElementRef;
  car: Car;
  id: string;

  constructor(private route: ActivatedRoute, private rentACarCompanyService: RentACarCompanyService) { }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    this.id = id;
  }

  onCarAdd() {
    this.car = new Car(null,
      this.form.brand,
      this.form.model,
      this.form.seatsNumber,
      this.form.doorsNumber,
      this.form.yearOfProduction,
      this.form.price);

    console.log(this.car);

    this.rentACarCompanyService.addCar(this.car, this.id).subscribe(
      data => {
        this.carCreated.emit(data);
        this.closeBtn.nativeElement.click();
      },
      error => {
        console.log(error.error.message);
      }

    );
  }

}
