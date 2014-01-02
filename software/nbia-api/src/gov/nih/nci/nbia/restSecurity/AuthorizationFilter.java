/**
 * 
 */
package gov.nih.nci.nbia.restSecurity;


import gov.nih.nci.nbia.dao.GeneralSeriesDAO;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElementPrivilegeContext;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.exceptions.CSConfigurationException;
import gov.nih.nci.security.exceptions.CSException;

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
		
		String applicationName = "NCIA";
		AuthorizationManager authorizationManager  = SecurityServiceProvider.getAuthorizationManager(applicationName);
		AuthenticationManager authenticationManager  = SecurityServiceProvider.getAuthenticationManager(applicationName);
		User usr = null;
		boolean onlyModalityParam = true;
		boolean onlyBodyPartParam = true;
		if(username !=null && password !=null) {
			authenticationManager.login(username, password);
			usr = authorizationManager.getUser(username);
		}
		List<String> collectionRequested = new ArrayList<String>();
		for (String name : new ArrayList<String>(queryParamsMap.keySet())) {
			if (name.equalsIgnoreCase("collection")) {
				collectionRequested.add(queryParamsMap.get(name));
				onlyModalityParam = false;
				onlyBodyPartParam = false;
			} else if(!name.equalsIgnoreCase("format")) {
				if (Arrays.asList("StudyInstanceUID","SeriesInstanceUID","PatientID").contains(name)) {
					onlyModalityParam = false;
					onlyBodyPartParam = false;
				}
				GeneralSeriesDAO generalSeriesDao = (GeneralSeriesDAO)SpringApplicationContext.getBean("generalSeriesDAO");
				collectionRequested.addAll(generalSeriesDao.getAuthorizedSecurityGroups(parameterMapping.get(name),queryParamsMap.get(name)));
				
			}
			
			System.out.println("requested param:-" + name + queryParamsMap.get(name));
			System.out.println("requested param's collection name:-"+ collectionRequested);
		}
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
			if(onlyModalityParam) {
				List<String> reqAuthList =  new ArrayList<String>();
				for (String reqCollection :collectionRequested) {
					if (authorizedCollections.contains("NCIA."+reqCollection)) {
						reqAuthList.add(reqCollection);
					}
				}
				httpRequest.setAttribute("authorizedCollections", reqAuthList);
			} else {
				//check if request collection is part of authorized collection for user
				for (String reqCollection :collectionRequested) {
					if (!authorizedCollections.contains("NCIA."+reqCollection)) {
						throw new Exception("Does not have access to requested information.");
					}
				}
			}
		} else {
			if(onlyModalityParam) {
				List<String> reqAuthList =  new ArrayList<String>();
				for (String reqCollection :collectionRequested) {
					ProtectionElement pe = authorizationManager.getProtectionElement("NCIA." + reqCollection);
					Set pGs = authorizationManager.getProtectionGroups(pe.getProtectionElementId().toString());
					List<String> protectionGroupLst = new ArrayList<String>();
					for (Object pg : pGs) {
						System.out.println("protection group of reuested prarm#########:-" + ((ProtectionGroup) pg).getProtectionGroupName());
						protectionGroupLst.add(((ProtectionGroup) pg).getProtectionGroupName());
					}
					if (protectionGroupLst.contains("NCIA.PUBLIC")) {
						reqAuthList.add(reqCollection);
					}
				}
				httpRequest.setAttribute("authorizedCollections", reqAuthList);
			} else {
				
			// check if requested collection is public then allow else throw
			// un-Authorized exception
			for (String collectionRq : collectionRequested) {
				ProtectionElement pe = authorizationManager.getProtectionElement("NCIA." + collectionRq);
				Set pGs = authorizationManager.getProtectionGroups(pe.getProtectionElementId().toString());
				List<String> protectionGroupLst = new ArrayList<String>();
				for (Object pg : pGs) {
					System.out.println("protection group of reuested prarm @@@@:-" + ((ProtectionGroup) pg).getProtectionGroupName());
					protectionGroupLst.add(((ProtectionGroup) pg).getProtectionGroupName());
				}
				if (!protectionGroupLst.contains("NCIA.PUBLIC")) {
					throw new Exception("Does not have access to collection");
				}

			}
			}
		}
		return true;
	}
}
