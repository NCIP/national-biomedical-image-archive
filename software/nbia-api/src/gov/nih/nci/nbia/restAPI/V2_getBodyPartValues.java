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


@Path("/v2/getBodyPartValues")
public class V2_getBodyPartValues extends getData{
	private static final String column="BodyPartExamined";
	public final static String TEXT_CSV = "text/csv";

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
		//String returnString = null;
		List<String> authorizedCollections = null;
		try {
			authorizedCollections = getAuthorizedCollections();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<String> data = getBodyPartValues(collection, modality, authorizedCollections);
		return formatResponse(format, data, column);
	}
}
