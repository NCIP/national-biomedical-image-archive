//To Test: http://localhost:8080/nbia-auth/services/v3/getUserList?format=html

package gov.nih.nci.nbia.restAPI;

import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.nbia.csmDao.UserDao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


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
		//System.out.println("!!!!spring get class name" +SpringApplicationContext.getBean("userDao").getClass().getName());
		UserDao dao = (UserDao) SpringApplicationContext.getBean("userDao");
		List<Object []> userOptions = dao.getAllCsmUser();
		return formatResponse(format, userOptions, columns);
		
	}
	
}
