import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { StartPageComponent } from '../start-page/start-page.component';
import { RegisterPageComponent } from '../register-page/register-page.component';
import { GetStartedComponent } from '../get-started/get-started.component';
import { HomePageComponent } from '../home-page/home-page.component';
import { AccountDetailsComponent } from '../account-details/account-details.component';

@NgModule({
  declarations: [
    AppComponent,
    StartPageComponent,
    RegisterPageComponent,
    GetStartedComponent,
    HomePageComponent,
    AccountDetailsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
