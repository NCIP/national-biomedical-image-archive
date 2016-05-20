//To Test: http://localhost:8080/nbia-auth/services/v3/getUserList?format=html

package gov.nih.nci.nbia.restAPI;

import gov.nih.nci.nbia.dao.TrialDataProvenanceDAO;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.dao.RoleSearchCriteria;
import gov.nih.nci.security.dao.SearchCriteria;
import gov.nih.nci.security.dao.UserSearchCriteria;
import gov.nih.nci.security.exceptions.CSConfigurationException;
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

import org.springframework.dao.DataAccessException;


@Path("/v3/getUserNameList")
public class V3_getUserNameList extends getData{
	private static final String[] columns={"label", "value"};
	public final static String TEXT_CSV = "text/csv";

	@Context private HttpServletRequest httpRequest;

	/**
	 * This method get a list of user login names
	 *
	 * @return String - list of user login names
	 */
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, TEXT_CSV})

	public Response  constructResponse(@QueryParam("format") String format) {
		//List<String> userNames = null;
		List<Object []> userOptions= new ArrayList<Object[]>();		
		try {
			UserProvisioningManager upm = getUpm();
			User user = new User();
			SearchCriteria searchCriteria = new UserSearchCriteria(user);

			java.util.List<User> existUserLst = upm.getObjects(searchCriteria);

			if ( existUserLst != null) {
				//userNames = new ArrayList<String>();
				//first option used for placeholder
				//Object [] placeHold = {"Select a User", ""};
				//userOptions.add(placeHold);
				for(User existUser : existUserLst) {
		            //userNames.add(existUser.getLoginName());
		            Object [] objs = {existUser.getLoginName(), existUser.getLoginName()};
					userOptions.add(objs);
		        }
			}
			else {
				Object [] placeHold = {"Warning: No User Defined Yet. Creat the User First!", ""};
				userOptions.add(placeHold);
				//String status = new String("Warning: No user has defined yet!");
				//userNames.add(status);
			}
		} catch (CSConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
/*		
		List<Object []> userOptions= new ArrayList<Object[]>();
		
		for (String userName: userNames) {
			Object [] objs = {userName, userName};
			userOptions.add(objs);
		}		
*/
		return formatResponse(format, userOptions, columns);
	}
}
