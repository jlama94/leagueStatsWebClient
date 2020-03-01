import { Component, OnInit } from '@angular/core';
import { MatchesService} from '../httpClient/services/matches.service';
import {Label} from 'ng2-charts';
import {SummonerData} from '../model/summoner-data';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})




export class HomeComponent implements OnInit {

  // variables
   chartType;

   chartData = []; // ChartDataSet[]

   chartLabels = []; // The X-axis values, Label[]

   isChartReady = false;

   summonerData: SummonerData; // object holding values from backend


  constructor(private matchService: MatchesService) {}

  // https://stackoverflow.com/questions/46909164/data-or-datasets-field-are-required-to-render-char-bar-in-angular-4

  ngOnInit() {
    this.setData('bar');
  }



  setData(bartype: string) {
    this.chartType = bartype;
    const garen = {
      data: [1, 0, 2, 3, 5, 8], label: 'Garen'
    };

    const teemo = {
      data: [3, 4, 5, 1, 0, 0], label: 'Teemo'
    };

    this.matchService.getSummonerData().subscribe(value =>  {
      this.chartData.push(garen);
      this.chartData.push(teemo);
      this.chartLabels = value.timeStap;
      this.isChartReady = true;
    }, error1 =>  {
      this.isChartReady = false;
      console.log(error1);
      return;
    });
  }





  /**
   * Here there could be a function that returns an array, and we just push it to the chartData array.
   */
  enterNewChampion() {
    const data = {
      data: [10, 2, 8, 0, 0, 2, 1], label: 'Warwick'
    };
    this.chartData.push(data);
  }

  /**
   * Just push a new label/date to the labels array.
   */
  enterNewDate() {
    const date: Label = '01-08-2020';
    this.chartLabels.push(date);
  }

}
