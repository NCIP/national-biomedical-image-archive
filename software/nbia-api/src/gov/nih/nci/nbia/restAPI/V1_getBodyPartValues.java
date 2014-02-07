//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?format=csv
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?Collection=IDRI&Modality=CT&format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?Collection=IDRI&Modality=CT&format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?Collection=IDRI&Modality=CT&format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?Collection=IDRI&Modality=CT&format=csv
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?Modality=CT&format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?Modality=CT&format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?Modality=CT&format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?Modality=CT&format=csv
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?Collection=IDRI&format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?Collection=IDRI&format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?Collection=IDRI&format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?Collection=IDRI&format=csv

package gov.nih.nci.nbia.restAPI;

import java.util.List;
import org.springframework.dao.DataAccessException;

import gov.nih.nci.nbia.util.SiteData;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.nbia.dao.GeneralSeriesDAO;
import gov.nih.nci.nbia.restUtil.FormatOutput;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/v1/getBodyPartValues")
public class V1_getBodyPartValues {
	private static final String column="BodyPartExamined";
	public final static String TEXT_CSV = "text/csv";
	public final static MediaType TEXT_CSV_TYPE = new MediaType("text", "csv");
	@Context private HttpServletRequest httpRequest;
	/**
	 * This method get a set of body part values filtered by query keys
	 *
	 * @return String - set of patient
	 */
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, TEXT_CSV})

	public Response  constructResponse(@QueryParam("Collection") String collection, @QueryParam("format") String format,
			@QueryParam("Modality") String modality) {
		String returnString = null;
		List<String> data = getDataFromDB (collection, modality);

		if ((data != null) && (data.size() > 0)) {
			if ((format == null) || (format.equalsIgnoreCase("JSON"))) {
				returnString = FormatOutput.toJSONArray(column, data).toString();
				return Response.ok(returnString).type("application/json").build();
				//returnString = "test now";
			}

			if (format.equalsIgnoreCase("HTML")) {
				returnString = FormatOutput.toHtml(column, data);
				return Response.ok(returnString).type("text/html").build();
			}

			if (format.equalsIgnoreCase("XML")) {
				returnString = FormatOutput.toXml(column, data);
				return Response.ok(returnString).type("application/xml").build();
			}
			if (format.equalsIgnoreCase("CSV")) {
				returnString = FormatOutput.toCsv(column, data);
				return Response.ok(returnString).type("text/csv").build();
			}

		}
		else {
			return Response.status(500)
					.entity("No Body Part Value found")
					.build();
		}
		return Response.status(500)
				.entity("Server was not able to process your request")
				.build();
	}


	private List<String> getDataFromDB (String collection, String modality) {
		List<String> results = null;
		List<SiteData> authorisedSites = (List)httpRequest.getAttribute("authorizedCollections");
		GeneralSeriesDAO tDao = (GeneralSeriesDAO)SpringApplicationContext.getBean("generalSeriesDAO");
		try {
			results = tDao.getBodyPartValues(collection, modality,authorisedSites);
		}
		catch (DataAccessException ex) {
			ex.printStackTrace();
		}
		return (List<String>) results;
	}
}
