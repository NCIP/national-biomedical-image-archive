package gov.nih.nci.nbia.restAPI;

import gov.nih.nci.nbia.dao.GeneralSeriesDAO;
import gov.nih.nci.nbia.dao.ImageDAO2;
import gov.nih.nci.nbia.dao.InstanceDAO;
import gov.nih.nci.nbia.dao.PatientDAO;
import gov.nih.nci.nbia.dao.StudyDAO;
import gov.nih.nci.nbia.dao.TrialDataProvenanceDAO;
import gov.nih.nci.nbia.restSecurity.AuthorizationService;
import gov.nih.nci.nbia.security.NCIASecurityManager;
import gov.nih.nci.nbia.security.TableProtectionElement;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.dao.ProtectionGroupSearchCriteria;
import gov.nih.nci.security.dao.RoleSearchCriteria;
import gov.nih.nci.security.dao.SearchCriteria;
import gov.nih.nci.security.dao.UserSearchCriteria;
import gov.nih.nci.security.exceptions.CSConfigurationException;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSObjectNotFoundException;
import gov.nih.nci.nbia.util.NCIAConfig;
import gov.nih.nci.nbia.util.SiteData;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.nbia.wadosupport.WADOParameters;
import gov.nih.nci.nbia.wadosupport.WADOSupportDAO;
import gov.nih.nci.nbia.wadosupport.WADOSupportDTO;
import gov.nih.nci.nbia.restUtil.FormatOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.sun.jersey.api.client.ClientResponse.Status;

public class getData {
	String applicationName = NCIAConfig.getCsmApplicationName();
	
	public UserProvisioningManager getUpm() throws CSException, CSConfigurationException {
		UserProvisioningManager upm = SecurityServiceProvider.getUserProvisioningManager(applicationName);
		return upm;
	}
	
	//Only used if the user is logged in.  Not for anonymousUser. There is a bug in SecurityContextHolder 
	//which will return the last logged in user if the user is not logged out. For getting around the problem,
	//the path interception will be used to get public collection for anonymousUser in getPublicCollections()
	//method.
	protected List<String> getAuthorizedCollections() throws Exception {
		List<String> authorizedCollections = null;
		try {
			Authentication authentication = SecurityContextHolder.getContext()
					.getAuthentication();
			System.out.println("!!!!!user name="
					+ authentication.getPrincipal());
			String userName = (String) authentication.getPrincipal();
			authorizedCollections = AuthorizationService
						.getAuthorizedCollections(userName);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new WebApplicationException(Response
					.status(Status.UNAUTHORIZED)
					.entity(ex.getLocalizedMessage()).build());
		}
		return authorizedCollections;
	}
	protected List<String> getAuthorizedCollections(String userName) throws Exception {
		List<String> authorizedCollections = null;
		try {
			authorizedCollections = AuthorizationService
						.getAuthorizedCollections(userName);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new WebApplicationException(Response
					.status(Status.UNAUTHORIZED)
					.entity(ex.getLocalizedMessage()).build());
		}
		return authorizedCollections;
	}
	protected List<String> getPublicCollections() throws Exception {
		List<String> authorizedCollections = null;
		try {
			authorizedCollections = AuthorizationService
					.getCollectionForPublicRole();
		} catch (Exception ex) {
			//ex.printStackTrace();
		//	throw new WebApplicationException(Response
			//		.status(Status.UNAUTHORIZED)
			//		.entity(ex.getLocalizedMessage()).build());
		}
		return authorizedCollections;
	}

	protected Response formatResponse(String format, List<String> data,
			String column) {
		String returnString = null;

		if ((data != null) && (data.size() > 0)) {
			if ((format == null) || (format.equalsIgnoreCase("JSON"))) {
				returnString = FormatOutput.toJSONArray(column, data)
						.toString();
				return Response.ok(returnString).type("application/json")
						.build();
			}

			if (format.equalsIgnoreCase("HTML")) {
				returnString = FormatOutput.toHtml(column, data);
				return Response.ok(returnString).type("text/html").build();
			}

			if (format.equalsIgnoreCase("XML")) {
				returnString = FormatOutput.toXml(column, data);
				return Response.ok(returnString).type("application/xml")
						.build();
			}
			if (format.equalsIgnoreCase("CSV")) {
				returnString = FormatOutput.toCsv(column, data);
				return Response.ok(returnString).type("text/csv").build();
			}
		} else {
			return Response.status(500).entity("No data found.").build();
		}
		return Response.status(500)
				.entity("Server was not able to process your request").build();
	}
	
	protected Response formatResponse(String format, List<Object[]> data, String[] columns) {
		String returnString = null;
		
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
					.entity("No data found.").build();
		}
		return Response.status(500)
				.entity("Server was not able to process your request")
				.build();	
	}
	protected Response formatResponseInstance(String format, List<Object[]> data, String[] columns) {
		String returnString = null;
		
		if ((data != null) && (data.size() > 0)) {
			if ((format == null) || (format.equalsIgnoreCase("JSON"))) {
				returnString = FormatOutput.toJSONArrayInstance(columns, data).toString();
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
					.entity("No data found.").build();
		}
		return Response.status(500)
				.entity("Server was not able to process your request")
				.build();	
	}
	protected boolean isUserHasAccess(String userName, Map<String, String> paramMap){
		if (userName == null) {
			Authentication authentication = SecurityContextHolder.getContext()
					.getAuthentication();
			userName = (String) authentication.getPrincipal();
			System.out.println("!!!!!user name="+ userName);
		}

		return AuthorizationService.isGivenUserHasAccess(userName, paramMap);
	}

	// For UPT replacement GUI
	public User getUserByLoginName(String loginName) throws Exception {
		UserProvisioningManager upm = getUpm();
		User user = new User();

		user.setLoginName(loginName);
		SearchCriteria searchCriteria = new UserSearchCriteria(user);
		List<User> list = upm.getObjects(searchCriteria);		
		return list.get(0);
	}
	
	// For UPT replacement GUI
	public ProtectionGroup getPGByPGName(String pgName) throws Exception {
		UserProvisioningManager upm = getUpm();
		ProtectionGroup pg = new ProtectionGroup();

		pg.setProtectionGroupName(pgName);		
		SearchCriteria searchCriteria = new ProtectionGroupSearchCriteria(pg);
		List<ProtectionGroup> list = upm.getObjects(searchCriteria);	
		return list.get(0);
	}

	// For UPT replacement GUI
	public Role getRoleByRoleName(String roleName) throws Exception {
		UserProvisioningManager upm = getUpm();
		Role role = new Role();

		role.setName(roleName);
		SearchCriteria searchCriteria = new RoleSearchCriteria(role);
		List<Role> list = upm.getObjects(searchCriteria);	
		return list.get(0);
	}		
	
	protected List<String> getModalityValues(String collection, String bodyPart, List<String> authorizedCollections) {
		List<String> results = null;

		GeneralSeriesDAO tDao = (GeneralSeriesDAO) SpringApplicationContext
				.getBean("generalSeriesDAO");
		try {
			results = tDao.getModalityValues(collection, bodyPart,
					authorizedCollections);
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		}
		return (List<String>) results;
	}
	

	protected List<String> getBodyPartValues(String collection, String modality, List<String> authorizedCollections) {
		List<String> results = null;

		GeneralSeriesDAO tDao = (GeneralSeriesDAO)SpringApplicationContext.getBean("generalSeriesDAO");
		try {
			results = tDao.getBodyPartValues(collection, modality, authorizedCollections);
		}
		catch (DataAccessException ex) {
			ex.printStackTrace();
		}
		return (List<String>) results;
	}
	
	protected List<String> getCollectionValues(List<String> authorizedCollections) {
		List<String> results = null;

		TrialDataProvenanceDAO tDao = (TrialDataProvenanceDAO)SpringApplicationContext.getBean("trialDataProvenanceDAO");
		try {
			results = tDao.getCollectionValues(authorizedCollections);
		}
		catch (DataAccessException ex) {
			ex.printStackTrace();
		}
		return (List<String>) results;
	}
	
	protected List<String> getManufacturerValues(String collection, String modality, String bodyPart, List<String> authorizedCollections) {
		List<String> results = null;
	
		GeneralSeriesDAO tDao = (GeneralSeriesDAO)SpringApplicationContext.getBean("generalSeriesDAO");
		try {
			results = tDao.getManufacturerValues(collection, modality, bodyPart, authorizedCollections);
		}
		catch (DataAccessException ex) {
			ex.printStackTrace();
		}
		return results;
	}
	
	
	protected List<Object[]> getPatientByCollection(String collection, List<String> authorizedCollections) {
		List<Object []> results = null;

		PatientDAO tDao = (PatientDAO)SpringApplicationContext.getBean("patientDAO");
		try {
			results = tDao.getPatientByCollection(collection, authorizedCollections);
		}
		catch (DataAccessException ex) {
			ex.printStackTrace();
		}
		return (List<Object[]>) results;
	}
	
	protected List<Object[]> getPatientStudy(String collection, String patientId, String studyInstanceUid, List<String> authorizedCollections) {
		List<Object []> results = null;

		StudyDAO tDao = (StudyDAO)SpringApplicationContext.getBean("studyDAO");
		try {
			results = tDao.getPatientStudy(collection, patientId, studyInstanceUid, authorizedCollections);
		}
		catch (DataAccessException ex) {
			ex.printStackTrace();
		}
		return (List<Object[]>) results;
	}
	

	protected List<Object[]> getSeries(String collection, String patientId, String studyInstanceUid, List<String> authorizedCollections) {
		List<Object[]> results = null;

		GeneralSeriesDAO tDao = (GeneralSeriesDAO)SpringApplicationContext.getBean("generalSeriesDAO");
		try {
			results = tDao.getSeries(collection, patientId, studyInstanceUid, authorizedCollections);
		}
		catch (DataAccessException ex) {
			ex.printStackTrace();
		}
		return results;
	}
	protected List<Object[]> getInstance(String sOPInstanceUID, String patientId, String studyInstanceUid, String seriesInstanceUid, List<String> authorizedCollections) {
		List<Object[]> results = null;

		InstanceDAO tDao = (InstanceDAO)SpringApplicationContext.getBean("instanceDAO");
		try {
			results = tDao.getImages(sOPInstanceUID, patientId, studyInstanceUid,  seriesInstanceUid, authorizedCollections);
		}
		catch (DataAccessException ex) {
			ex.printStackTrace();
		}
		return results;
	}
	protected List<String> getImage(String seriesInstanceUid) {
		List<String> results = null;

		ImageDAO2 tDao = (ImageDAO2)SpringApplicationContext.getBean("imageDAO2");
		try {
			results = tDao.getImage(seriesInstanceUid);
		}
		catch (DataAccessException ex) {
			ex.printStackTrace();
		}
		return (List<String>) results;
	}
	protected WADOSupportDTO getWadoImage(WADOParameters params, String user){
		
		WADOSupportDAO wadoDao = (WADOSupportDAO)SpringApplicationContext.getBean("WADOSupportDAO");
		WADOSupportDTO wdto = wadoDao.getWADOSupportDTO(params, user);
		return wdto;
		
	}
	protected WADOSupportDTO getWadoImage(String image, String contentType, String user){
		
		WADOSupportDAO wadoDao = (WADOSupportDAO)SpringApplicationContext.getBean("WADOSupportDAO");
		WADOSupportDTO wdto = wadoDao.getOviyamWADOSupportDTO(image, contentType, user);
		return wdto;
		
	}
}
