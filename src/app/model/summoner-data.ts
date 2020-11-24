export class SummonerData {
  private _summonerName?: string;
  private _championChartData?: ChampionChartData;

  constructor(summonerName: string, championChartData: ChampionChartData) {
    this._summonerName = summonerName;
    this._championChartData = championChartData;
  }

  get summonerName(): string {
    return this._summonerName;
  }

  set summonerName(value: string) {
    this._summonerName = value;
  }

  get championChartData(): ChampionChartData {
    return this._championChartData;
  }

  set championChartData(value: ChampionChartData) {
    this._championChartData = value;
  }
}

export class ChampionChartData {
  private _chartData?: ChartDatum[];
  private _labels?: string[];

  constructor(chartData: ChartDatum[], labels: string[]) {
    this._chartData = chartData;
    this._labels = labels;
  }

  get chartData(): ChartDatum[] {
    return this._chartData;
  }

  set chartData(value: ChartDatum[]) {
    this._chartData = value;
  }

  get labels(): string[] {
    return this._labels;
  }

  set labels(value: string[]) {
    this._labels = value;
  }
}

export class ChartDatum {
  private _data?: number[];
  private _label?: string;

  constructor(data: number[], label: string) {
    this._data = data;
    this._label = label;
  }

  get data(): number[] {
    return this._data;
  }

  set data(value: number[]) {
    this._data = value;
  }

  get label(): string {
    return this._label;
  }

  set label(value: string) {
    this._label = value;
  }
}
