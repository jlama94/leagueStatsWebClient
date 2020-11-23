// export interface SummonerData {
//   userName?: string;
//   championId?: number[];
//   timeStap?: string[];
// }

import {Match} from './match';

export interface SummonerData {
  userName?: string;
  matches: [Match];
}
