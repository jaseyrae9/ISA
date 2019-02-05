import { Component, OnInit, AfterViewInit, ChangeDetectorRef, Input  } from '@angular/core';
import { Chart } from 'chart.js';

@Component({
  selector: 'app-daily-income-chart',
  templateUrl: './daily-income-chart.component.html',
  styleUrls: ['./daily-income-chart.component.css']
})
export class DailyIncomeChartComponent implements OnInit, AfterViewInit {
  @Input() daily: any;
  @Input() type = 'line';

  chartDaily: any;

  constructor(private cd: ChangeDetectorRef) { }

  ngOnInit() {
  }

  ngAfterViewInit() {
    this.chartDaily = new Chart('canvasDaily', {
      // The type of chart we want to create
      type:  this.type,
      data: {
        labels: ['Monday', 'Thuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'],
        datasets: [{
            label: 'Daily',
            backgroundColor: 'rgb(51, 202, 187)',
            borderColor: 'rgb(51, 202, 187)',
            data: [this.daily[0],
                   this.daily[1],
                   this.daily[2],
                   this.daily[3],
                   this.daily[4],
                   this.daily[5],
                   this.daily[6]],
        }]
    },
    // Configuration options go here
    options: {}
    });
    this.cd.detectChanges();
  }

}
