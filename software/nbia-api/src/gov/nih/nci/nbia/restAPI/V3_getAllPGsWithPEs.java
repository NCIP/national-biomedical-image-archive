//To Test: http://localhost:8080/nbia-auth/services/v3/getAllPGsWithPEs?format=html
//To Test: http://localhost:8080/nbia-auth/services/v3/getAllPGsWithPEs?format=json

package gov.nih.nci.nbia.restAPI;

import gov.nih.nci.nbia.util.NCIAConfig;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
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


@Path("/v3/getAllPGsWithPEs")
public class V3_getAllPGsWithPEs extends getData{
	private static final String[] columns={"dataGroup", "description", "dataSets"};
	public final static String TEXT_CSV = "text/csv";

	@Context private HttpServletRequest httpRequest;
	/**
	 * This method get a list of protection groups with associated protection elements
	 * 
	 * @return String - list of protection groups with associated protection elements
	 */
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, TEXT_CSV})
	public Response  constructResponse(@QueryParam("format") String format) {
		List<Object []> result = new ArrayList<Object[]>();
		try {
			UserProvisioningManager upm = getUpm();
			
			List<ProtectionGroup> pgs = upm.getProtectionGroups();
			for (ProtectionGroup pg : pgs) {
				List<ProtectionElement> pes =  new ArrayList<ProtectionElement>(upm.getProtectionElements(pg.getProtectionGroupId().toString()));
				StringBuffer allPeNames = new StringBuffer();
				boolean first = true;
				for (ProtectionElement pe : pes) {
					if (first) {
					allPeNames.append(pe.getProtectionElementName());
					first = false;
					}
					else allPeNames.append(", "+pe.getProtectionElementName());
				}
				Object [] objs = {pg.getProtectionGroupName(), pg.getProtectionGroupDescription(), allPeNames.toString()};
				result.add(objs);
			}
			} catch (CSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		
		return formatResponse(format, result, columns);
	}
}
