import { Component, OnInit } from '@angular/core';

import {
  FormGroup,
  FormControl,
  Validators,
  FormBuilder
} from '@angular/forms';
import 'rxjs/add/operator/filter';
import {
  Router,
  ActivatedRoute
} from '@angular/router';

import { Title } from '@angular/platform-browser';

import {  AuthenticationService } from '../../service/index';

import {MdlSnackbarService, MdlDialogService} from '@angular-mdl/core';
import {  User } from '../../model/index';

import { PasswordValidation } from '../../util/passwordvalidation';


@Component({
  selector: 'app-user-security',
  templateUrl: './user-security.component.html',
  styleUrls: ['./user-security.component.css']
})
export class UserSecurityComponent implements OnInit {


  public form: FormGroup;
  


  ngOnInit() {
  
   
  }


  constructor(
  	private fb: FormBuilder, 
  	private dialogService: MdlDialogService,
    private mdlSnackbarService: MdlSnackbarService,
    private authenticationService: AuthenticationService,
  	) {

	var user = this.authenticationService.currentUser;
	var password = new FormControl('',Validators.required);
  	var newPassword = new FormControl('', Validators.required);
  	var confirmPassword = new FormControl('',Validators.required);

    this.form = fb.group({
      'password': password,
      'newPassword': newPassword,
      'confirmPassword': confirmPassword
    }, {
      validator: PasswordValidation.MatchPassword 
    });
 
  }

  public onSubmit() {
  	var me = this;

    let result = me.dialogService.confirm('Would you like to change password?', 'No', 'Yes', 'Excuse me');
  
    result.subscribe( () => {
            var formValue = me.form.getRawValue();
		    me.authenticationService.changePassword(formValue.password, formValue.newPassword)
		      .then((result)=> {
		          me.mdlSnackbarService.showSnackbar({
		             message: result.message,
		          });
		      });
		      
    },
    (err: any) => {
     
    }
   );






  }


}
