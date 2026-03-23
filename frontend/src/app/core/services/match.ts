import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Match } from '../../shared/models/match-model';

@Injectable({
  providedIn: 'root'
})
export class MatchService {

  private api = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  createMatch(userId: string) {
    return this.http.post<Match>(`${this.api}/matches`, { userId });
  }

  getMatchById(id: number) {
    return this.http.get<Match>(`${this.api}/matches/${id}`);
  }
}