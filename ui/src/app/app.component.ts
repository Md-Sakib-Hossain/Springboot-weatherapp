import { Component, OnInit } from '@angular/core';
import { MasterService } from './service/master.service';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { Chart, registerables } from 'chart.js';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  imports: [CommonModule, HttpClientModule, FormsModule],
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  weatherData: any;
  city: string = '';
  range: string = '';
  selectedData: any = null;
  weatherChart: any = null;


  constructor(private masterService: MasterService) {
    Chart.register(...registerables);
  }


  ngOnInit(): void {
    this.city = 'Dhaka';
    this.fetchWeather();
  }


  fetchWeather(): void {
    if (this.city.trim()) {
      this.masterService.getWeather(this.city).subscribe(data => {
        this.weatherData = data;
        this.updateChart();
        console.log(this.weatherData);
      });
    }
  }


  setTimeRange(range: string): void {
    this.range = range;
    this.updateChart();
  }


  getRangeLimit(): number {
    switch (this.range) {
      case '24hours': return 24;
      case '48hours': return 48;
      case '72hours': return 72;
      case '1week': return 168;
      case '1month': return 720;
      default: return 10;
    }
  }


  selectRow(data: any): void {
    this.selectedData = data;
  }


  updateChart(): void {
    if (!this.weatherData?.length) return;


    const limit = this.getRangeLimit();
    const data = this.weatherData.slice(-limit);
   
    const labels = data.map((item: any) => {
      const time = item.time.split('T')[1];
      return time.substring(0, 5); // Show only HH:mm
    });
   
    const temperatures = data.map((item: any) => item.temperature);


    if (this.weatherChart) {
      this.weatherChart.destroy();
    }


    const ctx = document.getElementById('weatherChart') as HTMLCanvasElement;
    this.weatherChart = new Chart(ctx, {
      type: 'line',
      data: {
        labels: labels,
        datasets: [{
          label: 'Temperature (°C)',
          data: temperatures,
          borderColor: 'rgb(75, 192, 192)',
          tension: 0.1,
          fill: false
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          y: {
            beginAtZero: false,
            title: {
              display: true,
              text: 'Temperature (°C)'
            }
          },
          x: {
            title: {
              display: true,
              text: 'Time'
            }
          }
        },
        plugins: {
          title: {
            display: true,
            text: `Temperature Variation in ${this.city}`
          }
        }
      }
    });
  }
}
