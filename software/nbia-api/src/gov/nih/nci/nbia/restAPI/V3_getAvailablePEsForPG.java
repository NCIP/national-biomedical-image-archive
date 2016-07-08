//To Test: http://localhost:8080/nbia-auth/services/v3/getAvailablePEsForPG?PGName=NCIA.IDRI//IDRI&format=html

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


@Path("/v3/getAvailablePEsForPG")
public class V3_getAvailablePEsForPG extends getData{
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
		Set<String> allPENames = null;
		
		List<String> results = null;

		TrialDataProvenanceDAO tDao = (TrialDataProvenanceDAO)SpringApplicationContext.getBean("trialDataProvenanceDAO");
		try {
			results = tDao.getProjSiteValues();
			for (String projSiteName : results)
				System.out.println("Get project site combination="+ projSiteName);

			allPENames = new HashSet<String>(results);
		}
		catch (DataAccessException ex) {
			ex.printStackTrace();
		}

		try {
			UserProvisioningManager upm = getUpm();
			//ProtectionElement protectionElement = new ProtectionElement();
			//SearchCriteria searchCriteria = new ProtectionElementSearchCriteria(protectionElement);
			ProtectionGroup protectionGrp = getPGByPGName(pgName);
			Set<ProtectionElement> set = upm.getProtectionElements(protectionGrp.getProtectionGroupId().toString());
			
			if (set != null) {
				Set<String> usedPENames = new HashSet<String>();
				CharSequence cs = "//";
				
				for(ProtectionElement ptm : set) {
					String peName = ptm.getProtectionElementName();
					if (peName.contains(cs) && peName.startsWith(NCIAConfig.getProtectionElementPrefix())) 
						usedPENames.add(peName.substring(5));
		        }
				allPENames.removeAll(usedPENames);

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
		List<Object []> peOptions= new ArrayList<Object[]>();

		if (!(allPENames.isEmpty())) {
		for (String ape: allPENames) {
			Object [] objs = {ape, ape};
			peOptions.add(objs);
		}
		
		Collections.sort(peOptions, new Comparator<Object[]>() {
			public int compare(Object[] s1, Object[] s2) {
			   //ascending order
			   return s1[0].toString().compareTo(s2[0].toString());
		    }
		});
	}		
		return formatResponse(format, peOptions, columns);
	}
}
