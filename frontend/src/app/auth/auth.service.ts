import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { environment } from '../../environment';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private token: string | null = null;
  private username: string | null = null;
  private base = `${environment.apiBaseUrl}/auth`;

  constructor(private http: HttpClient) {}

  getToken() { return this.token; }
  isLoggedIn() { return !!this.token; }
  getUsername() { return this.username; }

  login(username: string, password: string) {
    return this.http.post<{ token: string }>(`${this.base}/login`, { username, password })
      .pipe(tap(res => {
        this.token = res.token;
        this.username = username;
      }));
  }

  register(username: string, password: string) {
    return this.http.post<{ token: string }>(`${this.base}/register`, { username, password })
      .pipe(tap(res => {
        this.token = res.token;
        this.username = username;
      }));
  }

  logout() {
    this.token = null;
    this.username = null;
  }
}