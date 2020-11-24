import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {MatchResponse} from '../../model/match-response';


const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  })
};

@Injectable({
  providedIn: 'root'
})

export class MatchesService {

  constructor(private httpClient: HttpClient) { }

  getMatches() {
    // return this.httpClient.get('http://localhost:9090/matches/pTeemo', httpOptions);
    return this.httpClient.get<MatchResponse>('http://localhost:9090/matches/pTeemo', httpOptions);
  }
}
