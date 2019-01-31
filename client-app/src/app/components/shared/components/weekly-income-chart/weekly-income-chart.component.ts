import { Component, OnInit, AfterViewInit, ChangeDetectorRef, Input } from '@angular/core';
import { Chart } from 'chart.js';

@Component({
  selector: 'app-weekly-income-chart',
  templateUrl: './weekly-income-chart.component.html',
  styleUrls: ['./weekly-income-chart.component.css']
})
export class WeeklyIncomeChartComponent implements OnInit, AfterViewInit {
  chartWeekly: any;
  @Input() weekly: any;

  constructor(private cd: ChangeDetectorRef) { }

  ngOnInit() {
  }

  ngAfterViewInit() {
    console.log('Constructing chart');
    this.chartWeekly = new Chart('canvasWeekly', {
      // The type of chart we want to create
      type: 'line',

      data: {
        labels: ['Week 1.', 'Week 2.', 'Week 3.', 'Week 4.', 'Week 5.', 'Week 6.'],
        datasets: [{
            label: 'Weekly',
            backgroundColor: 'rgb(51, 202, 187)',
            borderColor: 'rgb(51, 202, 187)',
            data: [this.weekly[0],
            this.weekly[1],
            this.weekly[2],
            this.weekly[3],
            this.weekly[4],
            this.weekly[5]]
        }]
    },
    // Configuration options go here
    options: {}
    });
    this.cd.detectChanges();
  }

}
