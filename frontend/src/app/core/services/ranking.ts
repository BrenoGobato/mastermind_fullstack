import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RankingItem } from '../../shared/models/ranking-model';
import { environment } from '../../../environments/environment'; 

@Injectable({
  providedIn: 'root'
})
export class RankingService {
  private http = inject(HttpClient);
  private api = environment.apiUrl;

  getRanking() {
    return this.http.get<RankingItem[]>(`${this.api}/ranking`);
  }
}
 