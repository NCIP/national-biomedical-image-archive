//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?format=csv
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?Collection=IDRI&Modality=CT&BodyPartExamined=ABDOMEN&format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?Collection=IDRI&Modality=CT&BodyPartExamined=ABDOMEN&format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?Collection=IDRI&Modality=CT&BodyPartExamined=ABDOMEN&format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?Collection=IDRI&Modality=CT&BodyPartExamined=ABDOMEN&format=csv
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?Collection=IDRI&Modality=CT&format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?Collection=IDRI&Modality=CT&format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?Collection=IDRI&Modality=CT&format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?Collection=IDRI&Modality=CT&format=csv
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?Modality=CT&BodyPartExamined=ABDOMEN&format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?Modality=CT&BodyPartExamined=ABDOMEN&format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?Modality=CT&BodyPartExamined=ABDOMEN&format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?Modality=CT&BodyPartExamined=ABDOMEN&format=csv
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?Collection=IDRI&BodyPartExamined=ABDOMEN&format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?Collection=IDRI&BodyPartExamined=ABDOMEN&format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?Collection=IDRI&BodyPartExamined=ABDOMEN&format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?Collection=IDRI&BodyPartExamined=ABDOMEN&format=csv
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?Modality=CT&format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?Modality=CT&format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?Modality=CT&format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?Modality=CT&format=csv
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?Collection=IDRI&format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?Collection=IDRI&format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?Collection=IDRI&format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?Collection=IDRI&format=csv
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?BodyPartExamined=ABDOMEN&format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?BodyPartExamined=ABDOMEN&format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?BodyPartExamined=ABDOMEN&format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getManufacturerValues?BodyPartExamined=ABDOMEN&format=csv

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


@Path("/v1/getManufacturerValues")
public class V1_getManufacturerValues {
	private static final String column="Manufacturer";
	public final static String TEXT_CSV = "text/csv";
	public final static MediaType TEXT_CSV_TYPE = new MediaType("text", "csv");

	@Context private HttpServletRequest httpRequest;

	/**
	 * This method get a set of Manufacturer filtered by query keys
	 *
	 * @return String - set of manufacture values
	 */
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, TEXT_CSV})

	public Response  constructResponse(@QueryParam("Collection") String collection, @QueryParam("format") String format,
			@QueryParam("Modality") String modality, @QueryParam("BodyPartExamined") String bodyPart) {


		String returnString = null;
		List<String> data = getDataFromDB (collection, modality, bodyPart);

		if ((data != null) && (data.size() > 0)) {
			if ((format == null) || (format.equalsIgnoreCase("JSON"))) {
				returnString = FormatOutput.toJSONArray(column, data).toString();
				return Response.ok(returnString).type("application/json").build();
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


	private List<String> getDataFromDB (String collection, String modality, String bodyPart) {
		List<String> results = null;
		List<SiteData> authorisedSites = (List)httpRequest.getAttribute("authorizedCollections");
		GeneralSeriesDAO tDao = (GeneralSeriesDAO)SpringApplicationContext.getBean("generalSeriesDAO");
		try {
			results = tDao.getManufacturerValues(collection, modality, bodyPart,authorisedSites);
		}
		catch (DataAccessException ex) {
			ex.printStackTrace();
		}
		return results;
	}
}
