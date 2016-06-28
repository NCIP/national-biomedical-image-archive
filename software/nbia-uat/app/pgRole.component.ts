import {Component} from 'angular2/core';
import {HTTP_PROVIDERS} from 'angular2/http';
import {Dropdown,PickList,MultiSelect,InputText,DataTable,Button,Dialog,Column,Header,Footer} from 'primeng/primeng';
import {Checkbox} from 'primeng/primeng';
import {PgRole} from './pgRoles/pgRole';
import {PgRoleService} from './pgRoles/pgRoleservice';
import {SelectItem} from 'primeng/primeng';

@Component({
	templateUrl: 'app/pgRole.component.html',
	selector: 'pgRole',
    directives: [Dropdown,PickList,MultiSelect,InputText,Checkbox,DataTable,Button,Dialog,Column,Header,Footer],
	providers: [HTTP_PROVIDERS,PgRoleService]
})

export class PgRoleComponent {
	displayDialog: boolean;
//	displayUpdateDialog: boolean;
	userNames: SelectItem[];
	errorMessage: string;
	selectedUserName: string;
	pgRoles: PgRole[];
	pgRole: PgRole;
	pgSize: number;
	tblVisisble: boolean;
	allRoles: SelectItem[] =[];
	srs: string[] = [];
	availablePGs: SelectItem[] =[];
	selectedPGs: SelectItem[] =[];
	selectedPGName: string;
	newPgRole: boolean;
	selectedPgRole: PgRole;
	postData: string;
//	choose: string;
//    user: User = new PrimeUser();
 //   selectedUser: User;
//   newUser: boolean;
 //   users: User[];
//	postData: string;
//    cities: SelectItem[];
//    selectedCity: string;


    constructor(private pgRoleService: PgRoleService) { 
//		this.userNames = [];
//		this.userNames.push({label:'Select User', value:''});	
//		this.pgRoleService.getUserNames().
//		then(userNames => this.userNames = <SelectItem[]>userNames, error =>  this.errorMessage = <any>error);
//		this.selectedUserName = null;
		
//		this.availablePGs = [];
//		this.availablePGs.push({label:'Choose', value:''});
		
//		this.allRoles = [];

	}
	
	getPgRolesForUser() {
		this.pgRoles = [];
		this.pgSize = 0;
		this.tblVisisble = false;
		this.pgRoleService.getPgRolesForUser(this.selectedUserName).
		then(pgRoles => {this.pgRoles = pgRoles; this.pgSize = this.pgRoles.length;}, error =>  this.errorMessage = <any>error);
		//alert(this.pgRoles.length);
		//this.pgSize = this.pgRoles.length;
		
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
//			PgRole pgRole = new PrimePgRole(this.selectedPGName, this.srs.join(","));
			this.pgRoles.push(new PrimePgRole(this.selectedPGName, this.srs.join(", ")));
//			this.users.push(this.user);
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
	
	
/**
    showDialogToAdd() {
        this.newUser = true;
        this.user = new PrimeUser();
        this.displayDialog = true;
    }

    save() {
        if(this.newUser) {
			this.userService.addNewUser(this.user)
			.subscribe(
				data => this.postData = JSON.stringify(data),
				error => alert(error),
				() => console.log("Finished")
			);
			this.users.push(this.user);
		}
        else {
			this.userService.modifyExistingUser(this.user)
			.subscribe(
				data => this.postData = JSON.stringify(data),
				error => alert(error),
				() => console.log("Finished")
			);
            this.users[this.findSelectedUserIndex()] = this.user;
		}
        this.user = null;
        this.displayDialog = false;
    }

    delete() {
        this.users.splice(this.findSelectedUserIndex(), 1);
        this.user = null;
        this.displayDialog = false;
    }

    onRowSelect(event) {
        this.newUser = false;
        this.user = this.cloneUser(event.data);
        this.displayDialog = true;
    }

    cloneUser(u: User): User {
        let user = new PrimeUser();
        for(let prop in u) {
            user[prop] = u[prop];
        }
        return user;
    }


*/	
}

class PrimePgRole implements PgRole {

    constructor(public pgName?, public roleNames?) {}
}

