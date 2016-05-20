//To Test: http://localhost:8080/nbia-auth/services/v3/assignUserToPGWithRoles?loginName=authTest&PGName=NCIA.Test&roleName=NCIA.READ

package gov.nih.nci.nbia.restAPI;

import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.exceptions.CSConfigurationException;
import gov.nih.nci.security.exceptions.CSException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v3/assignUserToPGWithRoles")
public class V3_assignUserToPGWithRoles extends getData{
	@Context private HttpServletRequest httpRequest;

	/**
	 * This method assign an user to a protection group with a role
	 *
	 * @return String - the status of operation 
	 */
	@POST
	@Produces({MediaType.APPLICATION_JSON})

	public Response  constructResponse(@QueryParam("loginName") String loginName, @QueryParam("PGName") String pgName, @QueryParam("roleNames") String roleNames) {
		try {
			UserProvisioningManager upm = getUpm();
			//getProtection using protection group name
			ProtectionGroup pg = getPGByPGName(pgName);
			String [] roleNameList = roleNames.split(",");
			User user = getUserByLoginName(loginName);
			String [] roleIds = new String[roleNameList.length];
			for (int i=0; i < roleNameList.length; ++i) {
				Role role = getRoleByRoleName(roleNameList[i]);
				roleIds[i] = role.getId().toString();
			}
			upm.addUserRoleToProtectionGroup(user.getUserId().toString(), roleIds, pg.getProtectionGroupId().toString());

		} catch (CSConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Response.ok("Submited the assign request.").type("application/json").build();
	}	
}
