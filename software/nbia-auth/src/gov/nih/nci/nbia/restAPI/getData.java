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
			//test code
			//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@ Get ProtectionGroupList @@@@@@@@@@");
			//AuthorizationService.getProtectionGroupList();
			//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@ Get Exiting user list @@@@@@@@@@");
			//AuthorizationService.getExistingUserList();
			//AuthorizationService.modifyExistingUser("707363882", "panq@mail.nih.gov", false);
			//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@ test create a new user @@@@@@@@@@");
			//AuthorizationService.createNewUser("Tester21", "panq21@mail.nih.gov", false);
			//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@ test create a new protection group @@@@@@@@@@");
			//AuthorizationService.createNewProtectionGroup("NCIA.UPTRestTest", "Test Protection Group creation REST API");
			//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@ test delete protection group @@@@@@@@@@");
			//AuthorizationService.deleteExistProtectionGroup("865828918");
			//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@ test delete a new protection group @@@@@@@@@@");
			//AuthorizationService.deleteExistProtectionGroup(protectionGroupId);
			//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@ test assign PEs to a existing protection group @@@@@@@@@@");
			//AuthorizationService.assignPEsToPG("testProj_qp//testSite_qp", "NCIA.ISPY");
			//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@ test deassign PEs to a existing protection group @@@@@@@@@@");
			//AuthorizationService.deassignPEsToPG("TestJune//SiteName", "NCIA.Appscan");
			//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@ test deassign PEs to a existing protection group @@@@@@@@@@");
			//AuthorizationService.deassignPEsToPG("TestJune//SiteName", "NCIA.allrole");
			
			//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@ test assign a user with a role to a existing protection group @@@@@@@@@@");
			//AuthorizationService.assignUserToPGWithRole("panq", "NCIA.TCGA//Duke", "NCIA.READ" );
			
			//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@ test assign a user with a role to a existing protection group @@@@@@@@@@");
			//AuthorizationService.deassignUserToPGWithRole("panq", "NCIA.TCGA//Duke", "NCIA.READ" );
			
			//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@ test ProtectGroup/PEs list @@@@@@@@@@");
			//AuthorizationService.getAllPGWithPE();
			//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@ test All User ProtectGroup/Roles list @@@@@@@@@@");
			//AuthorizationService.getAllUserWithRoleForPG();
			
			//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@ Get protection elem list @@@@@@@@@@");
			//AuthorizationService.getProtectionElemList();
			//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@ Get role list @@@@@@@@@@");
			//AuthorizationService.getRoleList();
			//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@ Get proj//site list @@@@@@@@@@");
			//AuthorizationService.getProjSiteList();
			
			//test code
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
}
