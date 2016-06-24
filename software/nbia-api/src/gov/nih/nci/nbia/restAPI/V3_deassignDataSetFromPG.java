//To Test: http://localhost:8080/nbia-auth/services/v3/deassignDataSetFromPGG?projAndSite=TCGA//DUKE&PGName=NCIA.Test&projAndSite=IDRI//IDRI
//This API is created for a assumption that a protection element from the user's point of view has a name constructed as project name//site name. 
//a name constructed as project name//site name.  



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
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v3/deassignDataSetFromPG")
public class V3_deassignDataSetFromPG extends getData{
	@Context private HttpServletRequest httpRequest;

	/**
	 * This method deassign a combination of Project and Site to a Protection Group
	 *
	 * @return String - the status of operation 
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON})

	public Response  constructResponse(@QueryParam("projAndSite") String projAndSite, @QueryParam("PGName") String pgName) {
		String projName = null;
		String [] parsedS = projAndSite.split("//");
		if ((parsedS != null) && (parsedS.length == 2)){
			projName = parsedS[0];
		}
		else {
			return Response.ok("Please check the format of input param.  It should be <Project Name>//<Site Name>."
					+ " Please note \"//\" is needed between project name and site name").type("application/json").build();
		}
		try {
			UserProvisioningManager upm = getUpm();
			ProtectionElement protectionElm1= upm.getProtectionElement("NCIA."+projName, "NCIA.PROJECT");
			ProtectionElement protectionElm2 = upm.getProtectionElement("NCIA."+projAndSite, "NCIA.PROJECT//DP_SITE_NAME");
			ProtectionGroup pg = getPGByPGName(pgName);

			if (protectionElm1 != null) {
				Set pgs = upm.getProtectionGroups(protectionElm1.getProtectionElementId().toString());
				if (pgs.size()==1) {
					System.out.println("@@@@@@@@protection elements belong to 1 pg =" + pgs.size() );

					if (protectionElm2 != null) {
						String [] peIds = {protectionElm1.getProtectionElementId().toString(), protectionElm2.getProtectionElementId().toString()};
						upm.removeProtectionElementsFromProtectionGroup(pg.getProtectionGroupId().toString(), peIds);
						upm.removeProtectionElement(protectionElm1.getProtectionElementId().toString());
						upm.removeProtectionElement(protectionElm2.getProtectionElementId().toString());
					}

				}
				//else if protection elements belongs to multiple protection group
				else  if (pgs.size() > 1){
					System.out.println("@@@@@@@@protection elements belong to " + pgs.size() + " pg; pe1 ID = "
				+ protectionElm1.getProtectionElementId() + "; pe2 ID = " + protectionElm2.getProtectionElementId());
					String [] peIds = {protectionElm1.getProtectionElementId().toString(), protectionElm2.getProtectionElementId().toString()};
					ProtectionGroup[] pgList = (ProtectionGroup[]) pgs.toArray(new ProtectionGroup[0]);
					upm.removeProtectionElementsFromProtectionGroup(pgList[0].getProtectionGroupId().toString(), peIds);
				}
			}
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
		
		return Response.ok("Deassigned").type("application/json").build();
	}
}
