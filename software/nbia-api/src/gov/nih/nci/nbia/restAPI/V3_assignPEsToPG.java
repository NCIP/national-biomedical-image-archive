//To Test: http://localhost:8080/nbia-auth/services/v3/assignPEsToPG?projAndSite=TCGA//DUKE&PGName=NCIA.Test

package gov.nih.nci.nbia.restAPI;

import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
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

@Path("/v3/assignPEsToPG")
public class V3_assignPEsToPG extends getData{
	@Context private HttpServletRequest httpRequest;

	/**
	 * This method assign a combination of Project and Site to a Protection Group
	 *
	 * @return String - the status of operation 
	 */
	@POST
	@Produces({MediaType.APPLICATION_JSON})

	public Response  constructResponse(@QueryParam("PENames") String peNames, @QueryParam("PGName") String pgName) {
		String [] projSites = peNames.split(",");
		StringBuffer status = new StringBuffer();
		for (int i = 0; i < projSites.length; ++i) {
			status.append(assignADataSetToPG(projSites[i], pgName));
			status.append("\n");
		}
		
		return Response.ok(status.toString()).type("application/json").build();
	}
	
	public String assignADataSetToPG(String projAndSite, String pgName){
		String projName = null;
		String [] parsedS = projAndSite.split("//");
		if ((parsedS != null) && (parsedS.length == 2)){
			projName = parsedS[0];
		}
		else {
			return new String("Please check the format of input param for "+projAndSite+ ". It should be <Project Name>//<Site Name>."
					+ " Please note \"//\" is needed between project name and site name");
		}
		try {
			createNewProtectionElems(projAndSite);
			UserProvisioningManager upm = getUpm();
			upm = SecurityServiceProvider.getUserProvisioningManager(applicationName);
			upm.assignProtectionElement(pgName, "NCIA."+projName, "NCIA.PROJECT");
			upm.assignProtectionElement(pgName, "NCIA."+projAndSite, "NCIA.PROJECT//DP_SITE_NAME");
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
		return new String("Assigned "+ projAndSite +" to Protection Group:"+pgName);
	}
	
	
	// For UPT replacement GUI
	public void createNewProtectionElems(String projAndSite) throws Exception{
		UserProvisioningManager upm = getUpm();
		System.out.println("#######3createNewProtectionElems="+projAndSite);

		String projName = null;
		String [] parsedS = projAndSite.split("//");
		if ((parsedS != null) && (parsedS.length == 2)){
			projName = parsedS[0];
			}
		else {
			throw new Exception("Please check the format of input param.  It should be <Project Name>//<Site Name>."
					+ " Please note \"//\" is needed between project name and site name");
		}
		try {
			upm = SecurityServiceProvider.getUserProvisioningManager(applicationName);
			ProtectionElement protectionElm1= upm.getProtectionElement("NCIA."+projName, "NCIA.PROJECT");
			if (protectionElm1 == null) {
				protectionElm1 = new ProtectionElement();
				protectionElm1.setProtectionElementName("NCIA."+projName);
				protectionElm1.setProtectionElementDescription("NCIA.TRIAL_DATA_PROVENANCE");
				protectionElm1.setObjectId("NCIA."+projName);
				protectionElm1.setAttribute("NCIA.PROJECT");
				upm.createProtectionElement(protectionElm1);
			}

			ProtectionElement protectionElm2= upm.getProtectionElement("NCIA."+projAndSite, "NCIA.PROJECT//DP_SITE_NAME");
			if (protectionElm2 == null ) {
				protectionElm2 = new ProtectionElement();
				protectionElm2.setProtectionElementName("NCIA."+projAndSite);
				protectionElm2.setProtectionElementDescription("NCIA.TRIAL_DATA_PROVENANCE");
				protectionElm2.setObjectId("NCIA."+projAndSite);
				protectionElm2.setAttribute("NCIA.PROJECT//DP_SITE_NAME");
				upm.createProtectionElement(protectionElm2);
			}
		} catch (CSConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}		
}
