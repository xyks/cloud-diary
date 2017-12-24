import { Injectable } from '@angular/core';
import { Http, Headers, Response, RequestOptions } from '@angular/http';
import { ResponseDTO, User } from '../model/index';
import 'rxjs/add/operator/toPromise';

import { AppSettings } from '../config/app-settings';


declare const CryptoJS: any;
@Injectable()
export class AuthenticationService {

    private loginURL = AppSettings.API_SSO+'login';
    private passwordURL = AppSettings.API_SSO+'password';

    private imageURL = AppSettings.API_APP+'user/image/';

    private registerURL = AppSettings.API_SSO+'register';

    private forgetPasswordURL = AppSettings.API_SSO+'forgetpassword';



    public currentUser: User = new User();
    constructor(private http: Http) {
        this.initUser(localStorage.getItem('currentUser'));
    }

    login(email: string, password: string): Promise<ResponseDTO> {
        var me = this; 
        password = CryptoJS.SHA256(password).toString();
        let headers = new Headers({ 'Content-Type': 'application/json' });
        let options = new RequestOptions({ headers: headers });
        return me.http.post(this.loginURL, { email: email, password: password }, options)           
                    .toPromise()
                    .then(res=>{
                         let response = res.json() as ResponseDTO;  
                        
                         if(response.success){
                            me.updateBySSO(response.content);
                         }else{
                            localStorage.removeItem('currentUser');
                         }                      
                         return Promise.resolve(response);
                         
                    })
                    .catch(me.handleError);
    }


    register(email: string, password: string): Promise<ResponseDTO> {
        var me = this; 
         password = CryptoJS.SHA256(password).toString();
        let headers = new Headers({ 'Content-Type': 'application/json' });
        let options = new RequestOptions({ headers: headers });
        return me.http.post(this.registerURL, { email: email, password: password} , options)           
                    .toPromise()
                    .then(res=>{
                         let response = res.json() as ResponseDTO;                       
                         return Promise.resolve(response);
                         
                    })
                    .catch(me.handleError);
    }

    forgetPassword(email: string): Promise<ResponseDTO> {
        var me = this; 

        let headers = new Headers({ 'Content-Type': 'application/json' });
        let options = new RequestOptions({ headers: headers });
        return me.http.post(this.forgetPasswordURL, { email: email} , options)           
                    .toPromise()
                    .then(res=>{
                         let response = res.json() as ResponseDTO;                       
                         return Promise.resolve(response);
                         
                    })
                    .catch(me.handleError);
    }


    changePassword(password: string, newPassword: string): Promise<ResponseDTO> {
        var me = this; 
        password = CryptoJS.SHA256(password).toString();
        newPassword = CryptoJS.SHA256(newPassword).toString();
        let headers = new Headers({ 'Content-Type': 'application/json' });
        headers.append('token', me.currentUser.token);
        let options = new RequestOptions({ headers: headers });
        return me.http.post(this.passwordURL, { password: password, newPassword: newPassword }, options)           
                    .toPromise()
                    .then(res=>{
                         let response = res.json() as ResponseDTO;                       
                         return Promise.resolve(response);
                    })
                    .catch(me.handleError);
    }


    private handleError(error: any): Promise<any>{
        return Promise.reject(error.message||error);
    }

    public initUser(value: any): void{

        if(value){
            var jsonData = JSON.parse(value);
        }     
        var me = this;
        if(jsonData){           
            me.currentUser.email = jsonData.email;
            me.currentUser.nickName = jsonData.nickName;
            me.currentUser.signature = jsonData.signature;
            me.currentUser.gender = jsonData.gender;
            me.currentUser.cover = jsonData.cover;
            me.currentUser.headPortrait = jsonData.headPortrait;
            if(jsonData.token){
               me.updateBySSO(jsonData);
            }
            localStorage.setItem('currentUser', JSON.stringify( me.currentUser));
            me.updateImageURL();   
        }

       
    }


    updateImageURL(): void{
        var me = this;
        me.currentUser.headPortraitURL = me.currentUser.headPortrait ?me.imageURL+me.currentUser.headPortrait+'?token='+me.currentUser.token : '';
        me.currentUser.coverURL = me.currentUser.cover ? me.imageURL+me.currentUser.cover+'?token='+me.currentUser.token: '';
    }    

    updateBySSO(sso: any): void{
         var me = this; 
         me.currentUser.token = sso.token;
         me.currentUser.ip = sso.ip;
         me.currentUser.lastLoginIp = sso.lastLoginIp;
         me.currentUser.lastLoginDate = sso.lastLoginDate;

    }

    logout(): void {
        localStorage.removeItem('currentUser');
    }

}
