import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { LoginComponent } from './com/login/login.component';
import { HomeComponent } from './com/home/home.component';
import { AuthGuardService } from './service/index';
import { UserCenterComponent } from './com/user-center/user-center.component';
import { BasicInfoComponent } from './com/basic-info/basic-info.component';
import { AboutComponent } from './com/about/about.component';

const routes: Routes = [

    { path: 'login', component: LoginComponent },
    { path: 'me', component: UserCenterComponent },
    { path: 'about', component: AboutComponent },
    { path: 'home', component: HomeComponent, canActivate: [AuthGuardService] },

    // otherwise redirect to home
    { path: '**', redirectTo: 'home' }

];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
