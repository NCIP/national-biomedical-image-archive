//To Test: http://localhost:8080/nbia-api/api/v1/getCollectionValues?format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getCollectionValues?format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getCollectionValues?format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getCollectionValues?format=csv


package gov.nih.nci.nbia.restAPI;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MultivaluedMap;

import gov.nih.nci.nbia.dynamicsearch.DynamicSearchCriteria;
import gov.nih.nci.nbia.dynamicsearch.Operator;
import gov.nih.nci.nbia.dynamicsearch.QueryHandler;
import gov.nih.nci.nbia.factories.ApplicationFactory;
import gov.nih.nci.nbia.lookup.StudyNumberMap;
import gov.nih.nci.nbia.searchresult.PatientSearchResult;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.nbia.security.*;
import gov.nih.nci.nbia.textsupport.PatientTextSearchResult;
import gov.nih.nci.nbia.textsupport.PatientTextSearchResultImpl;
import gov.nih.nci.nbia.textsupport.SolrAllDocumentMetaData;
import gov.nih.nci.nbia.util.SiteData;
import gov.nih.nci.nbia.restUtil.AuthorizationUtil;
import gov.nih.nci.nbia.restUtil.JSONUtil;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
@Path("/getTextSearch")
public class GetTextSearch extends getData{
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

	public Response constructResponse(@FormParam("textValue") String textValue) {

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
		List <DynamicSearchCriteria> criteria=new ArrayList<DynamicSearchCriteria>();
		int i=0;
		QueryHandler qh = (QueryHandler)SpringApplicationContext.getBean("queryHandler");
		System.out.println("Searching Solr for"+textValue);
		List<SolrAllDocumentMetaData> results = qh.searchSolr(textValue);
		StringBuffer patientIDs = new StringBuffer();
		Map<String, SolrAllDocumentMetaData> patientMap=new HashMap<String, SolrAllDocumentMetaData>();
		for (SolrAllDocumentMetaData result : results)
		{
			patientIDs.append(result.getPatientId()+",");
			patientMap.put(result.getPatientId(), result);
		}
		if (patientIDs.toString().length()<2) patientIDs.append("zzz33333###"); // no patients found
		DynamicSearchCriteria dsc = new DynamicSearchCriteria();
		dsc.setField("patientId");
		dsc.setDataGroup("Patient");
		Operator op = new Operator();
		op.setValue("contains");
		dsc.setOperator(op);
		dsc.setValue(patientIDs.toString());

		criteria.clear();
		criteria.add(dsc);


			qh.setStudyNumberMap(ApplicationFactory.getInstance().getStudyNumberMap());
			qh.setQueryCriteria(criteria, "AND", authorizedSiteData, seriesSecurityGroups);
			qh.query();
			List<PatientSearchResult> patients = qh.getPatients();
			List<PatientSearchResult> textPatients = new ArrayList<PatientSearchResult>();
			for (PatientSearchResult patient:patients)
			{
				PatientTextSearchResult textResult=new PatientTextSearchResultImpl(patient);
				SolrAllDocumentMetaData solrResult =  patientMap.get(textResult.getSubjectId());
				if (solrResult==null)
				{
					System.out.println("******* can't find id in patient map " + textResult.getSubjectId());
				} else
				{
					textResult.setHit(solrResult.getFoundValue());
				    textPatients.add(textResult);
				}
			}
     
		
		return Response.ok(JSONUtil.getJSONforPatients(textPatients)).type("application/json")
				.build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.status(500)
				.entity("Server was not able to process your request").build();
	}
}
