import { Injectable } from '@angular/core';

import { Http, Headers, Response, RequestOptions } from '@angular/http';

import { ResponseDTO, Diary } from '../model/index';

import { AuthenticationService } from '../service/index';

import { AppSettings } from '../config/app-settings';

@Injectable()
export class DiaryService {
	private diaryURL = AppSettings.API_APP+'diary';

    constructor(
        private http: Http,
        private authenticationService: AuthenticationService,
    ) {}
        

    save(diary: Diary): Promise<ResponseDTO> {
        var me = this;
        var headers = new Headers({ 'Content-Type': 'application/json'});
        headers.append('token', this.authenticationService.currentUser.token);
        let options = new RequestOptions({ headers: headers });
        return this.http.post(this.diaryURL, diary, options)           
                    .toPromise()
                    .then(function(res){
                         let response = res.json() as ResponseDTO;
                         return Promise.resolve(response);
                    })
                    .catch(this.handleError);
    }

    get(diary: Diary): Promise<ResponseDTO> {
        var me = this; 

        var headers = new Headers({ 'Content-Type': 'application/json'});
        headers.append('token', this.authenticationService.currentUser.token);
        let options = new RequestOptions({ headers: headers });
        let getURL = this.diaryURL+'/'+diary.year+'/'+diary.month+'/'+diary.day;
        return this.http.get(getURL, options)           
                    .toPromise()
                    .then(function(res){
                         let response = res.json() as ResponseDTO;
                         return Promise.resolve(response);
                    })
                    .catch(this.handleError);
    }

    private handleError(error: any): Promise<any>{
        return Promise.reject(error.message||error);
    }



    logout(): void {
        localStorage.removeItem('currentUser');
    }

}
