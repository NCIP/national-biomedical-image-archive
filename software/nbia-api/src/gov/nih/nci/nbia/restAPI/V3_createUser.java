//To Test: http://localhost:8080/nbia-auth/services/v3/createUser?loginName=authTest&email=test@mail.nih.gov&active=true

package gov.nih.nci.nbia.restAPI;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import gov.nih.nci.security.UserProvisioningManager;
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

@Path("/v3/createUser")
public class V3_createUser extends getData{
	@Context private HttpServletRequest httpRequest;

	/**
	 * This method create a new user
	 *
	 * @return String - the status of operation 
	 */
	@POST
	@Produces({MediaType.APPLICATION_JSON})

	public Response  constructResponse(@QueryParam("loginName") String loginName, @QueryParam("email") String email, @QueryParam("active") String active) {
		try {
			UserProvisioningManager upm = getUpm();
			User user = new User();

			user.setLoginName(loginName);
			if (Boolean.parseBoolean(active))
				user.setActiveFlag((byte) 1);
			else user.setActiveFlag((byte) 0);
			//Added to screen???
			user.setEmailId(email);
			user.setFirstName("csm-need-this-firstN");
			user.setLastName("csm-need-this-lastN");
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
			user.setPasswordExpiryDate(simpleDateFormat.parse(simpleDateFormat.format(Calendar.getInstance().getTime())));

			upm.createUser(user);
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
		
		return Response.ok("[\"status\", \"Submited the modification request.\"]").type("application/json").build();
	}	
}
