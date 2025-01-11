import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {take} from 'rxjs';
import {formatDate} from '@angular/common';
import {DeviceService} from '../../../service/device.service';
import {AuthenticationService} from '../../../service/authentication.service';
import {WeatherService} from '../../../service/weather.service';
import {Prediction} from '../../../model/Prediction';
import {PredictionValue} from '../../../model/PredictionValue';
import {Chart} from 'chart.js/auto';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.scss']
})
export class DetailsComponent implements OnInit {

  startDate!: Date;
  endDate!: Date;
  prediction: Prediction[] = [];
  results: PredictionValue[] = [];
  dateStartToShow!: string;
  dateEndToShow!: string;
  isCalendarPressed: boolean = false;
  address = '';
  numberDevice: number = 0;
  selectedId: number = 0;
  isAdmin: boolean = false;
  wasAlgorithmsTrained: boolean = false;
  isLoadingScreen: boolean = false;
  chart: any;
  detailsChart: any;
  detailsMAPEChart: any;
  isOnInit: boolean = false;
  isShowMore: boolean = false;
  isShowMoreArrowDown: boolean = false;

  constructor(private router: Router,
              private authService: AuthenticationService,
              private weatherService: WeatherService,
              private toastr: ToastrService,
              private deviceService: DeviceService) {
  }

  ngOnInit(): void {
    this.isOnInit = true;
    this.isAdmin = this.authService.getUserRole() === 'ADMIN';
    this.endDate = new Date(2022, 9, 30);
    this.startDate = new Date(2023, 9, 26);
    this.getSelectedDates(this.startDate + '-' + this.endDate);
    this.selectedId = +(atob(decodeURIComponent(this.router.url.split('/')[2])));
    this.deviceService.findDeviceById(this.selectedId).pipe(take(1)).subscribe({
      next: (response) => {
        this.address = response.address;
        this.numberDevice = response.deviceNumber;
      }
    });
  }

  handleCalendarOpening() {
    this.isCalendarPressed = !this.isCalendarPressed;
  }

  getSelectedDates(newValue: string) {
    this.isCalendarPressed = false;
    let parts = newValue.split(' ');
    let partsForComparing = newValue.split('-');

    this.startDate = new Date(+parts[3], this.getMonth(parts[1]), +parts[2]);
    this.endDate = new Date(+parts[12], this.getMonth(parts[10]), +parts[11]);

    if (Date.parse(partsForComparing[0]) > Date.parse(partsForComparing[1])) {
      this.dateStartToShow = formatDate(this.endDate, 'dd-MM-YYYY', 'en-US');
      this.dateEndToShow = formatDate(this.startDate, 'dd-MM-YYYY', 'en-US');
    } else {
      this.dateStartToShow = formatDate(this.startDate, 'dd-MM-YYYY', 'en-US');
      this.dateEndToShow = formatDate(this.endDate, 'dd-MM-YYYY', 'en-US');
    }
    if (!this.isOnInit) {
      this.getPredictionResult(this.dateStartToShow, this.dateEndToShow);
    }
    this.isOnInit = false;
  }

  getMonth(monthStr: string) {
    return new Date(monthStr + '-1-01').getMonth();
  }

  trainAlgorithms() {
    this.toastr.info('Your request is being processed. You will be notify when the train is done!', 'Info', {
      timeOut: 9000,
      extendedTimeOut: 1000,
      progressBar: true,
      closeButton: true
    });

    if (this.chart) {
      this.chart.destroy();
      this.isShowMore = false;
    }
    this.isLoadingScreen = true;
    this.weatherService.trainModels().pipe(take(1)).subscribe({
      next: (response: Prediction[]) => {
        this.prediction = response;
        this.wasAlgorithmsTrained = true;
        this.isLoadingScreen = false;
      }
    });
  }

  getPredictionResult(start: string, end: string) {
    if (this.chart) {
      this.chart.destroy();
      this.isShowMore = false;
    }

    if (this.detailsChart && this.detailsMAPEChart) {
      this.detailsChart.destroy();
      this.detailsMAPEChart.destroy();
      this.isShowMoreArrowDown = false;
    }

    this.isLoadingScreen = true;
    this.weatherService.predictOnPeriod(start + ' 00:00', end + ' 00:00', this.numberDevice).pipe(take(1)).subscribe({
      next: (response: PredictionValue[]) => {
        this.results = response;
        this.isLoadingScreen = false;
        this.isShowMore = true;
        this.createChart();
      },
      error: () => {
        this.isLoadingScreen = false;
      }
    });
  }

  createChart() {
    const labels = this.results.map(item => item.date);
    const real_values = this.results.map(item => item.real);
    const dt_values = this.results.map(item => item.dt);
    const rf_values = this.results.map(item => item.rf);
    const mlp_values = this.results.map(item => item.mlp);

    this.chart = new Chart('MyChart', {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [
          {
            label: 'Real',
            data: real_values,
            backgroundColor: 'rgba(0, 123, 255, 0.7)'
          },
          {
            label: 'DT',
            data: dt_values,
            backgroundColor: 'rgba(255, 159, 28, 0.7)'
          },
          {
            label: 'RF',
            data: rf_values,
            backgroundColor: 'rgba(40, 167, 69, 0.7)'
          },
          {
            label: 'MLP',
            data: mlp_values,
            backgroundColor: 'rgba(255, 193, 7, 0.7)'
          },
        ]
      },
      options: {
        aspectRatio: 2.5,
        plugins: {
          title: {
            display: true
          },
          legend: {
            display: true,
            labels: {
              font: {
                size: 14,
              }
            }
          },
          tooltip: {
            callbacks: {
              title: function () {
                return '';
              },
              label: function (tooltipItem) {
                const datasetLabel = tooltipItem.dataset.label || '';
                const dataPoint = tooltipItem.raw;
                return `${datasetLabel}: ${dataPoint} kWh`;
              }
            }
          }
        },
        scales: {
          x: {
            grid: {
              display: false
            },
            ticks: {
              font: {
                size: 14
              }
            }
          },
          y: {
            grid: {
              color: 'rgba(0, 0, 0, 0.1)'
            },
            ticks: {
              font: {
                size: 14
              },
              callback: function (value, index, values) {
                return value + ' kWh';
              }
            }
          }
        }
      }
    });
  }

  createDetailsChart() {
    const labels = ['Decision Tree', 'Random Forest', 'MLP'];
    const rmse_dt = this.results.map(item => item.rmse_dt);
    const rmse_rf = this.results.map(item => item.rmse_rf);
    const rmse_mlp = this.results.map(item => item.rmse_mlp);

    const mse_mlp = this.results.map(item => item.mse_mlp);
    const mse_dt = this.results.map(item => item.mse_dt);
    const mse_rf = this.results.map(item => item.mse_rf);

    const rmse = [rmse_dt[0], rmse_rf[0], rmse_mlp[0]];
    const mse = [mse_dt[0], mse_rf[0], mse_mlp[0]];

    this.detailsChart = new Chart('MyDetailsChart', {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [
          {
            label: 'RMSE',
            data: rmse,
            backgroundColor: 'rgba(255, 159, 28, 0.7)'
          },
          {
            label: 'MSE',
            data: mse,
            backgroundColor: 'rgba(40, 167, 69, 0.7)'
          },
        ]
      },
      options: {
        aspectRatio: 2.5,
        plugins: {
          title: {
            display: true
          },
          legend: {
            display: true,
            labels: {
              font: {
                size: 14,
              }
            }
          },
          tooltip: {
            callbacks: {
              title: function () {
                return '';
              },
              label: function (tooltipItem) {
                const datasetLabel = tooltipItem.dataset.label || '';
                const dataPoint = tooltipItem.raw;
                return `${datasetLabel}: ${dataPoint}`;
              }
            }
          }
        },
        scales: {
          x: {
            grid: {
              display: false
            },
            ticks: {
              font: {
                size: 14
              }
            }
          },
          y: {
            grid: {
              color: 'rgba(0, 0, 0, 0.1)'
            },
            ticks: {
              font: {
                size: 14
              },
              callback: function (value, index, values) {
                return value;
              }
            }
          }
        }
      }
    });
  }

  createMAPEChart() {
    const labels = ['Decision Tree', 'Random Forest', 'MLP'];

    const mape_dt = this.results.map(item => item.mape_dt);
    const mape_rf = this.results.map(item => item.mape_rf);
    const mape_mlp = this.results.map(item => item.mape_mlp);
    const mape = [mape_dt[0], mape_rf[0], mape_mlp[0]];


    this.detailsMAPEChart = new Chart('MyDetailsMAPEChart', {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [
          {
            label: 'MAPE',
            data: mape,
            backgroundColor: 'rgba(255, 193, 7, 0.7)'
          },
        ]
      },
      options: {
        aspectRatio: 2.5,
        plugins: {
          title: {
            display: true
          },
          legend: {
            display: true,
            labels: {
              font: {
                size: 14,
              }
            }
          },
          tooltip: {
            callbacks: {
              title: function () {
                return '';
              },
              label: function (tooltipItem) {
                const datasetLabel = tooltipItem.dataset.label || '';
                const dataPoint = tooltipItem.raw;
                return `${datasetLabel}: ${dataPoint} %`;
              }
            }
          }
        },
        scales: {
          x: {
            grid: {
              display: false
            },
            ticks: {
              font: {
                size: 14
              }
            }
          },
          y: {
            grid: {
              color: 'rgba(0, 0, 0, 0.1)'
            },
            ticks: {
              font: {
                size: 14
              },
              callback: function (value, index, values) {
                return value + '%';
              }
            }
          }
        }
      }
    });
  }

  displayShowMore() {
    this.isShowMoreArrowDown = true;
    this.isShowMore = false;
    this.createDetailsChart();
    this.createMAPEChart();
  }

  displayShowLess() {
    this.isShowMoreArrowDown = false;
    this.isShowMore = true;
    this.detailsChart.destroy();
    this.detailsMAPEChart.destroy();
  }

  closeInformationCard() {
    this.wasAlgorithmsTrained = false;
  }
}
