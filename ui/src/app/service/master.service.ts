import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class MasterService {

  apiUrl: string = 'http://localhost:8080/weather?city=';  

  constructor(private http: HttpClient) { }

  getWeather(city: string) {
    return this.http.get(this.apiUrl + city);
  }
}
