import { Component, OnInit } from '@angular/core';
import { Card } from '../../model/index';
@Component({
  selector: 'app-user-center',
  templateUrl: './user-center.component.html',
  styleUrls: ['./user-center.component.css']
})
export class UserCenterComponent implements OnInit {

   cards: Card[] = [ 
  	{ title: 'Basic', component: 'app-basic-info'},
  	{ title: 'Image', component: 'app-basic-image'},
  	{ title: 'Security', component: 'app-basic-info'},
  ];

   selectedCard: Card;

  constructor() { }

  ngOnInit() {
    this.selectedCard = this.cards[0];
  }

  private onSelect(card: Card): void{
  	this.selectedCard = card;
  }

}
