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

import { UserService, AuthenticationService } from '../../service/index';

import {MdlSnackbarService, MdlDialogService} from '@angular-mdl/core';
import {  User } from '../../model/index';

const emailValidator = Validators.pattern('^[a-z]+[a-z0-9._]+@[a-z]+\.[a-z.]{2,5}$');
@Component({
  selector: 'app-basic-info',
  templateUrl: './basic-info.component.html',
  styleUrls: ['./basic-info.component.css']
})
export class BasicInfoComponent implements OnInit {


  public form: FormGroup;
  


  ngOnInit() {
  
   
  }


  constructor(
  	private fb: FormBuilder, 
  	private userService: UserService,
  	private dialogService: MdlDialogService,
    private mdlSnackbarService: MdlSnackbarService,
    private authenticationService: AuthenticationService,
  	) {

	var user = this.authenticationService.currentUser;
	var nickName = new FormControl(user.nickName,Validators.required);
  	var signature = new FormControl(user.signature, );
  	var gender = new FormControl(user.gender,);

    this.form = fb.group({
      'nickName': nickName,
      'signature': signature,
      'gender': gender
    });
 
  }

  public onSubmit() {
  	var me = this;

    let result = me.dialogService.confirm('Would you like to update information?', 'No', 'Yes', 'Excuse me');
  
    result.subscribe( () => {
            var formValue = me.form.getRawValue();
		    var temp = new User();
		    temp.nickName = formValue.nickName;
		    temp.signature = formValue.signature;
		    temp.gender = formValue.gender;

		    me.userService.updateBasicInfo(temp)
		      .then((result)=> {
		          if (result.success) {
		             var user = this.authenticationService.currentUser;
				     user.nickName = temp.nickName;
				     user.signature = temp.signature;
				     user.gender = temp.gender;
		          } 
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
