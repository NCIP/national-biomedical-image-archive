/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id: NCIASecurityManager.java 12937 2010-10-04 15:34:23Z kascice $
*
* $Log: not supported by cvs2svn $
* Revision 1.5  2008/02/07 18:14:16  lethai
* commented out debugging message
*
* Revision 1.4  2007/12/17 20:36:25  lethai
* changes for tracker id 8161 and 11009
*
* Revision 1.3  2007/10/01 12:22:10  bauerd
* *** empty log message ***
*
* Revision 1.2  2007/08/29 19:11:19  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/07 12:05:23  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:39  bauerd
* Initial Check in of reorganized components
*
* Revision 1.15  2006/11/20 17:57:08  panq
* Modified for caGrid which needs to grant the public access.
*
* Revision 1.14  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.security;

import gov.nih.nci.nbia.dao.AbstractDAO;
import gov.nih.nci.nbia.util.NCIAConfig;
import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroupRoleContext;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.dao.ProtectionGroupSearchCriteria;
import gov.nih.nci.security.dao.UserSearchCriteria;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSObjectNotFoundException;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public class NCIASecurityManagerImpl extends AbstractDAO
                                     implements NCIASecurityManager {

    private static Logger logger = Logger.getLogger(NCIASecurityManager.class);

    // Name of the "READ" Role
    private static final String readRoleName = RoleType.READ.toString();

    // Name of the public protection group
    private static final String publicProtectionGroupName = NCIAConfig.getProtectionElementPrefix() +
        "PUBLIC";

    // Primary key of the public protection group
    private static Long publicProtGroupId;
    private String applicationName = null;
    private UserProvisioningManager upm = null;
    private AuthenticationManager am = null;

    /*
     * Constructor
     */
    public NCIASecurityManagerImpl() {

    }

	@Transactional(propagation=Propagation.REQUIRED)
    public void init() throws DataAccessException {
    	try {
	        this.applicationName = "NCIA";
	        //logger.info("application name is " + name);
	        upm = (UserProvisioningManager)SecurityServiceProvider.getAuthorizationManager(this.applicationName);

	        am = SecurityServiceProvider.getAuthenticationManager(this.applicationName);
	        //logger.info("UserProvisioningManager: " + upm + " AuthenticationManager is " + am);

	        try {
	            // Get ID for public protection group
	            ProtectionGroup exampleProtGroup = new ProtectionGroup();
	            exampleProtGroup.setProtectionGroupName(publicProtectionGroupName);
	            ProtectionGroupSearchCriteria pgsc = new ProtectionGroupSearchCriteria(exampleProtGroup);

	            List<ProtectionGroup> protGroupResult = upm.getObjects(pgsc);
	            publicProtGroupId = ((ProtectionGroup) protGroupResult.get(0)).getProtectionGroupId();
	        } catch (Exception e) {

	            logger.error("A CSM protection group must be defined with the name " +
	                publicProtectionGroupName + " " +e);
	            throw new RuntimeException(e);
	        }
    	}
        catch(Exception ex) {
        	throw new RuntimeException(ex);
        }

    }
    /**
     * Returns an instance for the default application name
     *
     * @throws CSException
     */
//    public static NCIASecurityManager getInstance() throws Exception {
//        if (manager == null) {
//            manager = new NCIASecurityManagerImpl(NCIAConfig.getCsmApplicationName());
//        }
//
//        return manager;
//    }

    /*
     * login method is to check CSM database to validate user's username and
     * password @param username,password @return boolean
     */

    public boolean login(String username, String password)
        throws CSException {
    	//logger.info("NCIASecurityManager: username is " + username + " password is " + password);
        return am.login(username, password);
    }

	@Transactional(propagation=Propagation.REQUIRED)
	public boolean isInLocalDB(String username) {

		List<Object> results = null;
		String hql = "select user.loginName from NCIAUser user where ";
		hql = hql + "user.loginName = '" + username + "'";
		boolean flag = false;

		try {

			results = getHibernateTemplate().find(hql);
			if (results != null && results.size() > 0) {
				flag = true;
			}
			else {
				flag = false;
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			throw new RuntimeException("Could not execute the query.");
		}
		return flag;
	}

    /*
     * Build up the set of "protection elements" for a given user.
     * These fake protection elements are used to decide whether
     * a user can see a given collection, series, site, etc.
     *
     * <P>The authorization information is stored in CSM, so this
     * method queries CSM for the given user (login).  Then... the
     * TableProtectionElement objects are built in a non-obvious
     * way per "real" ProtectionElement in the CSM tables.
     */

    public Set<TableProtectionElement> getSecurityMap(String userId)
        throws CSObjectNotFoundException {
        Set<TableProtectionElement> retSet = new HashSet<TableProtectionElement>();
        Map<String, TableProtectionElement> tempHastable = new Hashtable<String, TableProtectionElement>();

        //userRoles = combination of user PG and users groups PGs
        Set<ProtectionGroupRoleContext> userRoles = new HashSet<ProtectionGroupRoleContext>();

        //step 1 get PG tied directly to user
        Set<ProtectionGroupRoleContext> userSpecificRoles = upm.getProtectionGroupRoleContextForUser(userId);
        userRoles.addAll(userSpecificRoles);

        //step 2 get PG tied to all the groups the user is a member of
        Set<Group> groups = upm.getGroups(userId);
        for(Group group : groups) {
        	Set<ProtectionGroupRoleContext> groupRoles = upm.getProtectionGroupRoleContextForGroup(Long.toString(group.getGroupId()));
        	userRoles.addAll(groupRoles);
        }

        //  Iterate over the protection groups
        for (ProtectionGroupRoleContext roleContext : userRoles) {
            ProtectionGroup group = roleContext.getProtectionGroup();
            Set<Role> roles = roleContext.getRoles();
            Set<ProtectionElement> tempProtectionElements = upm.getProtectionElements(group.getProtectionGroupId()
                                                                                           .toString());

            // Iterate over the protection elements in a protection group
            for (ProtectionElement pElement : tempProtectionElements) {
                // If the protection element already exists, add the roles
                if (tempHastable.containsKey(
                            pElement.getProtectionElementName())) {
                    TableProtectionElement tempEl = tempHastable.get(pElement.getProtectionElementName());
                    tempEl.addAllRoles(roles);

                    // if not, create a new protection element
                }
                else {
                    TableProtectionElement el = new TableProtectionElement(pElement);
                    el.addAllRoles(roles);
                    tempHastable.put(pElement.getProtectionElementName(), el);
                }
            }
        }

        retSet.addAll(tempHastable.values());

        return retSet;
    }

    /*
     * getSecurityMap method is to get all ProtectionElementPrivilegeContext for
     * a public user.  It will return a set of TableProtectionElements as well as the
     * roles that are associated to those TableProtectionElements
     *
     * @param - String userId @return - A set of ProtectionElement
     */

    public Set<TableProtectionElement> getSecurityMapForPublicRole()
        throws CSObjectNotFoundException {

        Set<ProtectionElement> pes = upm.getProtectionElements(publicProtGroupId.toString());
        Set<TableProtectionElement> retSet = new HashSet<TableProtectionElement>();
        Map<String, TableProtectionElement> tempHastable = new Hashtable<String, TableProtectionElement>();

        for (ProtectionElement pElement : pes) {
            TableProtectionElement el = new TableProtectionElement(pElement);
            el.addRole(readRoleName);
            tempHastable.put(pElement.getProtectionElementName(), el);
        }

        retSet.addAll(tempHastable.values());

        return retSet;
    }

    /*
     * Given a user's login name, return the id of that user in the
     * CSM_USER table.  For example, kascice -> 485.  If a user is not
     * found then null is returned.
     */

    public String getUserId(String loginName) {
        User user = new User();
        user.setLoginName(loginName);
        UserSearchCriteria usc = new UserSearchCriteria(user);
        List<User> userList = upm.getObjects(usc);

        if(userList.size()>0) {
        	return userList.get(0).getUserId().toString();
        }
        else {
        	return null;
        }
    }

    /*
     * Given a user's login name, return the email of that user in the
     * CSM_USER table.  For example, kascice -> kascice@mail.nih.gov.  If a user is not
     * found then null is returned.
     */

    public String getUserEmail(String loginName) {
        User user = new User();
        user.setLoginName(loginName);
        UserSearchCriteria usc = new UserSearchCriteria(user);
        List<User> userList = upm.getObjects(usc);

        if(userList.size()>0) {
            return userList.get(0).getEmailId();
        }
        else {
        	return null;
        }
    }
}
