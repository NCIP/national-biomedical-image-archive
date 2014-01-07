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
public class V1_getPatient {
	private static final String[] columns={"PatientId", "PatientName", "PatientBirthDate", "PatientSex", "EthnicGroup", "Collection"};
	public final static String TEXT_CSV = "text/csv";
	public final static MediaType TEXT_CSV_TYPE = new MediaType("text", "csv");
	@Context private HttpServletRequest httpRequest;
	/**
	 * This method get a set of patient objects filtered by collection
	 * 
	 * @return String - set of patient
	 */
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, TEXT_CSV})

	public Response  constructResponse(@QueryParam("Collection") String collection, @QueryParam("format") String format) {
		String returnString = null;
		List<Object[]> data = getDataFromDB (collection);
		
		if ((data != null) && (data.size() > 0)) {
			if ((format == null) || (format.equalsIgnoreCase("JSON"))) {
				returnString = FormatOutput.toJSONArray(columns, data).toString();
				return Response.ok(returnString).type("application/json").build();
			}
			
			if (format.equalsIgnoreCase("HTML")) {
				returnString = FormatOutput.toHtml(columns, data);
				return Response.ok(returnString).type("text/html").build();
			}
			
			if (format.equalsIgnoreCase("XML")) {
				returnString = FormatOutput.toXml(columns, data);
				return Response.ok(returnString).type("application/xml").build();
			}
			if (format.equalsIgnoreCase("CSV")) {
				returnString = FormatOutput.toCsv(columns, data);
				return Response.ok(returnString).type("text/csv").build();
			}
			
		}
		else {
			return Response.status(500)
					.entity("No patient found for collection "+ collection)
					.build();
		}
		return Response.status(500)
				.entity("Server was not able to process your request")
				.build();
	}

	
	private List<Object[]> getDataFromDB (String collection) {
		List<Object []> results = null;
		List<SiteData> authorisedSites = (List)httpRequest.getAttribute("authorizedCollections");
		PatientDAO tDao = (PatientDAO)SpringApplicationContext.getBean("patientDAO");
		try {
			results = tDao.getPatientByCollection(collection,authorisedSites);
		}
		catch (DataAccessException ex) {
			ex.printStackTrace();
		}
		return (List<Object[]>) results;
	}
}
