import {Injectable} from 'angular2/core';
import {Http, Response} from 'angular2/http';
import {Pg} from '../../app/pgs/pg';
import {Pe} from '../../app/pes/pe';
import myGlobals = require('../../app/conf/globals');
import 'rxjs/add/operator/map';
import 'rxjs/Rx';
import {Observable} from 'rxjs/Observable';
import {Headers} from "angular2/http";
import {SelectItem} from 'primeng/components/api/selectitem';


@Injectable()
export class PeService {

    constructor(private http: Http) {}

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
                    .then(data => { return data; }); 
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
