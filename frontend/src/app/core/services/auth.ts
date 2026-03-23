import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private api = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  login(data: { username: string; password: string }) {
    return this.http.post(`${this.api}/auth/login`, data);
  }

  register(data: any) {
    return this.http.post(`${this.api}/users`, data);
  }
}