import {Injectable} from 'angular2/core';
import {Http, Response} from 'angular2/http';
import {User} from '../../app/users/user';
import {Observable} from 'rxjs/Observable';
import myGlobals = require('../../app/conf/globals');
import 'rxjs/add/operator/map';
import {Headers} from "angular2/http";

@Injectable()
export class UserService {

	constructor(private http: Http) {}

	getUsers() {
		//alert(myGlobals.accessToken);
		var serviceUrl = myGlobals.serviceUrl +'getUserList?format=json';	
		var headers = new Headers();
		if(myGlobals.accessToken) {
			headers.append('Authorization', 'Bearer ' + myGlobals.accessToken);      
		}

        return this.http.get(serviceUrl,{headers: headers})
                    .toPromise()
                    .then(res => <User[]> res.json())
                    .then(data => { return data; }); 
    }

	addNewUser(user: User) {
		var serviceUrl = myGlobals.serviceUrl +'createUser';
		var params = '?loginName=' + user.loginName + '&email=' + user.email + '&active=' + user.active;
		var headers = new Headers();
		headers.append('Content-Type', 'application/x-www-form-urlencoded');
		if(myGlobals.accessToken) {
			headers.append('Authorization', 'Bearer ' + myGlobals.accessToken);      
		}
		
		return this.http.post(serviceUrl + params,
			params, {headers: headers}).map(res => res.json());
	}
	
	modifyExistingUser(user: User) {
		var serviceUrl = myGlobals.serviceUrl +'modifyUser';
		var params = '?loginName=' + user.loginName + '&email=' + user.email + '&active=' + user.active;
		var headers = new Headers(); 
		headers.append('Content-Type', 'application/x-www-form-urlencoded');
		if(myGlobals.accessToken) {
			headers.append('Authorization', 'Bearer ' + myGlobals.accessToken);      
		}			

		return this.http.post(serviceUrl + params,
			params, {headers:headers}).map(res => res.json());
	}
}
