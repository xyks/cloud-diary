import { Component, OnInit } from '@angular/core';

import { DiaryService, AuthenticationService, KeycodeService  } from '../../service/index';
import { Diary, User } from '../../model/index';

import {FormGroup, FormBuilder, Validators} from '@angular/forms';

import {MdlSnackbarService, MdlDialogService, MdlDialogReference} from '@angular-mdl/core';

import { KeycodeComponent } from '../keycode/keycode.component';

import {Router}     from '@angular/router';

declare const $: any;




@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  

   selectedDiary: Diary;
   tmpDiary: Diary;

   myForm: FormGroup;

   viewOnlyMode: boolean = true;
   isPreview: boolean = true;

   user: User;
  constructor(
    private diaryService: DiaryService,
   
    private formBuilder: FormBuilder,
    private authenticationService: AuthenticationService,
    private dialogService: MdlDialogService,
    private mdlSnackbarService: MdlSnackbarService,
    private keycodeService: KeycodeService,
    private router: Router
  ) {
    this.tmpDiary = new Diary();
    let today = new Date();
    this.selectedDiary = new Diary();
    this.selectedDiary.year = today.getFullYear();
    this.selectedDiary.month = (today.getMonth() + 1);
    this.selectedDiary.day =  today.getDate();
    this.viewOnlyMode= true;   
    this.user = this.authenticationService.currentUser;
  }

  ngOnInit(): void {
    
  }

  dateChange(date: Date) {
        var me = this;

        if(date.getFullYear() == me.selectedDiary.year && 
            date.getMonth() == me.selectedDiary.month &&
            date.getDate() == me.selectedDiary.day
        ){
           return;
        }


        if(!me.viewOnlyMode){
            
            let result = me.dialogService.confirm('Would you like to exit edit mode?', 'No', 'Yes', 'Excuse me');
        
            result.subscribe( () => {
                   me.refreshDiaryDate(date, me.selectedDiary);
                   me.selectedDiary.data = "";
                   me.viewOnlyMode = true;
                   me.isPreview = true;
        
              },
              (err: any) => {
                  var oldDate = new Date();
                  oldDate.setFullYear(me.selectedDiary.year);
                  oldDate.setMonth(me.selectedDiary.month);
                  oldDate.setDate(me.selectedDiary.day);
                  me.refreshDatePicker(oldDate);
              }
            );
             
        }else{
          me.refreshDiaryDate(date, me.selectedDiary);
          me.selectedDiary.data = "";
          me.isPreview = true;
        }
  }

  refreshDatePicker(date: Date){
     var datepicker = $('#mydatepicker').datepicker().data('datepicker');
     datepicker.selectDate(date);
  }

  preview(){
    var me = this;
    
    if(me.keycodeService.hasKeyCode()){

          me.diaryService.get(me.selectedDiary)
              .then(result=> {
                  me.mdlSnackbarService.showSnackbar({
                      message: result.message,
                  });
                  if (result.success) {
                      if(result.content && result.content.data){
                         result.content.data = me.keycodeService.decrypt( result.content.data);
                      }   
                      me.refreshDiaryData(result.content);
                      
                      me.isPreview = false;
                   
                  } else {
                     
                     
                  }
          });

    }else{

          let pDialog = me.dialogService.showCustomDialog({
            component: KeycodeComponent,
            
            isModal: true,
            styles: {'width': '350px'},
            clickOutsideToClose: true,
            enterTransitionDuration: 400,
            leaveTransitionDuration: 400
          });


  }

  }

  private edit(): void{
    this.viewOnlyMode = false;
  }

  private save(): void{
    var me = this;
    let result = me.dialogService.confirm('Would you like to save?', 'No', 'Yes', 'Excuse me');
    
    result.subscribe( () => {
        me.tmpDiary.year = me.selectedDiary.year;
        me.tmpDiary.month = me.selectedDiary.month;
        me.tmpDiary.day = me.selectedDiary.day;
        if(me.tmpDiary.data){
            me.tmpDiary.data = me.keycodeService.encrypt( me.tmpDiary.data);
        } 
        me.diaryService.save(me.tmpDiary)
              .then(result=> {
                  me.mdlSnackbarService.showSnackbar({
                    message: result.message,
                  });
                  if (result.success) {
                         
                      if(result.content && result.content.data){
                         result.content.data = me.keycodeService.decrypt( result.content.data);
                      }   

                      me.refreshDiaryData(result.content); 
                  } else {
                    
                  }

          });
      },
      (err: any) => {
        
      }
    );

    

  }
  
  private cancel(): void{
      var me = this;
      let result = me.dialogService.confirm('Would you like to discard changes?', 'No', 'Yes', 'Excuse me');
      
      result.subscribe( () => {
           me.viewOnlyMode = true;
           me.tmpDiary.data =  me.selectedDiary.data;
        },
        (err: any) => {
         
        }
      );
      
  
  }
  
   editorTextChange(content: string){
    this.tmpDiary.data = content;
  }


  private refreshDiaryDate(source: Date, target: Diary): Diary{
       target.year = source.getFullYear();
       target.month = source.getMonth();
       target.day = source.getDate();
       return target;
  }

  private refreshDiaryData(value: any): void{
      if(value){
          if(value.data){
            this.selectedDiary.data = value.data;
            this.tmpDiary.data = value.data;
          }else{
            this.selectedDiary.data = "";
            this.tmpDiary.data = "";
          }
          
          this.selectedDiary.createdDate = value.createdDate;
      }else{
          this.selectedDiary.data = "";
          this.selectedDiary.createdDate = null;
          this.tmpDiary.data = "";
      }
  }


  public logout(): void{
  debugger
      var me = this;

      me.keycodeService.setKeyCode('');
      me.authenticationService.logout();
      me.router.navigate(['/login'])
  }


}
