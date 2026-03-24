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

  submitAttempt(matchId: number, payload: { sequence: string[] }) {
    return this.http.post(`${this.api}/matches/${matchId}/attempts`, payload);
  } 

  getInProgressMatches(userId: string) {
    return this.http.get<Match[]>(
      `${this.api}/matches?status=IN_PROGRESS&userId=${userId}`
    );
  }
  
}