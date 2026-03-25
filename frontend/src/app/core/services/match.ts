import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AttemptResponse, Match } from '../../shared/models/match-model';
import { environment } from '../../../environments/environment'; 

@Injectable({
  providedIn: 'root'
})
export class MatchService {

  private api = environment.apiUrl;

  constructor(private http: HttpClient) {}

  createMatch(userId: string) {
    return this.http.post<Match>(`${this.api}/matches`, { userId });
  }

  getMatchById(id: number) {
    return this.http.get<Match>(`${this.api}/matches/${id}`);
  }

  submitAttempt(matchId: number, payload: { sequence: string[] }) {
    return this.http.post<AttemptResponse>(`${this.api}/matches/${matchId}/attempts`, payload);
  } 

  getInProgressMatches(userId: string) {
    return this.http.get<Match[]>(
      `${this.api}/matches?status=IN_PROGRESS&userId=${userId}`
    );
  }
  
}
