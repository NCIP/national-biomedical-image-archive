import {Injectable} from 'angular2/core';
import {Http, Response} from 'angular2/http';
import {PgRole} from '../../app/pgRoles/pgRole';
import myGlobals = require('../../app/conf/globals');
import 'rxjs/add/operator/map';
import 'rxjs/Rx';
import {Headers} from "angular2/http";
import {Observable} from 'rxjs/Observable';
import {SelectItem} from 'primeng/components/api/selectitem';


@Injectable()
export class PgRoleService {

	constructor(private http: Http) {}

	getUserNames() {
		var serviceUrl = myGlobals.serviceUrl +'getUserNameList';
		var params = '?format=json';
		var headers = new Headers();
		if(myGlobals.accessToken) {
			headers.append('Authorization', 'Bearer ' + myGlobals.accessToken);      
		}
		
		return this.http.get(serviceUrl + params,{headers: headers})                    
				.toPromise()
				.then(res => <SelectItem[]> res.json())
				.then(data => { return data; })
				.catch(this.handleError); 					
    }

	private handleError (error: any) {
		// In a real world app, we might send the error to remote logging infrastructure
		let errMsg = error.message || 'Server error';
		console.error(errMsg); 
		return Promise.reject(errMsg);
	}

	getPgRolesForUser(selectedUserName: string) {
		var serviceUrl = myGlobals.serviceUrl +'getAllPGsWithRolesForUser';
		var params = '?loginName=' + selectedUserName + '&format=json';
		var headers = new Headers();
		if(myGlobals.accessToken) {
			headers.append('Authorization', 'Bearer ' + myGlobals.accessToken);      
		}
		
//        return this.http.get(serviceUrl + params,{headers: headers})
//                    .toPromise()
//                    .then(res => <PgRole[]> res.json())
//                    .then(data => { return data; }); 
//		if (selectedUserName === undefined) 
//			return [];
//		else	
			return this.http.get(serviceUrl + params,{headers: headers})
			.map((res) => <PgRole[]> res.json());
	}
	
	getAllRoles() {
		var serviceUrl = myGlobals.serviceUrl +'getRoleList';
		var params = '?format=json';
		var headers = new Headers();
		if(myGlobals.accessToken) {
			headers.append('Authorization', 'Bearer ' + myGlobals.accessToken);      
		}
		
        return this.http.get(serviceUrl + params,{headers: headers})
                    .toPromise()
                    .then(res => <SelectItem[]> res.json())
                    .then(data => { return data; }); 
	}
	
	getAvailablePGs(loginName: string){
		var serviceUrl = myGlobals.serviceUrl +'getAvailablePGsForUser';
		var params = '?loginName='+ loginName + '&format=json';
		var headers = new Headers();
		if(myGlobals.accessToken) {
			headers.append('Authorization', 'Bearer ' + myGlobals.accessToken);      
		}
		
		return this.http.get(serviceUrl + params,{headers: headers})                    
				.toPromise()
				.then(res => <SelectItem[]> res.json())
				.then(data => { return data; })
				.catch(this.handleError); 	
	}
/*
	getAvailableRoles(loginName: string, pgName:string) {
		var serviceUrl = myGlobals.serviceUrl +'getAvailablePEsForPG';
		var params = '?PGName='+ pgName + '&format=json';
		var headers = new Headers();
		if(myGlobals.accessToken) {
			headers.append('Authorization', 'Bearer ' + myGlobals.accessToken);      
		}
		
		return this.http.get(serviceUrl + params,{headers: headers})                    
				.toPromise()
				.then(res => <SelectItem[]> res.json())
				.then(data => { return data; })
				.catch(this.handleError); 	
	}
*/

	addNewPgRoleForUser(loginName: string, pgName: String, roleNames: string[])	{
		var serviceUrl = myGlobals.serviceUrl +'assignUserToPGWithRoles';
		var params = '?loginName=' + loginName + '&PGName=' + pgName+ '&roleNames='+roleNames.join(",");
		var headers = new Headers();
		headers.append('Content-Type', 'application/x-www-form-urlencoded');
		if(myGlobals.accessToken) {
			headers.append('Authorization', 'Bearer ' + myGlobals.accessToken);      
		}
		return this.http.post(serviceUrl + params,
			params, {headers: headers}).map(res => res.json());	
	}
	
	modifyRolesOfUserForPG(loginName: string, pgName: String, roleNames: string[])	{
		var serviceUrl = myGlobals.serviceUrl +'modifyRolesOfUserForPG';
		var params = '?loginName=' + loginName + '&PGName=' + pgName+ '&roleNames='+roleNames.join(",");
		var headers = new Headers();
		headers.append('Content-Type', 'application/x-www-form-urlencoded');
		if(myGlobals.accessToken) {
			headers.append('Authorization', 'Bearer ' + myGlobals.accessToken);      
		}
		
		return this.http.post(serviceUrl + params,
			params, {headers: headers}).map(res => res.json());	
	}
	
	removeUserFromPG(loginName: string, pgName: string) {
		var serviceUrl = myGlobals.serviceUrl +'removeUserFromPG';
		var params = '?loginName=' + loginName + '&PGName=' + pgName;
		var headers = new Headers();
		headers.append('Content-Type', 'application/x-www-form-urlencoded');
		if(myGlobals.accessToken) {
			headers.append('Authorization', 'Bearer ' + myGlobals.accessToken);      
		}		
		
		return this.http.post(serviceUrl + params,
			params, {headers: headers}).map(res => res.json());		
	}
}
