/**
 *
 */
package gov.nih.nci.nbia.restSecurity;

import gov.nih.nci.nbia.dao.GeneralSeriesDAO;
import gov.nih.nci.nbia.dao.TrialDataProvenanceDAO;

import org.springframework.dao.DataAccessException;

import gov.nih.nci.nbia.security.NCIASecurityManager;
import gov.nih.nci.nbia.security.TableProtectionElement;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.nbia.util.NCIAConfig;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElementPrivilegeContext;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroupRoleContext;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.exceptions.CSConfigurationException;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSObjectNotFoundException;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.constants.Constants;
import gov.nih.nci.security.dao.ProtectionElementSearchCriteria;
import gov.nih.nci.security.dao.ProtectionGroupSearchCriteria;
import gov.nih.nci.security.dao.RoleSearchCriteria;
import gov.nih.nci.security.dao.SearchCriteria;
import gov.nih.nci.security.dao.UserSearchCriteria;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Date;
import java.sql.Timestamp;

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







	// For UPT replacement GUI
	public static void deassignUserToPGWithRole(String loginName, String pgName, String roleName) throws Exception {
		String applicationName = NCIAConfig.getCsmApplicationName();
		UserProvisioningManager upm = null;

		try {
			upm = (UserProvisioningManager)SecurityServiceProvider.getAuthorizationManager(applicationName);

			//getProtection using protection group name
			ProtectionGroup pg = getPGByPGName(pgName);
			Role role = getRoleByRoleName(roleName);
			User user = getUserByLoginName(loginName);
			String [] roleIds = {role.getId().toString()};
			System.out.println("@@@get uid="+user.getUserId() +"; pgID="+pg.getProtectionGroupId() + "; roleID="+role.getId());
			upm.removeUserRoleFromProtectionGroup(pg.getProtectionGroupId().toString(), user.getUserId().toString(), roleIds);
			// TODO Auto-generated catch block
		} catch (CSConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	// For UPT replacement GUI
//	public static void deassignPEsToPG(String projAndSite, String pGroupName) throws Exception {
//		String applicationName = NCIAConfig.getCsmApplicationName();
//		UserProvisioningManager upm = null;
//		System.out.println("######## test deassign projAndSite="+projAndSite + "; PG name= "+pGroupName);
//
//		String projName = null;
//		String [] parsedS = projAndSite.split("//");
//		if ((parsedS != null) && (parsedS.length == 2)){
//			projName = parsedS[0];
//			System.out.println("extract pe ="+ projName);
//			}
//		else {
//			throw new Exception("Please check the format of input param.  It should be <Project Name>//<Site Name>."
//					+ " Please note \"//\" is needed between project name and site name");
//		}
//		try {
//
//			upm = SecurityServiceProvider.getUserProvisioningManager(applicationName);
//
//			ProtectionElement protectionElm1= upm.getProtectionElement("NCIA."+projName, "NCIA.PROJECT");
//			ProtectionElement protectionElm2 = upm.getProtectionElement("NCIA."+projAndSite, "NCIA.PROJECT//DP_SITE_NAME");
//			ProtectionGroup pg = getPGByPGName(pGroupName);
//
//			if (protectionElm1 != null) {
//				Set pgs = upm.getProtectionGroups(protectionElm1.getProtectionElementId().toString());
//				if (pgs.size()==1) {
//					System.out.println("@@@@@@@@protection elements belong to 1 pg =" + pgs.size() );
//
//					if (protectionElm2 != null) {
//						String [] peIds = {protectionElm1.getProtectionElementId().toString(), protectionElm2.getProtectionElementId().toString()};
//						upm.removeProtectionElementsFromProtectionGroup(pg.getProtectionGroupId().toString(), peIds);
//						upm.removeProtectionElement(protectionElm1.getProtectionElementId().toString());
//						upm.removeProtectionElement(protectionElm2.getProtectionElementId().toString());
//					}
//
//				}
//				//else if protection elements belongs to multiple protection group
//				else  if (pgs.size() > 1){
//					System.out.println("@@@@@@@@protection elements belong to " + pgs.size() + " pg; pe1 ID = "
//				+ protectionElm1.getProtectionElementId() + "; pe2 ID = " + protectionElm2.getProtectionElementId());
//					String [] peIds = {protectionElm1.getProtectionElementId().toString(), protectionElm2.getProtectionElementId().toString()};
//					ProtectionGroup[] pgList = (ProtectionGroup[]) pgs.toArray(new ProtectionGroup[0]);
//					upm.removeProtectionElementsFromProtectionGroup(pgList[0].getProtectionGroupId().toString(), peIds);
//				}
//			}
//		} catch (CSConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (CSException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	// For UPT replacement GUI
	public static User getUserByLoginName(String loginName) throws Exception {
		String applicationName = NCIAConfig.getCsmApplicationName();
		UserProvisioningManager upm = (UserProvisioningManager)SecurityServiceProvider.getAuthorizationManager(applicationName);
		User user = new User();

		user.setLoginName(loginName);
		SearchCriteria searchCriteria = new UserSearchCriteria(user);
		List<User> list = upm.getObjects(searchCriteria);
		return list.get(0);
	}

	// For UPT replacement GUI
	public static ProtectionGroup getPGByPGName(String pgName) throws Exception {
		String applicationName = NCIAConfig.getCsmApplicationName();
		UserProvisioningManager upm = (UserProvisioningManager)SecurityServiceProvider.getAuthorizationManager(applicationName);
		ProtectionGroup pg = new ProtectionGroup();

		pg.setProtectionGroupName(pgName);
		SearchCriteria searchCriteria = new ProtectionGroupSearchCriteria(pg);
		List<ProtectionGroup> list = upm.getObjects(searchCriteria);
		return list.get(0);
	}

	// For UPT replacement GUI
	public static Role getRoleByRoleName(String roleName) throws Exception {
		String applicationName = NCIAConfig.getCsmApplicationName();
		UserProvisioningManager upm = (UserProvisioningManager)SecurityServiceProvider.getAuthorizationManager(applicationName);
		Role role = new Role();

		role.setName(roleName);
		SearchCriteria searchCriteria = new RoleSearchCriteria(role);
		List<Role> list = upm.getObjects(searchCriteria);
		return list.get(0);
	}

}
