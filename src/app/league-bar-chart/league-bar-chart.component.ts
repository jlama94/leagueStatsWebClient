import { Component, OnInit } from '@angular/core';
import { ChartDataSets, ChartOptions, ChartType } from 'chart.js';
import { Label } from 'ng2-charts';
import {MatchesService} from '../httpClient/services/matches.service';

@Component({
  selector: 'app-league-bar-chart',
  templateUrl: './league-bar-chart.component.html',
  styleUrls: ['./league-bar-chart.component.css']
})

/**
 *  Problemas:
 *  - Muchos juegos durante el mismo dia.
 *  - Campeones deberian ser tipo String no tipo numero.
 *
 *
 *
 * Ideas:
 * - Agrupar todos los matches de cada dia.
 * - Linkear el numero del champion con su nombre.
 * - Series deberian ser los champion names.
 * - barChartLabels deberian ser Dates.
 *
 *
 *
 *
 *
 *
 */

export class LeagueBarChartComponent implements OnInit {

  public barChartOptions: ChartOptions = {
    responsive: true,
  };
  public barChartLabels: Label[] = []; // should be dates.
  public barChartType: ChartType = 'bar';
  public barChartLegend = true;
  public barChartPlugins = [];


  // variables.
  championNames: Array<any> = [];
  isChartReady: boolean;
  dates: Array<any> = [];
  userName: string;



  public barChartData: ChartDataSets[] = [

/*
    {
      data: [65, 59, 80, 81, 56, 55, 40], label: 'Series A'
    },

    {
      data: [28, 48, 40, 19, 86, 27, 90], label: 'Series B'
    }
 */

    {
      /*
      Example: On monday I played 'x' champ 4 times, tuesday I played same champ 7 times... so on.
          A[4], A[7]


      So each serie should be a data array where each slot is the number of times
      I played that champion, and the label is just the champion name,


      From the backend:
        - An array of strings representing the dates (7 dates).
        - Array storing in each slot the number of times champion played per day.

        A Map? Map<String[], String>
            -----> [12, 3, 4, 5], Teemo


       */

      data: [4, 7, 0, 2], label: 'Teemo'
    },

    {
      data: [2, 1, 8, 4], label: 'Janna'
    }




  ];




  constructor(private service: MatchesService) { }

  ngOnInit() {

  }


  setDates() {
    const datesArray: Array<string> =
      ['02/12/2020', '02/13/2020', '02/14/2020', '02/15/2020', '02/16/2020', '02/17/2020', '02/18/2020'];
    if (datesArray === null) {
      this.isChartReady = false;
    }

    for (let date  of datesArray) {
      this.barChartLabels.push(date);
    }

    this.isChartReady = true;
  }

  setData() {
    this.service.getSummonerData().subscribe(value => {
      value.matches.forEach(match => {
//        console.log('Champion ID: ' + match.champion);
        this.championNames.push(match.champion);
      });
    }, error => {
      console.log('Cannot retrieve: ' + error);
    });
  }

}
