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

import gov.nih.nci.nbia.dao.TrialDataProvenanceDAO;
import gov.nih.nci.nbia.util.SpringApplicationContext;

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


@Path("/v3/getProjSiteList")
public class V3_getProjSiteList extends getData{
	private static final String column="Collection//Site";
	public final static String TEXT_CSV = "text/csv";

	@Context private HttpServletRequest httpRequest;

	/**
	 * This method get a list of Project(or Collection, used interchangeably) and Site combination
	 *
	 * @return String - list of <Project Name>//<Site Name>
	 */
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, TEXT_CSV})

	public Response  constructResponse(@QueryParam("format") String format) {

		List<String> results = null;

		TrialDataProvenanceDAO tDao = (TrialDataProvenanceDAO)SpringApplicationContext.getBean("trialDataProvenanceDAO");
		try {
			results = tDao.getProjSiteValues();
			for (String projSiteName : results)
				System.out.println("Get project site combination="+ projSiteName);
		}
		catch (DataAccessException ex) {
			ex.printStackTrace();
		}

		return formatResponse(format, results, column);
	}
}
