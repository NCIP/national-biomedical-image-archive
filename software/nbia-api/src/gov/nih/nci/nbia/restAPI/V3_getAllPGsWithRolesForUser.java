//To Test: http://localhost:8080/nbia-auth/services/v3/getAllPGsWithRolesForUser?loginName=panq&format=html
//To Test: http://localhost:8080/nbia-auth/services/v3/getAllPGsWithRolesForUser?loginName=panqformat=json

package gov.nih.nci.nbia.restAPI;

import gov.nih.nci.nbia.util.NCIAConfig;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroupRoleContext;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.exceptions.CSException;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/v3/getAllPGsWithRolesForUser")
public class V3_getAllPGsWithRolesForUser extends getData{
	private static final String[] columns={"pgName", "roleNames"};
	public final static String TEXT_CSV = "text/csv";

	@Context private HttpServletRequest httpRequest;
	/**
	 * This method get a list of users with associated protection group and roles
	 * 
	 * @return String - list of protection groups with associated protection elements
	 */
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, TEXT_CSV})
	public Response  constructResponse(@QueryParam("loginName") String loginName, @QueryParam("format") String format) {
		List<Object []> result = new ArrayList<Object[]>();
		try {
			UserProvisioningManager upm = getUpm();
			User user = getUserByLoginName(loginName);

			List<ProtectionGroupRoleContext> pgRCs = new ArrayList<ProtectionGroupRoleContext>(upm.getProtectionGroupRoleContextForUser(user.getUserId().toString()));
		    for (ProtectionGroupRoleContext pgRC:pgRCs) {
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
		    	Object [] objs = {pgRC.getProtectionGroup().getProtectionGroupName(), roleNames.toString()};
		    	result.add(objs);
		    }
		} catch (CSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	 	
		
		return formatResponse(format, result, columns);
	}
}
