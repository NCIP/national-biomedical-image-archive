//To Test: http://localhost:8080/nbia-api/api/v1/getCollectionValues?format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getCollectionValues?format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getCollectionValues?format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getCollectionValues?format=csv


package gov.nih.nci.nbia.restAPI;

import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MultivaluedMap;

import gov.nih.nci.ncia.criteria.*;
import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.nbia.dynamicsearch.DynamicSearchCriteria;
import gov.nih.nci.nbia.dynamicsearch.Operator;
import gov.nih.nci.nbia.dynamicsearch.QueryHandler;
import gov.nih.nci.nbia.lookup.StudyNumberMap;
import gov.nih.nci.nbia.search.PatientSearcher;
import gov.nih.nci.nbia.searchresult.PatientSearchResult;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.nbia.security.*;
import gov.nih.nci.nbia.util.SiteData;
import gov.nih.nci.nbia.restUtil.AuthorizationUtil;
import gov.nih.nci.nbia.restUtil.JSONUtil;

import java.text.SimpleDateFormat;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
@Path("/getSimpleSearch")
public class GetSimpleSearch extends getData{
	private static final String column="Collection";
	public final static String TEXT_CSV = "text/csv";

	@Context private HttpServletRequest httpRequest;
	/**
	 * This method get a set of all collection names
	 *
	 * @return String - set of collection names
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)

	public Response constructResponse(MultivaluedMap<String, String> inFormParams) {

		try {	
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		String userName = (String) authentication.getPrincipal();
		List<SiteData> authorizedSiteData = AuthorizationUtil.getUserSiteData(userName);
		if (authorizedSiteData==null){
		     AuthorizationManager am = new AuthorizationManager(userName);
		     authorizedSiteData = am.getAuthorizedSites();
		     AuthorizationUtil.setUserSites(userName, authorizedSiteData);
		}
		List<String> seriesSecurityGroups = new ArrayList<String>();
		int i=0;
		DICOMQuery query=new DICOMQuery();
		AuthorizationCriteria auth = new AuthorizationCriteria();
		auth.setSeriesSecurityGroups(seriesSecurityGroups);
		auth.setSites(authorizedSiteData);
		query.setCriteria(auth);

		while (inFormParams.get("criteriaType"+i)!=null)
		{
			if (inFormParams.get("criteriaType"+i).get(0).equalsIgnoreCase("CollectionCriteria")){
				if (query.getCollectionCriteria()==null){
				   CollectionCriteria criteria=new CollectionCriteria();
				   criteria.setCollectionValue(inFormParams.get("value"+i).get(0));
				   query.setCriteria(criteria);
				} else {
					query.getCollectionCriteria().setCollectionValue(inFormParams.get("value"+i).get(0));
				}
			}
			if (inFormParams.get("criteriaType"+i).get(0).equalsIgnoreCase("ImageModalityCriteria")){
				if (query.getImageModalityCriteria()==null){
					ImageModalityCriteria criteria=new ImageModalityCriteria();
				   criteria.setImageModalityValue(inFormParams.get("value"+i).get(0));
				   query.setCriteria(criteria);
				} else {
					query.getImageModalityCriteria().setImageModalityValue(inFormParams.get("value"+i).get(0));
				}
			}
			
			if (inFormParams.get("criteriaType"+i).get(0).equalsIgnoreCase("AnatomicalSiteCriteria")){
				if (query.getAnatomicalSiteCriteria()==null){
					AnatomicalSiteCriteria criteria=new AnatomicalSiteCriteria();
				   criteria.setAnatomicalSiteValue(inFormParams.get("value"+i).get(0));
				   query.setCriteria(criteria);
				} else {
					query.getAnatomicalSiteCriteria().setAnatomicalSiteValue(inFormParams.get("value"+i).get(0));
				}
			}
			if (inFormParams.get("criteriaType"+i).get(0).equalsIgnoreCase("ManufacturerCriteria")){
				if (query.getManufacturerCriteria()==null){
					ManufacturerCriteria criteria=new ManufacturerCriteria();
				   criteria.setCollectionValue(inFormParams.get("value"+i).get(0));
				   query.setCriteria(criteria);
				} else {
					query.getManufacturerCriteria().setCollectionValue(inFormParams.get("value"+i).get(0));
				}
			}
			if (inFormParams.get("criteriaType"+i).get(0).equalsIgnoreCase("DateRangeCriteria")){
				DateRangeCriteria criteria=new DateRangeCriteria();
				SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");
			    criteria.setFromDate(formatter.parse(inFormParams.get("fromDate"+i).get(0)));
			    criteria.setToDate(formatter.parse(inFormParams.get("toDate"+i).get(0)));
				query.setCriteria(criteria);
			}
			if (inFormParams.get("criteriaType"+i).get(0).equalsIgnoreCase("PatientCriteria")){
				if (query.getPatientCriteria()==null){
					PatientCriteria criteria=new PatientCriteria();
				   criteria.setCollectionValue(inFormParams.get("value"+i).get(0));
				   query.setCriteria(criteria);
				} else {
					query.getPatientCriteria().setCollectionValue(inFormParams.get("value"+i).get(0));
				}
			}
			if (inFormParams.get("criteriaType"+i).get(0).equalsIgnoreCase("MinNumberOfStudiesCriteria")){
				MinNumberOfStudiesCriteria criteria=new MinNumberOfStudiesCriteria();
			    criteria.setMinNumberOfStudiesValue(new Integer(inFormParams.get("value"+i).get(0)));
				query.setCriteria(criteria);
			}
			i++;
		}
        PatientSearcher patientSearcher = new PatientSearcher();
        List<PatientSearchResult> patients = patientSearcher.searchForPatients(query);
		
		return Response.ok(JSONUtil.getJSONforPatients(patients)).type("application/json")
				.build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.status(500)
				.entity("Server was not able to process your request").build();
	}
}
