import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterPageComponent } from '../register-page/register-page.component';
import { StartPageComponent } from '../start-page/start-page.component';
import { GetStartedComponent } from '../get-started/get-started.component';
import { HomePageComponent } from '../home-page/home-page.component';
import { AccountDetailsComponent } from '../account-details/account-details.component';

const routes: Routes = [
  { path: '', component: StartPageComponent },
  { path: 'register', component: RegisterPageComponent },
  { path: 'getStarted', component: GetStartedComponent },
  { path: 'home', component: HomePageComponent},
  { path: 'accDetails', component: AccountDetailsComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
