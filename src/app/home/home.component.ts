import {Component, OnInit} from '@angular/core';
import {MatchesService} from '../httpClient/services/matches.service';
import {SummonerData} from '../model/summoner-data';
import {Match} from '../model/match';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})


// https://stackoverflow.com/questions/46909164/data-or-datasets-field-are-required-to-render-char-bar-in-angular-4
export class HomeComponent implements OnInit {

  // variables

  userName: string;

  isChartReady = false;

  champions: [Match];

  summonerData: SummonerData; // object holding values from backend



  constructor (private matchService: MatchesService) {}


  ngOnInit () {
    // this.setData('bar');
  }


  setData (bartype: string) {
    this.matchService.getSummonerData().subscribe(response => {
      // console.log(response);
      this.summonerData = response;

      this.champions = this.summonerData.matches;


      console.log(this.champions);

    }, error => {
      console.log('Could not get data from service.');
    });

  }
}
