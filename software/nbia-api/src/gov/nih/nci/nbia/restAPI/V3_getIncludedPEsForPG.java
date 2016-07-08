//To Test: http://localhost:8080/nbia-auth/services/v3/getIncludedPEsForPG?PGName=NCIA.IDRI//IDRI&format=html

package gov.nih.nci.nbia.restAPI;

import gov.nih.nci.nbia.dao.TrialDataProvenanceDAO;
import gov.nih.nci.nbia.util.NCIAConfig;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
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


@Path("/v3/getIncludedPEsForPG")
public class V3_getIncludedPEsForPG extends getData{
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

	public Response  constructResponse(@QueryParam("PGName") String pgName, @QueryParam("format") String format) {
		List<Object []> peOptions= new ArrayList<Object[]>();

		try {
			UserProvisioningManager upm = getUpm();
			ProtectionGroup protectionGrp = getPGByPGName(pgName);
			Set<ProtectionElement> set = upm.getProtectionElements(protectionGrp.getProtectionGroupId().toString());
			
			if ((set != null) && (!(set.isEmpty()))){
				for(ProtectionElement ptm : set) {
					Object [] objs = {ptm.getProtectionElementName(), ptm.getProtectionElementName()};
					peOptions.add(objs);					
		        }
				
				Collections.sort(peOptions, new Comparator<Object[]>() {
					public int compare(Object[] s1, Object[] s2) {
					   //ascending order
					   return s1[0].toString().compareTo(s2[0].toString());
				    }
				});					
			}
			else {
					Object [] objs = {"Warning: No Data Sets Assigned Yet!", "Warning: No Data Sets Assigned Yet!"};
					peOptions.add(objs);
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
		
		return formatResponse(format, peOptions, columns);
	}
}
