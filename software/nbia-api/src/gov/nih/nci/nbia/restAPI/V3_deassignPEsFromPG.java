//To Test: http://localhost:8080/nbia-auth/services/v3/deassignPEsFromPG?projAndSite=TCGA//DUKE&PGName=NCIA.Test&projAndSite=IDRI//IDRI

package gov.nih.nci.nbia.restAPI;

import java.util.Set;

import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
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
import javax.ws.rs.core.Response.Status;

@Path("/v3/deassignPEsFromPG")
public class V3_deassignPEsFromPG extends getData{
	@Context private HttpServletRequest httpRequest;

	/**
	 * This method deassign a combination of Project and Site to a Protection Group
	 *
	 * @return String - the status of operation 
	 */
	@POST
	@Produces({MediaType.APPLICATION_JSON})

	public Response constructResponse(@QueryParam("PENames") String peNames, @QueryParam("PGName") String pgName) {
		// String projName = null;
		if ((pgName == null) || pgName.isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity("A value for PGName is needed.").build();
		}

		if (peNames != null) {
			String[] pes = peNames.split(",");
			try {
				ProtectionGroup pg = getPGByPGName(pgName);
				UserProvisioningManager upm = getUpm();
				String[] peIds = new String[pes.length];
				for (int i = 0; i < pes.length; ++i) {
					ProtectionElement protectionElm = upm.getProtectionElement(pes[i]);
					peIds[i] = protectionElm.getProtectionElementId().toString();

				}
				upm.removeProtectionElementsFromProtectionGroup(pg.getProtectionGroupId().toString(), peIds);

			} catch (CSConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return Response.status(Status.SERVICE_UNAVAILABLE)
						.entity("Server failed to perform requested action")
						.build();
			} catch (CSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return Response.status(Status.SERVICE_UNAVAILABLE)
						.entity("Server failed to perform requested action")
						.build();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return Response.status(Status.SERVICE_UNAVAILABLE)
						.entity("Server failed to perform requested action")
						.build();
			}
		} 
		else {
			return Response.status(Status.BAD_REQUEST)
					.entity("A value for PENames is needed. The values should have a format of comma seperated NCIA.<Project Name>//<Site Name>.")
					.build();
		}

		return Response.ok("Deassigned").type("application/json").build();
	}
}
