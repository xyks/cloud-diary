
import { Component, OnInit, Input,Output,EventEmitter, SimpleChanges, OnChanges }      from '@angular/core';


import { DiaryService } from '../../service/index';
import { Diary } from '../../model/index';

declare const $: any;
declare var Quill: any;

@Component({
  selector: 'app-richtext',
  templateUrl: './richtext.component.html',
  styleUrls: ['./richtext.component.css']
})
export class RichtextComponent implements OnInit {

@Input() input: string;

@Input() editMode: boolean = false;
 @Output()  change: EventEmitter<string> = new EventEmitter<string>();

  quill: any;

  constructor(
    
  ) {

  }
  
  ngOnInit(): void {
        var me = this;
        this.quill = new Quill('#editor', {
           modules: { toolbar: true },
          formats: false,
          theme: 'snow'
        });
        this.quill.disable();
        
        this.quill.on('text-change', function(delta: any, oldDelta: any, source: any) {

          if (source == 'api') {
              //console.log("An API call triggered this change.");
          } else if (source == 'user') {

              me.change.emit( me.quill.container.firstChild.innerHTML);
            
          }
        });
  }

  ngOnChanges(changes: SimpleChanges) {
        var me = this;
        if( changes['editMode'] && this.quill){
            this.quill.enable(this.editMode);

            if(!this.editMode){
                me.quill.container.firstChild.innerHTML = me.input;
               //this.quill.setText(this.input);
            }
        }

        if( changes['input'] && this.quill){
              me.quill.container.firstChild.innerHTML = me.input;
            //this.quill.setText(this.input);
        }
        
    }


 
}
