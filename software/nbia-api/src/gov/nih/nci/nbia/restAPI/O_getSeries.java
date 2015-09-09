//To Test: http://localhost:8080/nbia-api/api/v1/getSeries
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?format=csv
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?Collection=RIDER Pilot&PatientID=1.3.6.1.4.1.9328.50.1.0001&StudyInstanceUID=1.3.6.1.4.1.9328.50.1.2&format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?Collection=RIDER Pilot&PatientID=1.3.6.1.4.1.9328.50.1.0001&StudyInstanceUID=1.3.6.1.4.1.9328.50.1.2&format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?Collection=RIDER Pilot&PatientID=1.3.6.1.4.1.9328.50.1.0001&StudyInstanceUID=1.3.6.1.4.1.9328.50.1.2&format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?Collection=RIDER Pilot&PatientID=1.3.6.1.4.1.9328.50.1.0001&StudyInstanceUID=1.3.6.1.4.1.9328.50.1.2&format=csv
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?Collection=RIDER Pilot&PatientID=1.3.6.1.4.1.9328.50.1.0001&format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?Collection=RIDER Pilot&PatientID=1.3.6.1.4.1.9328.50.1.0001&format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?Collection=RIDER Pilot&PatientID=1.3.6.1.4.1.9328.50.1.0001&format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?Collection=RIDER Pilot&PatientID=1.3.6.1.4.1.9328.50.1.0001&format=csv
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?PatientID=1.3.6.1.4.1.9328.50.1.0001&StudyInstanceUID=1.3.6.1.4.1.9328.50.1.2&format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?PatientID=1.3.6.1.4.1.9328.50.1.0001&StudyInstanceUID=1.3.6.1.4.1.9328.50.1.2&format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?PatientID=1.3.6.1.4.1.9328.50.1.0001&StudyInstanceUID=1.3.6.1.4.1.9328.50.1.2&format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?PatientID=1.3.6.1.4.1.9328.50.1.0001&StudyInstanceUID=1.3.6.1.4.1.9328.50.1.2&format=csv
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?Collection=RIDER Pilot&StudyInstanceUID=1.3.6.1.4.1.9328.50.1.2&format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?Collection=RIDER Pilot&StudyInstanceUID=1.3.6.1.4.1.9328.50.1.2&format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?Collection=RIDER Pilot&StudyInstanceUID=1.3.6.1.4.1.9328.50.1.2&format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?Collection=RIDER Pilot&StudyInstanceUID=1.3.6.1.4.1.9328.50.1.2&format=csv
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?PatientID=1.3.6.1.4.1.9328.50.1.0001&format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?PatientID=1.3.6.1.4.1.9328.50.1.0001&format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?PatientID=1.3.6.1.4.1.9328.50.1.0001&format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?PatientID=1.3.6.1.4.1.9328.50.1.0001&format=csv
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?Collection=RIDER Pilot&format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?Collection=RIDER Pilot&format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?Collection=RIDER Pilot&format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?Collection=RIDER Pilot&format=csv
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?StudyInstanceUID=1.3.6.1.4.1.9328.50.1.2&format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?StudyInstanceUID=1.3.6.1.4.1.9328.50.1.2&format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?StudyInstanceUID=1.3.6.1.4.1.9328.50.1.2&format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getSeries?StudyInstanceUID=1.3.6.1.4.1.9328.50.1.3&format=csv

package gov.nih.nci.nbia.restAPI;

import gov.nih.nci.nbia.restUtil.OviyamUtil;

import java.util.List;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/o/getSeries")
public class O_getSeries extends getData {
	private static final String[] columns={"SeriesInstanceUID", "StudyInstanceUID", "Modality", "ProtocolName", "SeriesDate", "SeriesDescription", "BodyPartExamined", "SeriesNumber", "AnnotationsFlag", "Collection", "PatientID", "Manufacturer", "ManufacturerModelName", "SoftwareVersions", "ImageCount"};
	public final static String TEXT_CSV = "text/csv";

	@Context private HttpServletRequest httpRequest;
	/**
	 * This method get a set of Manufacturer filtered by query keys
	 *
	 * @return String - set of series info
	 */
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, TEXT_CSV})
	public Response  constructResponse(@QueryParam("Collection") String collection, @QueryParam("format") String format,
			@QueryParam("PatientID") String patientId, @QueryParam("StudyInstanceUID") String studyInstanceUid
			, @QueryParam("oviyamId") String oviyamId, @QueryParam("wadoUrl") String wadoUrl, @QueryParam("seriesUid") String seriesUid) {
		List<String> authorizedCollections = null;
		System.out.println("SeriesId in getSeries:"+seriesUid);
		try {
			String user=null;
			if (oviyamId!=null&&oviyamId.length()>0){
				user=OviyamUtil.getUser(oviyamId, wadoUrl);
			}
			if (user==null){
				authorizedCollections = getPublicCollections();
			} else {
				authorizedCollections = OviyamUtil.getUserCollections(user);
				if (authorizedCollections==null) {
				   authorizedCollections = getAuthorizedCollections(user);
				   OviyamUtil.setUserCollections(user, authorizedCollections);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Object[]> data = getSeries(collection, patientId, studyInstanceUid, authorizedCollections);
		if (seriesUid!=null&&seriesUid.length()>0&&!seriesUid.equals("null"))
		{
			for (Object[] object:data){
				if (object[0].toString().equals(seriesUid)){
					data=new ArrayList<Object[]>();
					data.add(object);
					break;
				}
				
			}
		}		
		return formatResponse(format, data, columns);
	}
}