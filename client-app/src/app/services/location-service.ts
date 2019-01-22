import { Observable } from 'rxjs/Observable';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
@Injectable({
  providedIn: 'root'
})
export class LocationService {
  constructor(private http: HttpClient) { }

  decode(address: string): Observable<any> {
    // tslint:disable-next-line:max-line-length
    return this.http.get('https://maps.googleapis.com/maps/api/geocode/json?address=' + address + ',+CA&key=AIzaSyDxMEmiWGtvuSRvzBShDgvPbWYQgo3GEHQ');
  }
}
