//To Test: http://localhost:8080/nbia-auth/services/v3/getProtectionEleNameList?format=html

package gov.nih.nci.nbia.restAPI;

import gov.nih.nci.nbia.dao.TrialDataProvenanceDAO;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.dao.ProtectionElementSearchCriteria;
import gov.nih.nci.security.dao.RoleSearchCriteria;
import gov.nih.nci.security.dao.SearchCriteria;
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


@Path("/v3/getProtectionEleNameList")
public class V3_getProtectionEleNameList extends getData{
	private static final String column="Protection Element";
	public final static String TEXT_CSV = "text/csv";

	@Context private HttpServletRequest httpRequest;

	/**
	 * This method get a list of protection element names
	 *
	 * @return String - list of protection element names
	 */
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, TEXT_CSV})

	public Response  constructResponse(@QueryParam("format") String format) {
		List<String> protectionElemNames = null;

		try {
			UserProvisioningManager upm = getUpm();
			ProtectionElement protectionElement = new ProtectionElement();
			SearchCriteria searchCriteria = new ProtectionElementSearchCriteria(protectionElement);
			List<ProtectionElement> list = upm.getObjects(searchCriteria);

			if (list != null) {
				protectionElemNames = new ArrayList<String>();

				for(ProtectionElement ptm : list) {
		            protectionElemNames.add(ptm.getProtectionElementName());
		        }
			}
			else {
				String status = new String("Warning: No protection element has defined yet!");
				protectionElemNames.add(status);
			}
		} catch (CSConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return formatResponse(format, protectionElemNames, column);
	}
}
