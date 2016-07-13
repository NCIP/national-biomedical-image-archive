import {Component, Input, ChangeDetectionStrategy} from 'angular2/core';
import {HTTP_PROVIDERS} from 'angular2/http';
import {Dropdown,PickList,MultiSelect,InputText,DataTable,Button,Dialog,Column,Header,Footer} from 'primeng/primeng';
import {Checkbox} from 'primeng/primeng';
import {PgRole} from './pgRoles/pgRole';
import {PgRoleService} from './pgRoles/pgRoleservice';
import {SelectItem} from 'primeng/primeng';
import myGlobals = require('./conf/globals');

@Component({
	templateUrl: 'app/pgRole.component.html',
	selector: 'pgRole',
	changeDetection: ChangeDetectionStrategy.OnPush,
    directives: [Dropdown,PickList,MultiSelect,InputText,Checkbox,DataTable,Button,Dialog,Column,Header,Footer],
	providers: [HTTP_PROVIDERS,PgRoleService]
})

export class PgRoleComponent {
	@Input() addedUser: any;
	displayDialog: boolean;
	userNames: SelectItem[];
	errorMessage: string;
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

    constructor(private pgRoleService: PgRoleService) { 
		this.wikiLink = myGlobals.wikiContextSensitiveHelpUrl + myGlobals.userAuthorizationWiki;
	}
	
	getPgRolesForUser() {
		this.pgRoles = [];
		this.pgSize = 0;
		this.pgRoleService.getPgRolesForUser(this.selectedUserName).
		then(pgRoles => {this.pgRoles = pgRoles; this.pgSize = this.pgRoles.length;}, error =>  this.errorMessage = <any>error);	
	}
	
    ngOnInit() {
			this.userNames = [];
		this.userNames.push({label:'Select User', value:''});	
		this.pgRoleService.getUserNames().
		then(userNames => this.userNames = <SelectItem[]>userNames, error =>  this.errorMessage = <any>error);
		this.selectedUserName = null;

		
		this.availablePGs = [];
		this.availablePGs.push({label:'Choose', value:''});
		

		this.allRoles = [];
    }
	
	ngOnChanges(changes: any[]) {
		var newLogin = changes['addedUser'].currentValue; 
		if (newLogin) {
			//alert("ngOnChanges called value = "+newLogin);
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
		}, error =>  this.errorMessage = <any>error);		
		
		this.srs = [];
		this.pgRoleService.getAllRoles().then(allRoles => {
		this.allRoles = allRoles;
		this.displayDialog = true;
		}, error =>  this.errorMessage = <any>error);

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
		}, error =>  this.errorMessage = <any>error);
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
        if(this.newPgRole) {
			this.pgRoleService.addNewPgRoleForUser(this.selectedUserName, this.selectedPGName, this.srs)
			.subscribe(
				data => this.postData = JSON.stringify(data),
//				error => alert(error),
				error =>  this.errorMessage = <any>error,
				() => console.log("Finished")
			);
			this.pgRoles.push(new PrimePgRole(this.selectedPGName, this.srs.join(", ")));
			this.pgSize = this.pgSize +1;
		}

        this.newPgRole = null;
        this.displayDialog = false;
    }
	
	delete(){
		this.pgRoleService.removeUserFromPG(this.selectedUserName, this.selectedPGName)
		.subscribe(
			data => this.postData = JSON.stringify(data),
//				error => alert(error),
			error =>  this.errorMessage = <any>error,
			() => console.log("Finished")
		);
        this.pgRoles.splice(this.findSelectedPgRoleIndex(), 1);
        this.pgRole = null;
        this.displayDialog = false;	
	}
	
	update() {	
	this.pgRoleService.modifyRolesOfUserForPG(this.selectedUserName, this.selectedPGName, this.srs)
		.subscribe(
		data => this.postData = JSON.stringify(data),
//				error => alert(error),
		error =>  this.errorMessage = <any>error,
		() => console.log("Finished")
			);
//        this.pgRoles.splice(this.findSelectedPgRoleIndex(), 1);
        this.pgRole.roleNames = this.srs.join(", ");
		this.pgRoles[this.findSelectedPgRoleIndex()] = this.pgRole;	
        this.displayDialog = false;		
	}	
}

class PrimePgRole implements PgRole {

    constructor(public pgName?, public roleNames?) {}
}

