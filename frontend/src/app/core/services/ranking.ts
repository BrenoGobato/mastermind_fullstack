import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RankingItem } from '../../shared/models/ranking-model';

@Injectable({
  providedIn: 'root'
})
export class RankingService {
  private http = inject(HttpClient);
  private api = 'http://localhost:8080';

  getRanking() {
    return this.http.get<RankingItem[]>(`${this.api}/ranking`);
  }
}
