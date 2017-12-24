import { Injectable } from '@angular/core';
import { Http, Headers, Response, RequestOptions } from '@angular/http';

import { ResponseDTO} from '../model/index';

import { AuthenticationService } from '../service/index';

import { AppSettings } from '../config/app-settings';

declare const CryptoJS: any;


@Injectable()
export class KeycodeService {

  private keyCodeURL = AppSettings.API_APP+'keycode';
   private SaveKeyCodeURL = AppSettings.API_APP+'savekeycode';

  private keyCode: string;	

  constructor(
      private http: Http,
      private authenticationService: AuthenticationService
  ) { }


  public getKeyCode(): string{
  	return this.keyCode;
  }

  setKeyCode(code: string): void{
  	//do something
  	this.keyCode = code;
  }

  public hasKeyCode(): boolean{
  	if(this.getKeyCode()){
  		return true;
  	}else{
  		return false;
  	}
  }

  public encrypt(input: string): string{
      return CryptoJS.AES.encrypt(input, this.keyCode).toString();
  }

  public decrypt(input: string): string{
    var bytes  = CryptoJS.AES.decrypt(input, this.keyCode);
    return bytes.toString(CryptoJS.enc.Utf8);
  }

  private convertCode(code: string): string{

      return CryptoJS.SHA256(code).toString();
  }

  sendKeyCode(code: string): Promise<ResponseDTO> {
        var me = this;

        var headers = new Headers({ 'Content-Type': 'application/json'});
        headers.append('token', this.authenticationService.currentUser.token);
        let options = new RequestOptions({ headers: headers });
        return this.http.post(this.keyCodeURL, me.convertCode(code), options)           
                    .toPromise()
                    .then(function(res){
                         let response = res.json() as ResponseDTO;
                         if(response.success && response.code == 0){
                            me.keyCode = code;
                         } 

                         return Promise.resolve(response);
                    })
                    .catch(this.handleError);
  }

    saveKeyCode(code: string): Promise<ResponseDTO> {
        var me = this;

        var headers = new Headers({ 'Content-Type': 'application/json'});
        headers.append('token', this.authenticationService.currentUser.token);
        let options = new RequestOptions({ headers: headers });
        return this.http.post(this.SaveKeyCodeURL, me.convertCode(code), options)           
                    .toPromise()
                    .then(function(res){
                         let response = res.json() as ResponseDTO;
                         if(response.success){
                            me.keyCode = code;
                         } 

                         return Promise.resolve(response);
                    })
                    .catch(this.handleError);
  }


  private handleError(error: any): Promise<any>{
        return Promise.reject(error.message||error);
    }


}
