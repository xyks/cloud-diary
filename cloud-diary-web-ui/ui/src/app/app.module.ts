import { BrowserModule } from '@angular/platform-browser';

import { NgModule } from '@angular/core';

import { FormsModule,ReactiveFormsModule }   from '@angular/forms';
import { HttpModule }    from '@angular/http';
import { CommonModule,  HashLocationStrategy, LocationStrategy }    from '@angular/common';


import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './com/login/login.component';
import { LoginFormComponent } from './com/login-form/login-form.component';

import { AuthenticationService , DiaryService, AuthGuardService, UserService, KeycodeService } from './service/index';
import { FooterComponent } from './com/footer/footer.component';
import { HomeComponent } from './com/home/home.component';
import { RichtextComponent } from './com/richtext/richtext.component';
import { MydatepickerComponent } from './com/mydatepicker/mydatepicker.component';

import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MdlModule, MdlDialogModule} from '@angular-mdl/core';
import { UserCenterComponent } from './com/user-center/user-center.component';
import { BasicInfoComponent } from './com/basic-info/basic-info.component';
import { BasicImageComponent } from './com/basic-image/basic-image.component';
import { UserSecurityComponent } from './com/user-security/user-security.component';
import { AboutComponent } from './com/about/about.component';
import { KeycodeComponent } from './com/keycode/keycode.component';
import {BlockUIModule} from 'primeng/primeng';
import {PanelModule} from 'primeng/primeng';



@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    LoginFormComponent,
    FooterComponent,
    HomeComponent,
    RichtextComponent,
    MydatepickerComponent,
    UserCenterComponent,
    BasicInfoComponent,
    BasicImageComponent,
    UserSecurityComponent,
    AboutComponent,
    KeycodeComponent
  ],
  entryComponents: [
    KeycodeComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpModule,
    BrowserAnimationsModule,
    MdlModule,
    MdlDialogModule,
    BlockUIModule,
    PanelModule,


  ],
  providers: [ 
    AuthenticationService,
    DiaryService,
    AuthGuardService,
    UserService,
    KeycodeService,
    {provide: LocationStrategy, useClass: HashLocationStrategy}
    ],
  bootstrap: [AppComponent]
})
export class AppModule { }
