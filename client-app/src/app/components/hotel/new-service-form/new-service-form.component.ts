import { Component, OnInit, EventEmitter, Output, ViewChild, ElementRef } from '@angular/core';
import { AdditionalService } from 'src/app/model/additional-service';
import { ActivatedRoute } from '@angular/router';
import { HotelService } from 'src/app/services/hotel/hotel.service';

@Component({
  selector: 'app-new-service-form',
  templateUrl: './new-service-form.component.html',
  styleUrls: ['./new-service-form.component.css', '../../../shared/css/inputField.css']
})
export class NewServiceFormComponent implements OnInit {
  @Output() additionalServiceCreated: EventEmitter<AdditionalService> = new EventEmitter();

  form: any = {};
  additionalService: AdditionalService;
  id: string;

  @ViewChild('closeBtn') closeBtn: ElementRef;

  constructor(private route: ActivatedRoute, private hotelService: HotelService) { }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    this.id = id;
  }

  onServiceAdd() {
    this.additionalService = new AdditionalService(null,
      this.form.name,
      this.form.description,
      this.form.price
    );

    this.hotelService.addAdditionService(this.additionalService, this.id).subscribe(
      data => {
        this.additionalServiceCreated.emit(data);
        this.closeBtn.nativeElement.click();
      },
      error => {
        console.log(error.error.message);
      }
    );

  }

}
