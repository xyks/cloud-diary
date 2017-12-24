import {AbstractControl} from '@angular/forms';
export class PasswordValidation {

    static MatchPassword(AC: AbstractControl) {

       let password = AC.get('newPassword').value; // to get value in input tag
       let confirmPassword = AC.get('confirmPassword').value; // to get value in input tag
        if(password != confirmPassword) {
            
            AC.get('confirmPassword').setErrors( {MatchPassword: true} );
        } else {
            AC.get('confirmPassword').setErrors(null );
        }
    }
}