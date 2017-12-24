import { Component, OnInit } from '@angular/core';

import { AppSettings } from '../../config/app-settings';
import {MdlSnackbarService, MdlDialogService} from '@angular-mdl/core';
import { AuthenticationService } from '../../service/index';

declare const $: any;
declare const Dropzone: any;


@Component({
  selector: 'app-basic-image',
  templateUrl: './basic-image.component.html',
  styleUrls: ['./basic-image.component.css']
})
export class BasicImageComponent implements OnInit {
   private uploadURL = AppSettings.API_APP+'user/image/upload';
   finalURL: string = 'url-for-upload';
   private myDropzone: any;	
   imageType: string;	


	blocked: boolean;

   constructor( private mdlSnackbarService: MdlSnackbarService,
   private authenticationService: AuthenticationService
   ) { 
  	 this.imageType = 'headPortrait';

   }

  ngOnInit() {
  	var me = this;	
	   me.myDropzone = new Dropzone("#dropzone", { 
	  	url:  me.finalURL ,

	  	maxFilesize: 0.5,
	  	autoProcessQueue: false,
	  	acceptedFiles: "image/*"
	  	});

	  	me.myDropzone.on("addedfile", function(file) {
	  		var oldFile = me.myDropzone.getAcceptedFiles()[0];
	  		if(oldFile){
	  			me.myDropzone.removeFile(oldFile);
	  		}
	  		
	  		file.previewElement.addEventListener("click", function() {
		    	me.myDropzone.removeFile(file);
		  });

	  	});

	  	me.myDropzone.on("success", function(file, res) {

	  	    me.blocked = false;
	  	    me.mdlSnackbarService.showSnackbar({
		        message: res.message
		    });

		    if(res.success){
		    	var oldFile = me.myDropzone.getAcceptedFiles()[0];
		  		if(oldFile){
		  			me.myDropzone.removeFile(oldFile);
		  		}
		  		me.authenticationService.currentUser[me.imageType] = res.content[me.imageType];
		  		me.authenticationService.initUser(JSON.stringify(me.authenticationService.currentUser));
		  		
		    }else{
		    	file.status = Dropzone.QUEUED;
		    }
	  		
	  		
	  		 

	  	});

	  	me.myDropzone.on("error", function(file, res) {

	  		me.blocked = false;
	  		me.mdlSnackbarService.showSnackbar({
		        message: "Failed to upload image. "+res,
		    });

	  	});

	  	me.myDropzone.on("processing", function(file) {
	  		me.blocked = true;
	  	  me.finalURL = me.uploadURL+"?imageType="+me.imageType+"&token="+me.authenticationService.currentUser.token;
	      me.myDropzone.options.url = me.finalURL;
	    });

  }

  onSubmit(){
  	var me = this;
  	
  	me.myDropzone.processQueue();
  }
}
