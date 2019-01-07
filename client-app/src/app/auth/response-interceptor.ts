import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
    constructor(private router: Router) {}

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(request).pipe(catchError(err => {
            if (err.status === 401) {
              this.router.navigate(['/error', 401]);
            } else if (err.status === 403) {
              this.router.navigate(['/error', 401]);
            } else  if (err.status === 404) {
              this.router.navigate(['/error', 404]);
            }
            const error = err.error.message || err.statusText;
            return throwError(err);
        }));
    }
}
