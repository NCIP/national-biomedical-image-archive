import {Component,Input,ChangeDetectionStrategy} from 'angular2/core';
import {HTTP_PROVIDERS} from 'angular2/http';
import 'rxjs/add/operator/map';
import 'rxjs/Rx';
import {Dropdown,PickList,MultiSelect,InputText,Messages,DataTable,Button,Dialog,Column,Header,Footer} from 'primeng/primeng';
import {Checkbox} from 'primeng/primeng';
import {PgRole} from './pgRoles/pgRole';
import {PgRoleService} from './pgRoles/pgRoleservice';
import {SelectItem,Message} from 'primeng/primeng';
import myGlobals = require('./conf/globals');

@Component({
	templateUrl: 'app/pgRole.component.html',
	selector: 'pgRole',
	styles: ['div {border: none;padding: 0px;margin: 0px;}'],
    directives: [Dropdown,PickList,MultiSelect,InputText,Messages,Checkbox,DataTable,Button,Dialog,Column,Header,Footer],
	providers: [HTTP_PROVIDERS,PgRoleService]
})

export class PgRoleComponent {
	@Input() addedUser: any;
	displayDialog: boolean;
	userNames: SelectItem[];
	errorMessage: string;
	statusMessage: Message[] = [];
	selectedUserName: string;
	pgRoles: PgRole[];
	pgRole: PgRole;
	pgSize: number;
	allRoles: SelectItem[] =[];
	srs: string[] = [];
	availablePGs: SelectItem[] =[];
	selectedPGs: SelectItem[] =[];
	selectedPGName: string;
	newPgRole: boolean;
	selectedPgRole: PgRole;
	postData: string;
	wikiLink: string;
	searchInProgress:boolean;

    constructor(private pgRoleService: PgRoleService) { 
		this.wikiLink = myGlobals.wikiContextSensitiveHelpUrl + myGlobals.userAuthorizationWiki;	
	}

		
	getPgRolesForUser() {
		this.searchInProgress = true;
		this.statusMessage = [];
		this.pgRoles = [];
		this.pgSize = 0;
		this.pgRoleService.getPgRolesForUser(this.selectedUserName).
		subscribe(pgRoles => {
		this.pgRoles = pgRoles; 
		this.pgSize = this.pgRoles.length; 
		this.searchInProgress=false;
		}, 
		error =>  {this.handleError(error);this.errorMessage = <any>error});
	}
	
    ngOnInit() {
		this.userNames = [];
		this.userNames.push({label:'Select User', value:''});	
		this.pgRoleService.getUserNames().
		then(userNames => this.userNames = <SelectItem[]>userNames, 
		error =>  {this.handleError(error);this.errorMessage = <any>error});
		
		this.selectedUserName = null;
		
		this.availablePGs = [];
		this.availablePGs.push({label:'Choose', value:''});
		
		this.allRoles = [];
		this.statusMessage = [];
		this.statusMessage.push({severity:'info', summary:'Info: ', detail:'Please select a user from above drop down list and click on it.'});
	}
	
	ngOnChanges(changes: any[]) {
		var newLogin = changes['addedUser'].currentValue; 
		if (newLogin) {
			this.userNames.push({label: newLogin, value: newLogin});
		}
	}
	
    showDialogToAdd() {
        this.newPgRole = true;
        this.pgRole = new PrimePgRole();
		this.selectedPGName = null;		
		this.pgRoleService.getAvailablePGs(this.selectedUserName).then(availablePGs => {
		this.availablePGs = availablePGs;
		// a workaround to show the choose as the initial tool tip as the dropdown box dose not provide it
		this.availablePGs.unshift({label:'Choose', value:''});
		}, 
		error =>  {this.handleError(error);this.errorMessage = <any>error});		
		
		this.srs = [];
		this.pgRoleService.getAllRoles().then(allRoles => {
		this.allRoles = allRoles;
		this.displayDialog = true;
		}, 
		error =>  {this.handleError(error);this.errorMessage = <any>error});
	}
	
    showDialogToUpdate(pgRole) {
        this.newPgRole = false;
		this.selectedPGName = pgRole.pgName;
        this.pgRole = this.clonePgRole(pgRole);
		this.selectedPgRole = pgRole;
		this.pgRoleService.getAllRoles().then(allRoles => {
		this.allRoles = allRoles;
		this.srs = pgRole.roleNames.split(", ");
		this.displayDialog = true;
		}, 
		error =>  {this.handleError(error);this.errorMessage = <any>error});
    }

	clonePgRole(u: PgRole): PgRole {
		let pgRole = new PrimePgRole();
		for(let prop in u) {
			pgRole[prop] = u[prop];
		}
		return pgRole;
    }
	
    findSelectedPgRoleIndex(): number {
        return this.pgRoles.indexOf(this.selectedPgRole);
    }

    save() {
		this.statusMessage = [];
        if(this.newPgRole) {
			this.pgRoleService.addNewPgRoleForUser(this.selectedUserName, this.selectedPGName, this.srs)
			.subscribe(
				data => this.postData = JSON.stringify(data),
				error =>  {this.handleError(error);this.errorMessage = <any>error},
				() => console.log("Finished")
			);
			this.pgRoles.push(new PrimePgRole(this.selectedPGName, this.srs.join(", ")));
			this.pgSize = this.pgSize +1;
		}

        this.newPgRole = null;
        this.displayDialog = false;
    }
	
	delete(){
		this.statusMessage = [];
		this.pgRoleService.removeUserFromPG(this.selectedUserName, this.selectedPGName)
		.subscribe(
			data => this.postData = JSON.stringify(data),
			error =>  {this.handleError(error);this.errorMessage = <any>error},
			() => console.log("Finished")
		);
        this.pgRoles.splice(this.findSelectedPgRoleIndex(), 1);
        this.pgRole = null;
        this.displayDialog = false;
	}
	
	update() {
		this.statusMessage = [];
		this.pgRoleService.modifyRolesOfUserForPG(this.selectedUserName, this.selectedPGName, this.srs)
		.subscribe(
		data => this.postData = JSON.stringify(data),
		error =>  {this.handleError(error);this.errorMessage = <any>error},
		() => console.log("Finished")
			);
        this.pgRole.roleNames = this.srs.join(", ");
		this.pgRoles[this.findSelectedPgRoleIndex()] = this.pgRole;	
        this.displayDialog = false;	
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

class PrimePgRole implements PgRole {

    constructor(public pgName?, public roleNames?) {}
}

