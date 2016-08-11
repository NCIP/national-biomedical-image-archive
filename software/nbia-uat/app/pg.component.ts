import {Component} from 'angular2/core';
import {HTTP_PROVIDERS} from 'angular2/http';
import {InputText,MultiSelect,Messages,DataTable,Button,Dialog,Column,Header,Footer} from 'primeng/primeng';
import {Checkbox} from 'primeng/primeng';
import {SelectItem,Message} from 'primeng/primeng';
import {Pg} from './pgs/pg';
import {PgService} from './pgs/pgservice';
import {Pe} from './pes/pe';
import myGlobals = require('./conf/globals');


@Component({
	templateUrl: 'app/pg.component.html',
	selector: 'pg',
    directives: [InputText,MultiSelect,Messages,Checkbox,DataTable,Button,Dialog,Column,Header,Footer],
	providers: [HTTP_PROVIDERS,PgService]
})

export class PgComponent{
	displayDialog: boolean;
	displayAssignDialog: boolean;
	displayDeassignDialog: boolean;
    pg: Pg = new PrimePg();
    selectedPg: Pg;
    newPg: boolean;
    pgs: Pg[];
	postData: string;
	selectedPes: string[] = [];
	availablePes: SelectItem[] =[];
	includedPes: SelectItem[] = [];
	errorMessage: string;
	wikiLink: string;
	statusMessage: Message[] = [];	
	
    constructor(private pgService: PgService) {
		this.wikiLink = myGlobals.wikiContextSensitiveHelpUrl + myGlobals.managePGWiki;
	}

    ngOnInit() {
        this.pgService.getPgs().then(pgs => this.pgs = pgs,
		error =>  {this.handleError(error);this.errorMessage = <any>error});
    }

    showDialogToAdd() {
        this.newPg = true;
        this.pg = new PrimePg();
        this.displayDialog = true;
		this.displayDeassignDialog = false;
        this.displayAssignDialog = false;
    }
	
    showDialogToAssign(pg) {
        this.newPg = false;
        this.pg = this.clonePg(pg);
		this.selectedPg = pg;
		this.selectedPes = [];
		this.availablePes = [];
		this.pgService.getAvailablePes(pg.dataGroup).then(availablePes =>{
		this.availablePes = availablePes;
		this.displayDeassignDialog = false;
        this.displayAssignDialog = true;} , 
		error => {this.handleError(error);this.errorMessage = <any>error});
		
    }

	showDialogToRemove(pg){
        this.newPg = false;
        this.pg = this.clonePg(pg);
		this.selectedPg = pg;
		this.includedPes = [];
		this.selectedPes = [];
		this.pgService.getIncludedPes(pg.dataGroup).then(includedPes => this.includedPes = includedPes, 
		error =>  {this.handleError(error);this.errorMessage = <any>error});
		this.displayAssignDialog = false;
		this.displayDeassignDialog = true;	
	}		

    save() {
        if(this.newPg) {
			if (this.pgExists(this.pg.dataGroup, this.pgs)) {
				alert("The Protection Group name " + this.pg.dataGroup + " is taken.  Please try a different name.");
			}
			this.pgService.addNewPg(this.pg)
			.subscribe(
				data => this.postData = JSON.stringify(data),
				error => {this.handleError(error);this.errorMessage = <any>error},
				() => console.log("Finished")
			);
			this.pgs.push(this.pg);
		}
        else {
			this.pgService.modifyExistingPg(this.pg)
			.subscribe(
				data => this.postData = JSON.stringify(data),
				error => {this.handleError(error);this.errorMessage = <any>error},
				() => console.log("Finished")
			);
            this.pgs[this.findSelectedPgIndex()] = this.pg;
		}
        this.pg = null;
        this.displayDialog = false;
    }
	
	addPEs(){
        if (this.selectedPes.length >= 1)
		{			
			this.pgService.addPEsToExistingPg(this.pg, this.selectedPes.join(","))
			.subscribe(
				data => this.postData = JSON.stringify(data),
				error =>  {this.handleError(error);this.errorMessage = <any>error},
				() => console.log("Finished")
			);
			
			let tmpString = this.makePEsFromSelectItem(this.selectedPes);
			if (this.pgs[this.findSelectedPgIndex()].dataSets){
				//this.pgs[this.findSelectedPgIndex()].dataSets += ", NCIA."+this.selectedPes.join(", NCIA.");
				//this.pgs[this.findSelectedPgIndex()].dataSets += ", NCIA."+ makePEsFromSelectItem(this.selectedPes).join(", NCIA.");
				this.pgs[this.findSelectedPgIndex()].dataSets += ", NCIA."+ tmpString.join(", NCIA.");
			}
			else {
				//this.pgs[this.findSelectedPgIndex()].dataSets += "NCIA." + this.selectedPes.join(", NCIA.");
				//this.pgs[this.findSelectedPgIndex()].dataSets = "NCIA."+ makePEsFromSelectItem(this.selectedPes).join(", NCIA.");
				this.pgs[this.findSelectedPgIndex()].dataSets = "NCIA."+ tmpString.join(", NCIA.");				
			}
		}
        this.pg = null;
        this.displayAssignDialog = false;
    }
	
	removePEs(){
        if (this.selectedPes.length >= 1)
		{
			this.pgService.removePEsFromPg(this.pg, this.selectedPes.join(","))
			.subscribe(
				data => this.postData = JSON.stringify(data),
				error => {this.handleError(error);this.errorMessage = <any>error},
				() => console.log("Finished")
			);
			
			//diffArray(this.includedPes, this.selectedPes)
			var item = this.includedPes;
			var includedPENames = this.includedPes.map(function(item){ return item.label;});
			this.pgs[this.findSelectedPgIndex()].dataSets = this.diffArray(includedPENames, this.selectedPes).join(", ");
		}

        this.pg = null;
        this.displayDeassignDialog = false;
    }	

    delete() {
		this.pgService.deleteSelectPg(this.pg)
			.subscribe(
				data => this.postData = JSON.stringify(data),
				error => {this.handleError(error);this.errorMessage = <any>error},
				() => console.log("Finished")
			);
        this.pgs.splice(this.findSelectedPgIndex(), 1);
        this.pg = null;
        this.displayDialog = false;
    }
	
    onSelect(pg) {
        this.newPg = false;
        this.pg = this.clonePg(pg);
		this.selectedPg = pg;
        this.displayDialog = true;
    }
	
    onRowSelect(event) {
        this.newPg = false;
        this.pg = this.clonePg(event.data);
        this.displayDialog = true;
    }

    clonePg(u: Pg): Pg {
        let pg = new PrimePg();
        for(let prop in u) {
            pg[prop] = u[prop];
        }
        return pg;
    }

    findSelectedPgIndex(): number {
        return this.pgs.indexOf(this.selectedPg);
    }
	
	diffArray(a, b): any[] {
	  var seen = [], diff = [];
	  for ( var i = 0; i < b.length; i++)
		  seen[b[i]] = true;
	  for ( var i = 0; i < a.length; i++)
		  if (!seen[a[i]])
			  diff.push(a[i]);
	  return diff;
	}
	
	makePEsFromSelectItem(a: string[]): Pe[] {
	  var pes = [];
	  for ( var i = 0; i < a.length; i++) {
		  pes.push(a[i].split('//',1)[0]);
		  pes.push(a[i]);
	  }
	  return pes;
	}

	removeItem(array, item){
		for(var i in array){
			if(array[i]==item){
				array.splice(i,1);
				break;
            }
		}
    }
	
	pgExists(nameKey, myArray): boolean{
		for (var i=0; i < myArray.length; i++) {
			if (myArray[i].dataGroup.toUpperCase() == nameKey.toUpperCase()) {
				return true;
			}
		}
		return false;
	}

	private handleError (error: any) {
		this.statusMessage = [];
		
		if (error.status==500) {
			this.statusMessage.push({severity:'error', summary:'Error: ', detail:'No data found from server.'});
		}
		else if (error.status == 401) {		
			this.statusMessage.push({severity:'error', summary:'Error: ', detail:'Session expired. Please login again.'});
		}
		else if (error.status == 200) {
			this.statusMessage.push({severity:'info', summary:'Info: ', detail:'Request sent to server.'});
		}
		else if (error.status === undefined){
			this.statusMessage.push({severity:'info', summary:'Info: ', detail:'Sent.'});
		}
		else {
			this.statusMessage.push({severity:'error', summary:'Error: ', detail:'Error occured while retriving data from server. Check the server connection please. Error code: '+error.status});
		}
		this.searchInProgress = false;
	}			
}

class PrimePg implements Pg {
    constructor(public dataGroup?, public description?, public dataSets?) {}
}

