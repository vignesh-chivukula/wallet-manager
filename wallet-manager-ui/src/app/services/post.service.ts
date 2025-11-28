import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';
import { ITransaction, ITransactionDetail, IUser } from '../interface/IResponse';
import { Observable } from 'rxjs';
import { IPublicTokenRequest } from '../interface/IPublicTokenRequest';



@Injectable()

export class PostService {

  private url = 'http://localhost:8082/v1/';

  private userContext = 'user/';
  private transactionsContext = 'transactions/';



  constructor(private httpClient: HttpClient) { }



  getPosts(){
    
    return this.httpClient.get(this.url);

  }

  authenticateUser<IResponse>(userName : String,password: String) {
     let finalUrl = this.url + this.userContext+ "authenticate";
     let User = {"email" : userName, "password":password};
     return this.httpClient.post<IResponse>(finalUrl,User);
  }

  createUser<IResponse>(userDetail : IUser){
    let finalUrl = this.url+this.userContext+"create";
    return this.httpClient.post<IResponse>(finalUrl,userDetail);
  }

  getUserDetails<IResponse>(userId : number){
    let finalUrl = this.url+this.transactionsContext+'home/'+userId;
    return this.httpClient.get<IResponse>(finalUrl);
  }

  getAllTransactionDetails<IResponse>(userId : number){
    let finalUrl = this.url+this.transactionsContext+'all/'+userId;
    return this.httpClient.get<IResponse>(finalUrl);
  }

  getTransactionDetailsByMonth<IResponse>(userId : number, monthId : String){
    let finalUrl = this.url+this.transactionsContext+'month/'+userId + '/'+monthId;
    return this.httpClient.get<IResponse>(finalUrl);
  }

  createTransactionDetail<IResponse>(transactionDetail:ITransactionDetail){
    let finalUrl = this.url+this.transactionsContext+"create";
    return this.httpClient.post<IResponse>(finalUrl,transactionDetail);
  }

  updateTransactionDetail<IResponse>(transactionDetail: ITransactionDetail){
    let finalUrl= this.url+this.transactionsContext+'update';
    return this.httpClient.put<IResponse>(finalUrl,transactionDetail);

  }

  deleteTransactionDetail<IResponse>(transactionDetail: ITransactionDetail){
    let finalUrl = this.url+this.transactionsContext+"delete";
    return this.httpClient.put<IResponse>(finalUrl,transactionDetail);
  }

  // Step 1: Get link token from backend
    createLinkToken<IResponse>(userId: number):Observable<IResponse> {
      let finalUrl = this.url+this.transactionsContext+"obtainPlaidToken/"+userId;
      return this.httpClient.get<IResponse>(finalUrl);
    }
  
    // Step 2: Send public token to backend
    exchangePublicToken<IResponse>(publicToken: IPublicTokenRequest): Observable<IResponse> {
      let finalUrl = this.url+this.transactionsContext+"exchange_public_token";
      return this.httpClient.post<IResponse>(finalUrl,publicToken);
    }

    getLinkedAccounts<IResponse>(userId: number):Observable<IResponse> {
      let finalUrl = this.url+this.transactionsContext+"get-linked-accounts/"+userId;
      return this.httpClient.get<IResponse>(finalUrl);
    }


}
