//To Test: http://localhost:8080/nbia-api/api/v1/getPatient?Collection=IDRI
//To Test: http://localhost:8080/nbia-api/api/v1/getPatient?format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getPatient?format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getPatient?format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getPatient?format=csv
//To Test: http://localhost:8080/nbia-api/api/v1/getPatient?Collection=IDRI&format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getPatient?Collection=IDRI&format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getPatient?Collection=IDRI&format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getPatient?Collection=IDRI&format=csv

package gov.nih.nci.nbia.restAPI;

import java.util.List;
import org.springframework.dao.DataAccessException;

import gov.nih.nci.nbia.util.SiteData;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.nbia.dao.PatientDAO;
import gov.nih.nci.nbia.restUtil.FormatOutput;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/v1/getPatient")
public class V1_getPatient extends getData{
	private static final String[] columns={"PatientId", "PatientName", "PatientBirthDate", "PatientSex", "EthnicGroup", "Collection"};
	public final static String TEXT_CSV = "text/csv";

	@Context private HttpServletRequest httpRequest;
	/**
	 * This method get a set of patient objects filtered by collection
	 * 
	 * @return String - set of patient
	 */
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, TEXT_CSV})

	public Response  constructResponse(@QueryParam("Collection") String collection, @QueryParam("format") String format) {
		List<String> authorizedCollections = null;
		
		try {
			authorizedCollections = getPublicCollections();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Object[]> data = getPatientByCollection(collection, authorizedCollections);
		return formatResponse(format, data, columns);
	}
}
