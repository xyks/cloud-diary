import { Component, OnInit } from '@angular/core';


import { User, ResponseDTO } from '../../model/index';
import { Router } from '@angular/router';
import { AuthenticationService, UserService } from '../../service/index';

import {MdlSnackbarService} from '@angular-mdl/core';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent implements OnInit {

 showConnect: boolean;
 showMore: boolean ;
  user: User;

blocked: boolean;

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
    private mdlSnackbarService: MdlSnackbarService,
    private userService: UserService,
  
  ) {
  	 this.user = new User();
     this.blocked = false;
  }
 
  ngOnInit(): void {
    this.authenticationService.logout();
    this.showConnect = true;
    this.showMore = true;
    
  }

  clickConnect(): void {
    this.showConnect = false;
  }


  signup(): void{
    var me = this;
    
    if(!me.user.password || !me.user.email){
        me.mdlSnackbarService.showSnackbar({
           message: "Please fill in both email and password to sign up.",
       });
       return;
    }

    if(me.user.password.length >= 3){
        me.showConnect = true;
    }else{
        me.mdlSnackbarService.showSnackbar({
           message: "Password should be at least 3 characters.",
       });
      return;
    }
    me.blocked = true;
    me.authenticationService.register(me.user.email, me.user.password)
      .then((result)=> {
          me.mdlSnackbarService.showSnackbar({
               message: result.message,
            });
         me.blocked = false; 
      },()=>{
         me.mdlSnackbarService.showSnackbar({
               message: "Failed to sign up, please try later.",
            });
             me.blocked = false; 
      });



  }

forgetPassword(): void{
    var me = this;
    
    if(!me.user.email){
        me.mdlSnackbarService.showSnackbar({
           message: "Please fill in email to get back password.",
       });
       return;
    }

    if(me.user.password.length >= 3){
        me.showConnect = true;
    }else{
        me.mdlSnackbarService.showSnackbar({
           message: "Password should be at least 3 characters.",
       });
      return;
    }
     me.blocked = true; 
    me.authenticationService.forgetPassword(me.user.email)
      .then((result)=> {
          me.mdlSnackbarService.showSnackbar({
               message: result.message,
            });
           me.blocked = false; 
      },()=>{
         me.mdlSnackbarService.showSnackbar({
               message: "Failed to get back password, please try later.",
            });
             me.blocked = false; 
      });



  }

  



  send(): void {
    
    var me = this;
    if(me.user.password.length >= 3){
        me.showConnect = true;
    }

     me.blocked = true; 
    me.authenticationService.login(me.user.email, me.user.password)
      .then((result)=> {
      me.blocked = false;
          if (result.success) {
              
              me.userService.get()
                .then((userResult)=>{
                    
                    if(userResult.success){
                       
                        me.authenticationService.initUser(JSON.stringify(userResult.content));
                        me.router.navigate(['/home']);  
                        
                         me.mdlSnackbarService.showSnackbar({
                             message: result.message,
                         });
                    }else{
                         me.mdlSnackbarService.showSnackbar({
                             message: userResult.message,
                         });
                    }

                   
                   
                },()=>{
                 me.mdlSnackbarService.showSnackbar({
                   message: "Failed to  login, please try later.",
                });
                 
                });
              
          }else{
            me.mdlSnackbarService.showSnackbar({
               message: result.message,
            });
            
          }
          
      },()=>{
         me.mdlSnackbarService.showSnackbar({
               message: "Failed to login, please try later.",
            });
             me.blocked = false; 
      });



  }


  more(): void{
    var me = this;
    me.showMore = false;

  }

}
