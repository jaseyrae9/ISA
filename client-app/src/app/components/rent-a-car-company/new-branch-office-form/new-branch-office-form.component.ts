import { Component, OnInit, Output, ViewChild, ElementRef, EventEmitter} from '@angular/core';
import { BranchOffice } from 'src/app/model/rent-a-car-company/branch-offfice';
import { ActivatedRoute } from '@angular/router';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';

@Component({
  selector: 'app-new-branch-office-form',
  templateUrl: './new-branch-office-form.component.html',
  styleUrls: ['../../../shared/css/inputField.css', './new-branch-office-form.component.css']
})
export class NewBranchOfficeFormComponent implements OnInit {
  @Output() branchOfficeCreated: EventEmitter<BranchOffice> = new EventEmitter();
  form: any = {};
  @ViewChild('closeBtn') closeBtn: ElementRef;
  branchOffice: BranchOffice;
  id: string;

  constructor(private route: ActivatedRoute, private rentACarCompanyService: RentACarCompanyService) { }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    this.id = id;
  }

  onBranchOfficeAdd() {
    this.branchOffice = new BranchOffice(null,
      this.form.name,
      true);

    this.rentACarCompanyService.addBranchOffice(this.branchOffice, this.id).subscribe(
      data => {
        this.branchOfficeCreated.emit(data);
        this.closeBtn.nativeElement.click();
      },
      error => {
        console.log(error.error.message);
      }
    );
  }

}
