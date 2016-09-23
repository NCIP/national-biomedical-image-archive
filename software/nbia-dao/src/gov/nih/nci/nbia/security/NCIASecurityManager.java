/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id$
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


import gov.nih.nci.nbia.util.NCIAConfig;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSObjectNotFoundException;

import java.util.Set;

import org.springframework.dao.DataAccessException;


public interface NCIASecurityManager {

    /**
     * Enum to contain all of the roles and their string values
     */
    public enum RoleType {

        READ(NCIAConfig.getProtectionElementPrefix() + "READ"),
		ADMIN(NCIAConfig.getProtectionElementPrefix() + "ADMIN"),
        MANAGE_VISIBILITY_STATUS(NCIAConfig.getProtectionElementPrefix() +
            "MANAGE_VISIBILITY_STATUS"),
        VIEW_SUBMISSION_REPORT(NCIAConfig.getProtectionElementPrefix() +
            "VIEW_SUBMISSION_REPORT"),
        DELETE_ADMIN(NCIAConfig.getProtectionElementPrefix() + "DELETE_ADMIN"),
        MANAGE_COLLECTION_DESCRIPTION(NCIAConfig.getProtectionElementPrefix() + "MANAGE_COLLECTION_DESCRIPTION"),
        SUPER_CURATOR(NCIAConfig.getProtectionElementPrefix() + "SUPER_CURATOR");
        private final String type;

        RoleType(String type) {
            this.type = type;
        }

        public String toString() {
            return type;
        }

    }

    public void init() throws DataAccessException;

    /*
     * login method is to check CSM database to validate user's username and
     * password @param username,password @return boolean
     */
    public boolean login(String username, String password) throws CSException;

	public boolean isInLocalDB(String username);
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
        throws CSObjectNotFoundException;
    /*
     * getSecurityMap method is to get all ProtectionElementPrivilegeContext for
     * a public user.  It will return a set of TableProtectionElements as well as the
     * roles that are associated to those TableProtectionElements
     *
     * @param - String userId @return - A set of ProtectionElement
     */
    public Set<TableProtectionElement> getSecurityMapForPublicRole()
        throws CSObjectNotFoundException;

    /*
     * Given a user's login name, return the id of that user in the
     * CSM_USER table.  For example, kascice -> 485.  If a user is not
     * found then null is returned.
     */
    public String getUserId(String loginName);

    /*
     * Given a user's login name, return the email of that user in the
     * CSM_USER table.  For example, kascice -> kascice@mail.nih.gov.  If a user is not
     * found then null is returned.
     */
    public String getUserEmail(String loginName);

    /*
     * Change the password for the user with loginName
     */
    public void modifyPasswordForNewUser(String loginName, String password) throws Exception;
}
