import { ActivatedRoute } from '@angular/router';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-error-page',
  templateUrl: './error-page.component.html',
  styleUrls: ['./error-page.component.css']
})
export class ErrorPageComponent implements OnInit {
  is401 = false;
  constructor(private route: ActivatedRoute) {
    const code = this.route.snapshot.paramMap.get('code');
    if (code === '401') {
      this.is401 = true;
    }
  }

  ngOnInit() {
  }

}
