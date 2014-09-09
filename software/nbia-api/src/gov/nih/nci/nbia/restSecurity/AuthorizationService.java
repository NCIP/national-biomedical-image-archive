/**
 *
 */
package gov.nih.nci.nbia.restSecurity;


import gov.nih.nci.nbia.dao.GeneralSeriesDAO;
import gov.nih.nci.nbia.security.NCIASecurityManager;
import gov.nih.nci.nbia.security.TableProtectionElement;
import gov.nih.nci.nbia.util.SiteData;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.nbia.util.NCIAConfig;
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

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.core.util.Base64;
import com.sun.jersey.spi.container.ContainerRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

/**
 * @author panq
 *
 */
public class AuthorizationService {

	// private static final String AUTHORIZATION_PROPERTY = "Authorization";
	// private static final String AUTHENTICATION_SCHEME = "Basic";

	public static List<String> getCollectionForPublicRole()
			throws CSObjectNotFoundException {
		List<String> publicProtectionElemLst = new ArrayList<String>();
		String csmContextName = NCIAConfig.getCsmApplicationName()+".";
		NCIASecurityManager mgr = (NCIASecurityManager) SpringApplicationContext
				.getBean("nciaSecurityManager");
		Set<TableProtectionElement> publicPEs = mgr
				.getSecurityMapForPublicRole();
		for (TableProtectionElement tPE : publicPEs) {
			String protectionElementName = tPE.getAttributeValue();
			if (protectionElementName.indexOf("//") != -1) {
				protectionElementName = protectionElementName.replaceFirst(
						csmContextName, "'");
				protectionElementName = protectionElementName.concat("'");
				publicProtectionElemLst .add(protectionElementName);
				System.out.println("!!!public protection group="
						+ protectionElementName);
			}
		}
		return publicProtectionElemLst;
	}


	public static List<String> getAuthorizedCollections(String userName) throws Exception {
		String applicationName = NCIAConfig.getCsmApplicationName();
		User usr = null;
		AuthorizationManager authorizationManager;
		List<String> authorizedCollections = null;

//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		System.out.println("!!!!!user name="+authentication.getPrincipal());
//		String userName = (String)authentication.getPrincipal();

		try {
			authorizationManager = SecurityServiceProvider.getAuthorizationManager(applicationName);
			usr = authorizationManager.getUser(userName);

			if (usr != null) {
				Set<ProtectionElementPrivilegeContext> authorizedCollectionLst = authorizationManager
						.getProtectionElementPrivilegeContextForUser(usr
								.getUserId().toString());

				if (authorizedCollectionLst != null) {
					authorizedCollections = new ArrayList<String>();
					for (ProtectionElementPrivilegeContext authorizedCollection : authorizedCollectionLst) {
						String protectionElementName = authorizedCollection
								.getProtectionElement()
								.getProtectionElementName();
						if (protectionElementName.indexOf("//") != -1) {
							//make it ready to be used in sql
							protectionElementName = protectionElementName.replaceFirst(applicationName+".", "'");
							protectionElementName = protectionElementName.concat("'");
//							protectionElementName = protectionElementName.replaceFirst("NCIA.", "('");
//							protectionElementName = protectionElementName.replaceFirst("//", "', '");
//							protectionElementName = protectionElementName.concat("')");
							authorizedCollections.add(protectionElementName);
							System.out.println("!!!!!user name="+userName);
							System.out.println("!!!!protection element name ="+protectionElementName);
						}
					}
				}
				return authorizedCollections;

			} else {
				throw new Exception("The user " + userName
						+ " cannot be found in authorization database");
			}
		} catch (CSConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isGivenUserHasAccess(String userName, Map<String,String> queryParamsMap) {
	List<String> projAndSiteRequested = new ArrayList<String>();
	List<String> authorizedProjAndSite = null;

	try {
	if (userName.equalsIgnoreCase("anonymousUser")) {
		authorizedProjAndSite = AuthorizationService
				.getCollectionForPublicRole();
	} else {
		authorizedProjAndSite = AuthorizationService
				.getAuthorizedCollections(userName);
	}

	GeneralSeriesDAO generalSeriesDao = (GeneralSeriesDAO)SpringApplicationContext.getBean("generalSeriesDAO");
	projAndSiteRequested.addAll(generalSeriesDao.getRequestedProjectAndSite(queryParamsMap));

	if (authorizedProjAndSite != null) {
		//check if request collection is part of authorized collection for user
		for (String reqPandS :projAndSiteRequested) {
			System.out.println("requested Project and site ="+reqPandS);
			String formatedS = "'"+ reqPandS +"'";
			if (authorizedProjAndSite.contains(formatedS))
			return true;
		}
	}
	else {
		return false;
		 //throw new  CSObjectNotFoundException ("The user "+userName+" is not in authrization database");
		//user is authenticated but not in local database
	}
    }
    catch (Exception ex) {
    	ex.printStackTrace();
    }
	return false;
	}
}
