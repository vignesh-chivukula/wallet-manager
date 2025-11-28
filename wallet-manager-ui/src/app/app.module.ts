import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { WalletManagerLoginComponent } from './wallet-manager-login/wallet-manager-login.component';
import { PostService } from './services/post.service';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule }   from '@angular/forms';
import { MatSidenavModule  } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatMenuModule } from '@angular/material/menu';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { WalletManagerHomeComponent } from './wallet-manager-home/wallet-manager-home.component';
import { RouterModule } from '@angular/router';
import { CanvasJSAngularChartsModule } from '@canvasjs/angular-charts';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { WalletManagerHeaderComponent } from './wallet-manager-header/wallet-manager-header.component';
import { WalletManagerSignupComponent } from './wallet-manager-signup/wallet-manager-signup.component';
import { walletmanagerDetailedInfoComponent } from './wallet-manager-detailed-info/wallet-manager-detailed-info.component';
import { PlaidLinkComponent } from './plaid-link/plaid-link.component';
import { BudgetManagerComponent } from './budget-manager/budget-manager.component';


@NgModule({
  declarations: [
    AppComponent,
    WalletManagerLoginComponent,
    WalletManagerHomeComponent,
    WalletManagerHeaderComponent,
    WalletManagerSignupComponent,
    walletmanagerDetailedInfoComponent,
    PlaidLinkComponent,
    BudgetManagerComponent
  ],
  imports: [
    HttpClientModule,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forRoot([
      {path: 'wallet-manager-home', component: WalletManagerHomeComponent},
      {path: 'wallet-manager-login', component: WalletManagerLoginComponent},
      {path: 'wallet-manager-signup', component: WalletManagerSignupComponent},
      {path: 'wallet-manager-detail', component: walletmanagerDetailedInfoComponent},
      {path: 'plaid-link', component: PlaidLinkComponent}
    ]),
    CanvasJSAngularChartsModule,
    NgbModule,
    MatSidenavModule,
    MatListModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatMenuModule,
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule
  ],
  providers: [PostService],
  bootstrap: [AppComponent]
})
export class AppModule { }
