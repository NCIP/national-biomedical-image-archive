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
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MultivaluedMap;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import gov.nih.nci.nbia.dynamicsearch.DynamicSearchCriteria;
import gov.nih.nci.nbia.dynamicsearch.Operator;
import gov.nih.nci.nbia.dynamicsearch.QueryHandler;
import gov.nih.nci.nbia.lookup.StudyNumberMap;
import gov.nih.nci.nbia.searchresult.PatientSearchResult;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.nbia.security.*;
import gov.nih.nci.nbia.util.SiteData;
import gov.nih.nci.nbia.restUtil.AuthorizationUtil;
import gov.nih.nci.nbia.restUtil.JSONUtil;
import gov.nih.nci.nbia.dto.CustomSeriesListDTO;
import gov.nih.nci.nbia.dto.SeriesDTO;
import gov.nih.nci.nbia.dao.CustomSeriesListDAO;
import gov.nih.nci.nbia.dao.GeneralSeriesDAO;
@Path("/createSharedList")
public class CreateSharedList extends getData{
	private static final String column="Collection";
	public final static String TEXT_CSV = "text/csv";

	@Context private HttpServletRequest httpRequest;
	/**
	 * This method get a set of all collection names
	 *
	 * @return String - set of collection names
	 */
	@POST
	@Produces(MediaType.TEXT_PLAIN)

	public Response constructResponse(@FormParam("list") List<String> list, @FormParam("name") String name, 
			@FormParam("description") String description, @FormParam("url") String url) {

		try {	
	   Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		String user = (String) authentication.getPrincipal();
		List<SiteData> authorizedSiteData = AuthorizationUtil.getUserSiteData(user);
		if (authorizedSiteData==null){
		     AuthorizationManager am = new AuthorizationManager(user);
		     authorizedSiteData = am.getAuthorizedSites();
		     AuthorizationUtil.setUserSites(user, authorizedSiteData);
		}

		List<String> noPermissionList = validate(list, authorizedSiteData);
		if (noPermissionList.size()>0){
    		return Response.status(400).type("text/plain")
			.entity("User does not have permission for all series in list")
			.build();
		}
		CustomSeriesListDAO customSeriesListDAO = (CustomSeriesListDAO)SpringApplicationContext.getBean("customSeriesListDAO");
		if (customSeriesListDAO.isDuplicateName(name)){
    		return Response.status(400).type("text/plain")
			.entity("Duplicate list name")
			.build();
		}
		CustomSeriesListDTO  csDTO=new CustomSeriesListDTO();
		csDTO.setSeriesInstanceUIDs(list);
		csDTO.setName(name);
		csDTO.setComment(description);
		csDTO.setHyperlink(url);
		customSeriesListDAO.insert(csDTO, user);
		
		return Response.ok().type("text/plain")
				.entity("List created")
				.build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.status(500)
				.entity("Server was not able to process your request").build();
	}
	/**
	 * Determine whether user has permission to see the seriesUids
	 * @param seriesUids
	 * @return the seriesUids that user does not have permission to see
	 */
	public List<String> validate(List<String> seriesUids, List<SiteData> authorizedSites){
		List<String> noPermissionList = new ArrayList<String>();
		    GeneralSeriesDAO generalSeriesDAO = (GeneralSeriesDAO)SpringApplicationContext.getBean("generalSeriesDAO");
			List<SeriesDTO> seriesDTOsFound = generalSeriesDAO.findSeriesBySeriesInstanceUID(seriesUids, authorizedSites, null);
			//user do not have permission to see any in the list.
			if(seriesDTOsFound.isEmpty()){
				System.out.println("The returned SeriesDTOs is empty. user do not have permission to see any series in the list.");
				return seriesUids;
			}
			//System.out.println("found: " + seriesDTOsFound.size());
			List<String> seriesUidsFound = new ArrayList<String>();
			for(SeriesDTO seriesDTO: seriesDTOsFound){
				String seriesInstanceUid = seriesDTO.getSeriesUID();
				seriesUidsFound.add(seriesInstanceUid);	
			}
			for(String seriesUid : seriesUids){
				if(!seriesUidsFound.contains(seriesUid)){
					noPermissionList.add(seriesUid);
				}
			}
		return noPermissionList;
	}
}
