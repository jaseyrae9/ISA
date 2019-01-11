import { Component, OnInit, Input } from '@angular/core';
import { AdditionalService } from 'src/app/model/additional-service';

@Component({
  selector: 'app-additional-service',
  templateUrl: './additional-service.component.html',
  styleUrls: ['./additional-service.component.css']
})
export class AdditionalServiceComponent implements OnInit {
  @Input() additionalService: AdditionalService;

  constructor() { }

  ngOnInit() {
  }

}
