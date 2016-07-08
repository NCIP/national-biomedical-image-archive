//To Test: http://localhost:8080/nbia-auth/services/v3/getAvailablePGsForUser?loginName=panq

package gov.nih.nci.nbia.restAPI;

import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroupRoleContext;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.exceptions.CSConfigurationException;
import gov.nih.nci.security.exceptions.CSException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.dao.DataAccessException;


@Path("/v3/getAvailablePGsForUser")
public class V3_getAvailablePGsForUser extends getData{
	private static final String[] columns={"label", "value"};
	public final static String TEXT_CSV = "text/csv";

	@Context private HttpServletRequest httpRequest;

	/**
	 * This method get a list of protection element names
	 *
	 * @return String - list of protection element names
	 */
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, TEXT_CSV})

	public Response  constructResponse(@QueryParam("loginName") String loginName, @QueryParam("format") String format) {
		Set<String> allPg = null;

		try {
			UserProvisioningManager upm = getUpm();
			List<ProtectionGroup> protectionGrps = upm.getProtectionGroups();

			if ((protectionGrps != null) && (!protectionGrps.isEmpty())) {
				allPg = new HashSet<String>();
				for (ProtectionGroup pg: protectionGrps) {
					allPg.add(pg.getProtectionGroupName());
				}
				
				Set<String> usedPg = new HashSet<String>();
				User user = getUserByLoginName(loginName);
				List<ProtectionGroupRoleContext> pgRCs = new ArrayList<ProtectionGroupRoleContext>(upm.getProtectionGroupRoleContextForUser(user.getUserId().toString()));
			    for (ProtectionGroupRoleContext pgRC:pgRCs) {
			    	usedPg.add(pgRC.getProtectionGroup().getProtectionGroupName());
			    }
				
			    allPg.removeAll(usedPg);
			}
			else allPg.add("Warning: No Data Group found. Please create one first.");
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

		List<Object []> pgOptions= new ArrayList<Object[]>();
		if (!(allPg.isEmpty())) {
			for (String apg: allPg) {
				Object [] objs = {apg, apg};
				pgOptions.add(objs);
			}
			
			Collections.sort(pgOptions, new Comparator<Object[]>() {
				public int compare(Object[] s1, Object[] s2) {
				   //ascending order
				   return s1[0].toString().compareTo(s2[0].toString());
			    }
			});
		}
		return formatResponse(format, pgOptions, columns);
	}
}
