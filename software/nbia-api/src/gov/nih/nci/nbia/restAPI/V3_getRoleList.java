//To test: http://localhost:8080/nbia-auth/services/v3/getRoleList?format=html
package gov.nih.nci.nbia.restAPI;

import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.dao.RoleSearchCriteria;
import gov.nih.nci.security.dao.SearchCriteria;
import gov.nih.nci.security.exceptions.CSConfigurationException;
import gov.nih.nci.security.exceptions.CSException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v3/getRoleList")
public class V3_getRoleList extends getData{
	private static final String[] columns={"label", "value"};
	public final static String TEXT_CSV = "text/csv";

	@Context private HttpServletRequest httpRequest;

	/**
	 * This method get a list of role names
	 *
	 * @return String - list of role names
	 */
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, TEXT_CSV})

	public Response  constructResponse(@QueryParam("format") String format) {
		List<Object []> roleOptions= new ArrayList<Object[]>();		

		try {
			UserProvisioningManager upm = getUpm();
			Role role = new Role();

			SearchCriteria searchCriteria = new RoleSearchCriteria(role);
			List<Role> list = upm.getObjects(searchCriteria);

			if (list != null) {
				Collections.sort(list, new Comparator<Role>() {
					public int compare(Role s1, Role s2) {
					   //ascending order
					   return s1.getName().compareTo(s2.getName());
				    }
				});
				
				for(Role aRole : list) {
					Object [] objs = {aRole.getName(), aRole.getName()};
					roleOptions.add(objs);
		        }
			}
			else {
				Object [] objs = {"Warning: No role has defined yet!", ""};
				roleOptions.add(objs);
			}
		} catch (CSConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return formatResponse(format, roleOptions, columns);
	}
}
