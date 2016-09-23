import {Injectable} from 'angular2/core';
import {Http, Response} from 'angular2/http';
import {Pg} from '../../app/pgs/pg';
import myGlobals = require('../../app/conf/globals');
import {Headers} from "angular2/http";
import {Observable} from 'rxjs/Observable';
import {SelectItem} from 'primeng/components/api/selectitem';

@Injectable()
export class PgService {
	
    constructor(private http: Http) {}

	getPgs() {
		var serviceUrl = myGlobals.serviceUrl +'getAllPGsWithPEs';
		var params = '?format=json';
		var headers = new Headers();
		if(myGlobals.accessToken) {
			headers.append('Authorization', 'Bearer ' + myGlobals.accessToken);      
		}

        return this.http.get(serviceUrl + params, {headers: headers})
                    .toPromise()
                    .then(res => <Pg[]> res.json())
                    .then(data => { return data; }); 
    }
	
	getAvailablePes(pgName: string) {
		var serviceUrl = myGlobals.serviceUrl +'getAvailablePEsForPG';
		var params = '?PGName='+ pgName + '&format=json';
		var headers = new Headers();
		if(myGlobals.accessToken) {
			headers.append('Authorization', 'Bearer ' + myGlobals.accessToken);      
		}		
		return this.http.get(serviceUrl + params, {headers: headers})                    
				.toPromise()
				.then(res => <SelectItem[]> res.json())
				.then(data => { return data; })
				.catch(this.handleError); 
    }
	
	getIncludedPes(pgName: string) {
		var serviceUrl = myGlobals.serviceUrl +'getIncludedPEsForPG';
		var params = '?PGName='+ pgName + '&format=json';
		var headers = new Headers();
		if(myGlobals.accessToken) {
			headers.append('Authorization', 'Bearer ' + myGlobals.accessToken);      
		}
		
		return this.http.get(serviceUrl + params, {headers: headers})                    
				.toPromise()
				.then(res => <SelectItem[]> res.json())
				.then(data => { return data; })
				.catch(this.handleError); 
    }	

	private handleError (error: any) {
		// In a real world app, we might send the error to remote logging infrastructure
		let errMsg = error.message || 'Server error';
		console.error(errMsg); // log to console instead
		return Promise.reject(errMsg);
	}
  
	addNewPg(pg: Pg) {
		var serviceUrl = myGlobals.serviceUrl +'createProtecionGroup';
		var params = '?PGName=' + pg.dataGroup + '&description='+pg.description;
		var headers = new Headers();
		headers.append('Content-Type', 'application/x-www-form-urlencoded');
		if(myGlobals.accessToken) {
			headers.append('Authorization', 'Bearer ' + myGlobals.accessToken);      
		}
		return this.http.post(serviceUrl + params,
			params, {headers: headers}).map(res => res.json());
	}
	
	modifyExistingPg(pg: Pg) {
		var serviceUrl = myGlobals.serviceUrl +'modifyProtecionGroup';
		var params = '?PGName=' + pg.dataGroup + '&description='+pg.description;
		var headers = new Headers(); 
		headers.append('Content-Type', 'application/x-www-form-urlencoded');
		if(myGlobals.accessToken) {
			headers.append('Authorization', 'Bearer ' + myGlobals.accessToken);      
		}
		
		return this.http.post(serviceUrl + params,
			params, {headers: headers}).map(res => res.json());
	}
	
	addPEsToExistingPg(pg: Pg, pes: string) {
		var serviceUrl = myGlobals.serviceUrl +'assignPEsToPG';
		var params = '?PGName=' + pg.dataGroup + '&PENames='+pes;
		var headers = new Headers(); 
		headers.append('Content-Type', 'application/x-www-form-urlencoded');
		if(myGlobals.accessToken) {
			headers.append('Authorization', 'Bearer ' + myGlobals.accessToken);      
		}
		
		return this.http.post(serviceUrl + params,
			params, {headers: headers}).map(res => res.json());	
	}
	
	removePEsFromPg(pg: Pg, pes: string) {
		var serviceUrl = myGlobals.serviceUrl +'deassignPEsFromPG';
		var params = '?PGName=' + pg.dataGroup + '&PENames='+pes;
		var headers = new Headers(); 
		headers.append('Content-Type', 'application/x-www-form-urlencoded');
		if(myGlobals.accessToken) {
			headers.append('Authorization', 'Bearer ' + myGlobals.accessToken);      
		}		
		return this.http.post(serviceUrl + params,
			params, {headers: headers}).map(res => res.json());	
	}
	
	deleteSelectPg(pg: Pg) {
	//need deassign PE from PG first???
	
		var serviceUrl = myGlobals.serviceUrl +'deleteProtecionGroup';
		var params = '?PGName=' + pg.dataGroup;
		var headers = new Headers(); 
		headers.append('Content-Type', 'application/x-www-form-urlencoded');
		if(myGlobals.accessToken) {
			headers.append('Authorization', 'Bearer ' + myGlobals.accessToken);      
		}		
		return this.http.post(serviceUrl + params,
			params, {headers: headers}).map(res => res.json());
	}		
}
