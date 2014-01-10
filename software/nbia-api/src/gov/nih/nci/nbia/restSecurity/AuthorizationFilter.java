/**
 * 
 */
package gov.nih.nci.nbia.restSecurity;


import gov.nih.nci.nbia.dao.GeneralSeriesDAO;
import gov.nih.nci.nbia.security.NCIASecurityManager;
import gov.nih.nci.nbia.security.TableProtectionElement;
import gov.nih.nci.nbia.util.SiteData;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElementPrivilegeContext;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.exceptions.CSConfigurationException;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSObjectNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.core.util.Base64;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

/**
 * @author niktevv
 *
 */
public class AuthorizationFilter implements ContainerRequestFilter {

	private static final String AUTHORIZATION_PROPERTY = "Authorization";
	private static final String AUTHENTICATION_SCHEME = "Basic";
	
	@Context private HttpServletRequest httpRequest;
	
	public ContainerRequest filter(ContainerRequest request) {
	
		// Get the authentication passed in HTTP headers parameters
		String authorization = request.getHeaderValue(AUTHORIZATION_PROPERTY);
		String username = null;
		String password = null; 
		// If no authorization information present; block access
		if (authorization != null && !authorization.isEmpty()) {
			// Get encoded username and password
			final String encodedUserPassword = authorization.replaceFirst(AUTHENTICATION_SCHEME + " ", "");
			// Decode username and password
			String usernameAndPassword;
			usernameAndPassword = new String(Base64.decode(encodedUserPassword));
			// Split username and password tokens
			final StringTokenizer tokenizer = new StringTokenizer(
						usernameAndPassword, ":");
			username = tokenizer.nextToken();
			password = tokenizer.nextToken();
		} 
		// authenticate User name and password
		try {
			authenticateAndAuthorizeUser(username,password,request);
		}catch ( CSConfigurationException e) {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).entity(e.getLocalizedMessage()).build());
		} catch ( CSException e1) {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).entity(e1.getLocalizedMessage()).build());
		}
		catch (Exception e1) {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).entity(e1.getLocalizedMessage()).build());
		}
		return request;
	}
	private boolean authenticateAndAuthorizeUser(String username, String password,ContainerRequest request) throws Exception {
		MultivaluedMap<String,String> map = request.getQueryParameters();
		Map<String,String> queryParamsMap = new HashMap<String,String>();
		for (String key : new ArrayList<String>(map.keySet())) {
			queryParamsMap.put(key, map.get(key).get(0));
		}
		
		
		Map<String,String> parameterMapping = new HashMap<String, String>();
		parameterMapping.put("PatientID", "patientId");
		parameterMapping.put("Modality", "modality");
		parameterMapping.put("BodyPartExamined", "bodyPartExamined");
		parameterMapping.put("StudyInstanceUID", "studyInstanceUID");
		parameterMapping.put("SeriesInstanceUID", "seriesInstanceUID");
		parameterMapping.put("Collection", "project");
		
		String applicationName = "NCIA";
		AuthorizationManager authorizationManager  = SecurityServiceProvider.getAuthorizationManager(applicationName);
		AuthenticationManager authenticationManager  = SecurityServiceProvider.getAuthenticationManager(applicationName);
		User usr = null;
		boolean onlyPublicData = true;
		if(username !=null && password !=null) {
			authenticationManager.login(username, password);
			usr = authorizationManager.getUser(username);
		}
		List<SiteData> collectionRequested = new ArrayList<SiteData>();
		Map<String,String> daoParam = new HashMap<String, String>();
		Set<String> keySet = queryParamsMap.keySet();
		for (String name : keySet) {
			if(!name.equalsIgnoreCase("format")) {
				if (Arrays.asList("StudyInstanceUID","SeriesInstanceUID","PatientID","Collection").contains(name)) {
					onlyPublicData = false;
				}
				daoParam.put(parameterMapping.get(name),queryParamsMap.get(name));
			}
		}
		GeneralSeriesDAO generalSeriesDao = (GeneralSeriesDAO)SpringApplicationContext.getBean("generalSeriesDAO");
		collectionRequested.addAll(generalSeriesDao.getAuthorizedSecurityGroups(daoParam));
		
		//user is authenticated but not in local database		
		if (usr != null) {
			Set<ProtectionElementPrivilegeContext>  authorizedCollectionLst = authorizationManager.getProtectionElementPrivilegeContextForUser(usr.getUserId().toString());
			List<String> authorizedCollections= new ArrayList<String>();
			if(authorizedCollectionLst !=null) {
				for(ProtectionElementPrivilegeContext authorizedCollection : authorizedCollectionLst) {
					String protectionElementName = authorizedCollection.getProtectionElement().getProtectionElementName();
					if(protectionElementName.indexOf("//") != -1) {
						authorizedCollections.add(protectionElementName);
					}
				}
			}
			//check if request collection is part of authorized collection for user
			List<SiteData> authorizedProjectNameList = new ArrayList<SiteData>();
			List<SiteData> unAuthorizedList = new ArrayList<SiteData>();
			for (SiteData reqCollection :collectionRequested) {
				if (authorizedCollections.contains("NCIA."+reqCollection.getCollection()+"//"+reqCollection.getSiteName())) {
					  authorizedProjectNameList.add(reqCollection);
				} else {
					unAuthorizedList.add(reqCollection);
				}
			}
			if(onlyPublicData) {
				httpRequest.setAttribute("authorizedCollections", authorizedProjectNameList);	
			} else if (!unAuthorizedList.isEmpty()) {
				throw new Exception("Does not have access to requested information.");
			}
			
		} else {
				// check if requested collection is public then allow else throw
				//un-Authorized exception
				List<SiteData> publicCollections = new ArrayList<SiteData>();
				List<SiteData> privateCollections = new ArrayList<SiteData>();
				List<String> pePublic = getCollectionForPublicRole();
				for (SiteData collectionRq : collectionRequested) {
					if(pePublic.contains("NCIA."+collectionRq.getCollection())) {
						publicCollections.add(collectionRq);
					} else {
						privateCollections.add(collectionRq);
					}
//					ProtectionElement pe = null;
//					if(collectionRq.getCollection().equals("ISPY")) {
//						pe = authorizationManager.getProtectionElement("NCIA.ISPY ");
//					} else{
//					   pe = authorizationManager.getProtectionElement("NCIA." + collectionRq.getCollection());
//					}
//					Set pGs = authorizationManager.getProtectionGroups(pe.getProtectionElementId().toString());
//					for (Object pg : pGs) {
//						if ((((ProtectionGroup) pg).getProtectionGroupName()).contains("NCIA.PUBLIC") && !publicCollections.contains(collectionRq)) {
//							publicCollections.add(collectionRq);
//						}
//						protectionGroupLst.add(((ProtectionGroup) pg).getProtectionGroupName());
//					}
				}
				if(onlyPublicData) {
					httpRequest.setAttribute("authorizedCollections", publicCollections);
				} else if (!privateCollections.isEmpty()) {
					throw new Exception("Does not have access to requested information.");
				}
			}
		
		return true;
	}
	private List<String> getCollectionForPublicRole() throws CSObjectNotFoundException {
		List<String> protectionGroupLst = new ArrayList<String>();
		NCIASecurityManager mgr = (NCIASecurityManager)SpringApplicationContext.getBean("nciaSecurityManager");
		 Set<TableProtectionElement> publicPEs =  mgr.getSecurityMapForPublicRole();
		 for (TableProtectionElement tPE : publicPEs) {
			 protectionGroupLst.add(tPE.getAttributeValue());
			 
		 }
		 return protectionGroupLst;
	}
}
