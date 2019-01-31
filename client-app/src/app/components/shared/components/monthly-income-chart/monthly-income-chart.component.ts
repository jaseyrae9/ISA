import { Component, OnInit, AfterViewInit, ChangeDetectorRef, Input } from '@angular/core';
import { Chart } from 'chart.js';

@Component({
  selector: 'app-monthly-income-chart',
  templateUrl: './monthly-income-chart.component.html',
  styleUrls: ['./monthly-income-chart.component.css']
})
export class MonthlyIncomeChartComponent implements OnInit, AfterViewInit {
  @Input() monthly: any;

  chartMonthly: any;

  constructor(private cd: ChangeDetectorRef) { }

  ngOnInit() {
  }

  ngAfterViewInit() {
    this.chartMonthly = new Chart('canvasMonthly', {
      // The type of chart we want to create
      type: 'line',

      data: {
        labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
        datasets: [{
          label: 'Monthly',
          backgroundColor: 'rgb(51, 202, 187)',
          borderColor: 'rgb(51, 202, 187)',
          data: [this.monthly[0],
          this.monthly[1],
          this.monthly[2],
          this.monthly[3],
          this.monthly[4],
          this.monthly[5],
          this.monthly[6],
          this.monthly[7],
          this.monthly[8],
          this.monthly[9],
          this.monthly[10],
          this.monthly[11]]
        }]
      },
      // Configuration options go here
      options: {}
    });

    this.cd.detectChanges();
  }


}
