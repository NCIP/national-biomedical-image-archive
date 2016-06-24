//To Test:http://localhost:8080/nbia-auth/services/v3/getProtectionGrpList?format=html

package gov.nih.nci.nbia.restAPI;

import gov.nih.nci.nbia.dao.TrialDataProvenanceDAO;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.Role;
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


@Path("/v3/getProtectionGrpList")
public class V3_getProtectionGrpList extends getData{
	private static final String[] columns={"pgName", "description", "dataSetName"};
	public final static String TEXT_CSV = "text/csv";

	@Context private HttpServletRequest httpRequest;

	/**
	 * This method get a list of names of protection group
	 *
	 * @return String - list of names of protection group
	 */
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, TEXT_CSV})

	public Response  constructResponse(@QueryParam("format") String format) {
		List<Object[]> data = null;

		try {
			UserProvisioningManager upm = getUpm();
			java.util.List<ProtectionGroup> protectionGrpLst = upm.getProtectionGroups();

			if ( protectionGrpLst != null) {
				data = new ArrayList<Object []>();
				for(ProtectionGroup pg : protectionGrpLst) {
					List<ProtectionElement> pes =  new ArrayList<ProtectionElement>(upm.getProtectionElements(pg.getProtectionGroupId().toString()));					
					for (ProtectionElement pe : pes) {
						Object [] objs = {pg.getProtectionGroupName(), 
								pg.getProtectionGroupDescription(), 
								pe.getProtectionElementName()};
			            data.add(objs);
					}
		        }
			}
			else {
				Object [] objs = {"Warning: No Protection Group has defined yet!", "NA", "NA"};
				data.add(objs);
			}
		} catch (CSConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return formatResponse(format, data, columns);
	}

}
