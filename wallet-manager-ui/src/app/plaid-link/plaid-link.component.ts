import { Component, OnInit, Output, EventEmitter, Input  } from '@angular/core';
import { PlaidService } from '../services/plaid.service';
import { PostService } from '../services/post.service';
import { ILinkedAccounts, IResponse } from '../interface/IResponse';
import { IPublicTokenRequest } from "../interface/IPublicTokenRequest";


declare var Plaid: any;


@Component({
  selector: 'app-plaid-link',
  templateUrl: './plaid-link.component.html',
  styleUrls: ['./plaid-link.component.css']
})
export class PlaidLinkComponent implements OnInit {
   linkToken: string = "";
   token: IResponse;
  @Input() userId!: number; 
  @Output() close = new EventEmitter<void>();
  @Output() finish = new EventEmitter<void>();
  linkedAccounts: ILinkedAccounts[] = [];


  constructor(private plaidService: PlaidService,private service:PostService) {}

  ngOnInit(){

    this.ngOnInitLoad();
  }

  ngOnInitLoad() {
    this.linkedAccounts = [];
    console.log("called ng init load for linked account modal for the user: ", this.userId);

    this.service.getLinkedAccounts<IResponse>(this.userId).subscribe(linkedAccounts => {
      console.log("Obtained the accounts: ", linkedAccounts);
      
      linkedAccounts.responseBody.forEach((element: ILinkedAccounts) => {
        const linkedAccount:ILinkedAccounts = element as ILinkedAccounts;
        console.log("individual linked account : ", linkedAccount);  
        this.linkedAccounts.push(linkedAccount);
        console.log("size after adding : ", this.linkedAccounts.length);
      });
    });



  }


  generateLinkToken() {
    // Replace the user ID with your actual logged-in user
    console.log("Link button clicked for the user id: ", this.userId);
    this.service.createLinkToken<IResponse>(this.userId).subscribe(token => {
      this.linkToken = token.responseBody;
      console.log("Received link token:", this.linkToken);
      this.initializePlaid(this.linkToken);
    });
  }

  initializePlaid(token: string) {
    this.close.emit();
    const handler = Plaid.create({
      token: token,

      onSuccess: (public_token: string, metadata: any) => {
        console.log("Public token received:", public_token);

        const requestBody: IPublicTokenRequest = {
          publicToken: public_token,
          userId: this.userId
        }

        this.service.exchangePublicToken(requestBody).subscribe(accessToken => {
          console.log("Access token stored on backend:", accessToken);

          this.finish.emit();
        });
      },

      onExit: (error: any, metadata: any) => {
        console.log("User exited Plaid Link:", metadata);
      }
    });

    // Launch the Plaid popup automatically
    handler.open();
  }
}
