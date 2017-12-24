import { Component, OnInit } from '@angular/core';

import {  KeycodeService  } from '../../service/index';
import {MdlSnackbarService, MdlDialogReference, MdlDialogService} from '@angular-mdl/core';

@Component({
  selector: 'app-keycode',
  templateUrl: './keycode.component.html',
  styleUrls: ['./keycode.component.css']
})
export class KeycodeComponent implements OnInit {

  keyCode: string;	
  showInfo: boolean;
  constructor(
     private dialog: MdlDialogReference,
  	 private keycodeService: KeycodeService,
  	 private mdlSnackbarService: MdlSnackbarService,
     private dialogService: MdlDialogService,
  ) { }

  ngOnInit() {
  }

  submit(){
  	var me = this;
  	me.keycodeService.sendKeyCode(me.keyCode).then(
  		result =>{
  			me.mdlSnackbarService.showSnackbar({
	            message: result.message,
	        });
  			if(result.success){
          if(result.code == 0){
              me.dialog.hide();
          }else if (result.code > 0){
                let result = me.dialogService.confirm(
                  "Key Code is used to encrypt your diary content, and it can not be changed later. If you forget it, you will lose all the diary records. Even" +" website admin can not get it back as it is never saved to data center."
                  +" Save it or re-enter? "
                  , 'Re-enter', 'Save', 'Attention');
                // if you need both answers
                result.subscribe( () => {
                    me.keycodeService.saveKeyCode(me.keyCode).then(
                      res=>{
                          me.mdlSnackbarService.showSnackbar({
                              message: res.message,
                          });

                          if(res.success){
                             me.dialog.hide();
                          }
                      },()=>{
                          me.mdlSnackbarService.showSnackbar({
                              message: "Failed to save key code, please try later.",
                          });
                      }
                    );
                  },
                  (err: any) => {
                    
                  }
                );
                
          }

  				
  			}
  		}
  	),()=>{
        me.mdlSnackbarService.showSnackbar({
              message: "Failed to check key code, please try later.",
          });

    };
  }

}
