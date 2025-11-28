import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PlaidService {

  private baseUrl = "http://localhost:8082/api/plaid";

  constructor(private http: HttpClient) {}

  // Step 1: Get link token from backend
  createLinkToken(userId: string): Observable<string> {
    return this.http.get(`${this.baseUrl}/create_link_token?userId=${userId}`, { responseType: 'text' });
  }

  // Step 2: Send public token to backend
  exchangePublicToken(publicToken: string): Observable<string> {
    return this.http.post(`${this.baseUrl}/exchange_public_token`,
      { publicToken: publicToken },
      { responseType: 'text' }
    );
  }
}
