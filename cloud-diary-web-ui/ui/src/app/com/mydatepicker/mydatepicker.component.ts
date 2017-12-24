import { Component, OnInit, Input,Output,EventEmitter, SimpleChanges, OnChanges }      from '@angular/core';


declare const $: any;


@Component({
  selector: 'app-mydatepicker',
  templateUrl: './mydatepicker.component.html',
  styleUrls: ['./mydatepicker.component.css']
})
export class MydatepickerComponent implements OnInit {

 @Output()  change: EventEmitter<Date> = new EventEmitter<Date>();
  constructor() {
  	
   }

  ngOnInit() {
  	var me = this;
  	$('#mydatepicker').datepicker({
  		language: 'en',
  		onSelect: function onSelect(fd, date) {
		       
		        // If date with event is selected, show it
		        if (date ) {
		            me.change.emit(date);
		        }
		        
		    }	


  		});
  }

}
