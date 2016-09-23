//To Test: http://localhost:8080/nbia-auth/services/v3/assignDataSetToPG?projAndSite=TCGA//DUKE&PGName=NCIA.Test

package gov.nih.nci.nbia.restAPI;

import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
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

@Path("/v3/deleteProtecionGroup")
public class V3_deleteProtecionGroup extends getData{
	@Context private HttpServletRequest httpRequest;

	/**
	 * This method create a new Protection Group
	 *
	 * @return String - the status of operation 
	 */
	@POST
	@Produces({MediaType.APPLICATION_JSON})

	public Response  constructResponse(@QueryParam("PGName") String pgName) {
		try {
			ProtectionGroup pg = getPGByPGName(pgName);
			UserProvisioningManager upm = getUpm();
			upm.removeProtectionGroup(pg.getProtectionGroupId().toString());
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
		
		return Response.ok("Submited the deletion request").type("application/json").build();
	}	
}
