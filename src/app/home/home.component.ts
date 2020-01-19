import { Component, OnInit } from '@angular/core';
import {MatchesService} from '../httpClient/services/matches.service';

import * as Highcharts from 'highcharts';




@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})




export class HomeComponent implements OnInit {
  championNames = [
    '01-01-2020',
    '01-02-2020',
    '01-03-2020',
    '01-04-2020',
    '01-05-2020',
    '01-06-2020',
    '01-07-2020'
  ];
  Highcharts: typeof Highcharts = Highcharts;
  chartType = 'chart';

  chartOptions: Highcharts.Options = {
    chart: {
      type: 'column',
    },
    xAxis: {
      categories: this.championNames,
      title: {
        text: 'Dates'
      }
    },
    yAxis: {
      title: {
        text: 'Champion Usage'
      }
    }
    ,
    title: {
      text: 'Champion Usage'
    },
    subtitle: {
      text: 'Amount of times you have played a champion within 7 days'
    },

    series: [
      {
        type: 'column',
        allowPointSelect: true,
        name: 'Syndra', // Name of the series

        /*
            how many times have you used the champion per day?
            Monday: 11,
            Tuesday: 2,
            Wed: 3,
            Thurs: 5,
            Friday: 2
            Sat: 1,
            Sun: 1
         */

        data: [11, 2, 3, 5, 2, 1, 1]
      },

      {
        type: 'column',
        name: 'Riven',
        data: [3, 2, 1, 1, 2, 5, 6]
      },

      {
        type: 'column',
        name: 'Garen',
        data: [2, 2, 10, 5, 6, 7, 2]
      },

      {
        type: 'column',
        name: 'Teemo',
        data: [10, 10, 2, 3, 1, 1, 2]
      },

      {
        type: 'column',
        name: 'Warwick',
        data: [4, 0, 0, 0, 0, 0, 2]
      }
    ]
  };


  constructor(private matchService: MatchesService) { }


  ngOnInit() {
  }
}
