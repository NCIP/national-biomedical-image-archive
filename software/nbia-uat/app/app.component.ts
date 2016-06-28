import {Component} from 'angular2/core';
import {UserComponent} from "./user.component"
import {PgComponent} from "./pg.component"
import {PgRoleComponent} from "./pgRole.component"
import {TabView} from 'primeng/primeng';
import {TabPanel} from 'primeng/primeng';
import myGlobals = require('../app/conf/globals');

@Component({
    selector: 'my-app',
    template: `
   	<!--h1>Token: {{params | json}}</h1-->
	<table width="100%" 
       border="0" 
       cellspacing="0" 
       cellpadding="0"
       class="hdrBG" align="center" >
  <tr>
	  <td align="left">
      <a href="http://www.cancer.gov">
        <img alt="National Cancer Institute" 
             src="app/images/white-ncilogo.gif"
             border="0"/>
      </a>
    </td>
    <td align="right">
      <a href="http://www.cancer.gov">
        <img alt="U.S. National Institutes of Health"
             src="app/images/white-nihtext.gif" border="0"/>
		<img alt="www.cancer.gov"
             src="app/images/white-nciurl.gif" border="0"/>             
      </a>
    </td>
	   
	</tr>
	<tr>
	<td bgcolor="#4C5D87">
     <img src="app/images/Logo-NCIA.jpg" alt="Logo"/>
   </td>
	<td bgcolor="#4C5D87" valign="bottom" align="right">              
		<span class="fontSize24 TextShadow blue mediumFont dispBlock" >User Authorization Tool&nbsp;&nbsp;</span>
    </td> 
  </tr>
</table>

		<!--div class="ContentSideSections">
		<div class="Content100 overHidden TextShadow">
			<span class="fontSize30 TextShadow orange mediumFont marginBottom20 dispBlock">NBIA Authorization Page</span>
			<span class="defaultText dispTable">This tool grants the user's access to NBIA data collection.</span>
		</div>
	</div-->
	<p-tabView>
    <p-tabPanel header="User">
		<user></user>
	</p-tabPanel>
    <p-tabPanel header="Protection Group">
        <pg></pg>
    </p-tabPanel>
    <p-tabPanel header="User Authorization">
	   <pgRole></pgRole>
    </p-tabPanel>	
	`,
	directives: [UserComponent,PgComponent,PgRoleComponent,TabView,TabPanel]
})


export class AppComponent {	
  params;
  constructor() {
  this.params = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&')[0].split('=')[1]; 
  myGlobals.accessToken=this.params;
  //myGlobals.serviceUrl = window.location.protocol +"//"+ window.location.host+"/nbia-api/services/v3/"; 
 }

}
