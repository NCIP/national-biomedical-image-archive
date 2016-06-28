//To Test: http://localhost:8080/nbia-auth/services/v3/getAllPGsWithPEs?format=html
//To Test: http://localhost:8080/nbia-auth/services/v3/getAllPGsWithPEs?format=json

package gov.nih.nci.nbia.restAPI;

import gov.nih.nci.nbia.util.NCIAConfig;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroupRoleContext;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.exceptions.CSException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/v3/getAllUsersWithRolesForPG")
public class V3_getAllUsersWithRolesForPG extends getData{
	private static final String[] columns={"Login Name", "Protection Group", "Role"};
	public final static String TEXT_CSV = "text/csv";

	@Context private HttpServletRequest httpRequest;
	/**
	 * This method get a list of users with associated protection group and roles
	 * 
	 * @return String - list of protection groups with associated protection elements
	 */
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, TEXT_CSV})
	public Response  constructResponse(@QueryParam("format") String format) {
		List<Object []> result = new ArrayList<Object[]>();
		try {
			UserProvisioningManager upm = getUpm();
		Date before = Calendar.getInstance().getTime();
			List<User> users = upm.getUsers();
		Date after = Calendar.getInstance().getTime();
		long diffInSeconds = (after.getTime() - before.getTime()) / 1000;
		System.out.println("&&&&&&&& it takes " + diffInSeconds +"sec to get a users list");
		
			for (User user : users) {
	//			before = Calendar.getInstance().getTime();
				List<ProtectionGroupRoleContext> pgRCs = new ArrayList<ProtectionGroupRoleContext>(upm.getProtectionGroupRoleContextForUser(user.getUserId().toString()));
			    for (ProtectionGroupRoleContext pgRC:pgRCs) {
	//		    	Date before1 = Calendar.getInstance().getTime();
			    	StringBuffer roleNames = new StringBuffer();
			    	List<Role> roles = new ArrayList<Role>(pgRC.getRoles());
			    	boolean first = true;
			    	for (Role role : roles){
			    		if (first) {
							roleNames.append(role.getName());
							first = false;
							}
							else roleNames.append(", "+role.getName());	
			    	}
	//		    	Date after1 = Calendar.getInstance().getTime();
	//		    	long diffSec = (after1.getTime() - before1.getTime()) / 1000;
	//		    	System.out.println("&&&&&&&& $$it takes " + diffSec +"sec for processing a user");
			    	
			    	Object [] objs = {user.getLoginName(), pgRC.getProtectionGroup().getProtectionGroupName(), roleNames.toString()};
			    	result.add(objs);
			    }
	//		    after = Calendar.getInstance().getTime();
	//		    diffInSeconds = (after.getTime() - before.getTime()) / 1000;
	//		    System.out.println("&&&&&&&& $$it takes " + diffInSeconds +"sec for processing a user");
			}
		Date after1 = Calendar.getInstance().getTime();
		diffInSeconds = (after1.getTime() - before.getTime()) / 1000;
	    System.out.println("&&&&&&&& $$it takes " + diffInSeconds +"sec for processing all user");		
		
		} catch (CSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return formatResponse(format, result, columns);
	}
}
