/**
 * 
 */
package gov.nih.nci.nbia.restSecurity;


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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.ws.rs.WebApplicationException;
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
    
	public ContainerRequest filter(ContainerRequest request) {
		MultivaluedMap<String,String> map = request.getQueryParameters();
		Map<String,String> queryParamMap = new HashMap<String,String>();
		for (String key : new ArrayList<String>(map.keySet())) {
			queryParamMap.put(key, map.get(key).get(0));
		}

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
			authenticateAndAuthorizeUser(username,password,queryParamMap);
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
	private boolean authenticateAndAuthorizeUser(String username, String password, Map<String,String> queryParamsMap) throws Exception {
		String applicationName = "NCIA";
		AuthorizationManager authorizationManager  = SecurityServiceProvider.getAuthorizationManager(applicationName);
		AuthenticationManager authenticationManager  = SecurityServiceProvider.getAuthenticationManager(applicationName);
		User usr = null;
		if(username !=null && password !=null) {
			authenticationManager.login(username, password);
			usr = authorizationManager.getUser(username);
		}
		//SecurityDAO dao = new SecurityDAO();
		List<String> collectionRequested = null;
		for (String name : new ArrayList<String>(queryParamsMap.keySet())) {
			//collectionRequested = dao.findProjectSiteName(name, queryParamsMap.get(name));
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
			//check if request collection is part of authorized collection for user
			for (String reqCollection :collectionRequested) {
				if (!authorizedCollections.contains("NCIA."+reqCollection)) {
					throw new Exception("Does not have access to requested information.");
				}
			}
		} else {

			//check if requested collection is public then allow else throw un-Authorized exception
			if(collectionRequested !=null) {
				for (String collectionRq : collectionRequested ) {
					ProtectionElement pe = authorizationManager.getProtectionElement("NCIA."+collectionRq);
					Set pGs = authorizationManager.getProtectionGroups(pe.getProtectionElementId().toString());
					List<String> protectionGroupLst = new ArrayList<String>();
					for(Object pg : pGs) {
						System.out.println("protection group of reuested prarm:-" + ((ProtectionGroup)pg).getProtectionGroupName());
						protectionGroupLst.add(((ProtectionGroup)pg).getProtectionGroupName());
					}
					if(!protectionGroupLst.contains("NCIA.PUBLIC")) {
						throw new Exception("Does not have access to collection");
					}
						
				}
			}
			
		}
		return true;
	}
}
