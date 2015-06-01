//To Test: http://localhost:8080/nbia-api/api/v1/getModalityValues
//To Test: http://localhost:8080/nbia-api/api/v1/getModalityValues?format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getModalityValues?format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getModalityValues?format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getModalityValues?format=csv
//To Test: http://localhost:8080/nbia-api/api/v1/getModalityValues?Collection=IDRI&BodyPartExamined=ABDOMEN&format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getModalityValues?Collection=IDRI&BodyPartExamined=ABDOMEN&format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getModalityValues?Collection=IDRI&BodyPartExamined=ABDOMEN&format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getModalityValues?Collection=IDRI&BodyPartExamined=ABDOMEN&format=csv
//To Test: http://localhost:8080/nbia-api/api/v1/getModalityValues?BodyPartExamined=ABDOMEN&format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getModalityValues?BodyPartExamined=ABDOMEN&format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getModalityValues?BodyPartExamined=ABDOMEN&format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getModalityValues?BodyPartExamined=ABDOMEN&format=csv
//To Test: http://localhost:8080/nbia-api/api/v1/getModalityValues?Collection=IDRI&format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getModalityValues?Collection=IDRI&format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getModalityValues?Collection=IDRI&format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getModalityValues?Collection=IDRI&format=csv

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

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.BadCredentialsException;


@Path("/v1/getModalityValues")
public class V1_getModalityValues extends getData{
	private static final String column="Modality";
	public final static String TEXT_CSV = "text/csv";

	@Context private HttpServletRequest httpRequest;
	/**
	 * This method get a set of all modality values (CT, MR, ...) filtered by query keys
	 *
	 * @return String - set of patient
	 */
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, TEXT_CSV})

	public Response  constructResponse(@QueryParam("Collection") String collection, @QueryParam("format") String format,
			@QueryParam("BodyPartExamined") String bodyPart) {
		List<String> authorizedCollections = null;
		try {
			authorizedCollections = getPublicCollections();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<String> data = getModalityValues (collection, bodyPart, authorizedCollections);
		return formatResponse(format, data, column);
	}
}