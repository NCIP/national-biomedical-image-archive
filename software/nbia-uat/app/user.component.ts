import {Component,Output,EventEmitter} from 'angular2/core';
import {HTTP_PROVIDERS} from 'angular2/http';
import {InputText,DataTable,Messages,Button,Dialog,Column,Header,Footer} from 'primeng/primeng';
import {Checkbox,Message} from 'primeng/primeng';
import {User} from './users/user';
import {UserService} from './users/userservice';
import myGlobals = require('./conf/globals');

@Component({
	templateUrl: 'app/user.component.html',
	selector: 'user',
    directives: [InputText,Checkbox,DataTable,Messages,Button,Dialog,Column,Header,Footer],
	providers: [HTTP_PROVIDERS,UserService]
})

export class UserComponent {
	@Output() addUser: EventEmitter<any> = new EventEmitter();
	displayDialog: boolean;
    user: User = new PrimeUser();
    selectedUser: User;
    newUser: boolean;
    users: User[];
	postData: string;
	wikiLink: string;
	statusMessage: Message[] = [];

    constructor(private userService: UserService) { 
		this.wikiLink = myGlobals.wikiContextSensitiveHelpUrl + myGlobals.manageUserWiki;
	}

    ngOnInit() {
        this.userService.getUsers().then(users => this.users = users,
		error =>  {this.handleError(error);this.errorMessage = <any>error});
    }

    showDialogToAdd() {
        this.newUser = true;
        this.user = new PrimeUser();
        this.displayDialog = true;
    }

    save() {
        if(this.newUser) {
			if (this.userExists(this.user.loginName, this.users)) {
				alert("The login name " + this.user.loginName + " is taken.  Please try a different name.");
			}
			else {
				this.userService.addNewUser(this.user)
				.subscribe(
					data => this.postData = JSON.stringify(data),
					error => this.handleError(error),
					() => console.log("Finished")
				);
				this.users.push(this.user);
				this.addUser.next (this.user.loginName);
			}
		}
        else {
			this.userService.modifyExistingUser(this.user)
			.subscribe(
				data => this.postData = JSON.stringify(data),
				error => this.handleError(error),
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

	showDialog(u) {
        this.newUser = false;
        this.user = this.cloneUser(u);
		this.selectedUser=u;
        this.displayDialog = true;
    }

    cloneUser(u: User): User {
        let user = new PrimeUser();
        for(let prop in u) {
            user[prop] = u[prop];
        }
        return user;
    }

    findSelectedUserIndex(): number {
        return this.users.indexOf(this.selectedUser);
    }
	
	userExists(nameKey, myArray): boolean{
		for (var i=0; i < myArray.length; i++) {
			if (myArray[i].loginName.toUpperCase() == nameKey.toUpperCase()) {
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

class PrimeUser implements User {

    constructor(public loginName?, public email?, public active?) {}
}

