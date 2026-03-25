import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RegisterRequest, AuthUser } from '../../shared/models/auth-model';
import { environment } from '../../../environments/environment'; 


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private api = environment.apiUrl;

  user = signal<any | null>(this.getUserFromStorage());

  constructor(private http: HttpClient) {}

  login(data: { username: string; password: string }) {
    return this.http.post(`${this.api}/auth/login`, data);
  }

  register(data: RegisterRequest) {
    return this.http.post<AuthUser>(`${this.api}/users`, data);
  }

  setUser(user: any) {
    localStorage.setItem('user', JSON.stringify(user));
    this.user.set(user);
  }

  logout() {
    localStorage.removeItem('user');
    this.user.set(null);
  }

  private getUserFromStorage() {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  }
}
