import { Component, OnInit, Input } from '@angular/core';
import { AdditionalService } from 'src/app/model/additional-service';

@Component({
  selector: 'app-additional-services-table',
  templateUrl: './additional-services-table.component.html',
  styleUrls: ['./additional-services-table.component.css']
})
export class AdditionalServicesTableComponent implements OnInit {
  @Input() additionalServices: AdditionalService[];

  constructor() { }

  ngOnInit() {
  }

}
