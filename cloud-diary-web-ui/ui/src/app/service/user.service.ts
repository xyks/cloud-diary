import { Injectable } from '@angular/core';


import { Http, Headers, Response, RequestOptions } from '@angular/http';

import { ResponseDTO, User } from '../model/index';
import { AuthenticationService } from '../service/index';

import { AppSettings } from '../config/app-settings';

@Injectable()
export class UserService {

  private updateBasicURL = AppSettings.API_APP+'user/basic';
  private getUserURL = AppSettings.API_APP+'user';
  
  constructor(private http: Http, private authenticationService: AuthenticationService) { 

  }

  updateBasicInfo(user: User): Promise<ResponseDTO> {
    var headers = new Headers({ 'Content-Type': 'application/json'});
    headers.append('token', this.authenticationService.currentUser.token);
    let options = new RequestOptions({ headers: headers });
    return this.http.post(this.updateBasicURL, user, options)           
                .toPromise()
                .then(function(res){
                     let response = res.json() as ResponseDTO;
                     return Promise.resolve(response);
                })
                .catch(this.handleError);
    }

    get(): Promise<ResponseDTO> {
    	var me = this;
	    var headers = new Headers({ 'Content-Type': 'application/json'});
	    headers.append('token', me.authenticationService.currentUser.token);
	    let options = new RequestOptions({ headers: headers });
	    return me.http.get(me.getUserURL, options)           
	                .toPromise()
	                .then(function(res){
	                     let response = res.json() as ResponseDTO;
	                     return Promise.resolve(response);
	                })
	                .catch(me.handleError);
    }

    

    private handleError(error: any): Promise<any>{
        return Promise.reject(error.message||error);
    }

}
