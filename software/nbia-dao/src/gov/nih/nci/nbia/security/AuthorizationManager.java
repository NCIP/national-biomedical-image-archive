/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id$
*/
package gov.nih.nci.nbia.security;

import gov.nih.nci.ncia.criteria.AuthorizationCriteria;
import gov.nih.nci.ncia.criteria.CollectionCriteria;
import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.nbia.security.NCIASecurityManager.RoleType;
import gov.nih.nci.nbia.util.NCIAConfig;
import gov.nih.nci.nbia.util.SiteData;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * This object is instantiated for a given user (or "public") in order to
 * allow querying of what collections, sites, series, etc.
 * a user is authorized to view.
 *
 * <P>This object also knows how to mutate a Query object with a
 * proper criteria to filter based upon what is authorized for the
 * user this manager happens to deal with.
 *
 * <P>This is higher level then NCIASecurityMgr which deals
 * directly with CSM.  The behavior in this object seems
 * largely dependent upon the "security map" returned by
 * NCIASecurityMgr.
 */
public class AuthorizationManager {
	//these are CSM_PROTECTION_ELEMENT.DESCRIPTION values??????????
    private static final String PROT_ELEM_TRIAL_DATA_PROVENANCE = "TRIAL_DATA_PROVENANCE";
    private static final String PROT_ELEM_GENERAL_SERIES = "GENERAL_SERIES";

    //these are CSM_PROTECTION_ELEMENT.ATTRIBUTE values???????
    private static final String PROT_ELEM_PROJECT = "PROJECT";
    private static final String PROT_ELEM_SECURITY_GROUP = "SECURITY_GROUP";
    private static final String PROT_ELEM_SITE = "DP_SITE_NAME";


    // The user's security rights
    private Set<TableProtectionElement> securityRights;

    /**
     * Constructor - initializes this instance of AuthorizationManager for the
     * user
     *
     * @param userName
     * @throws Exception
     */
    public AuthorizationManager(String userName) throws Exception {
		NCIASecurityManager mgr = (NCIASecurityManager)SpringApplicationContext.getBean("nciaSecurityManager");
        String userId = mgr.getUserId(userName);
        securityRights = mgr.getSecurityMap(userId);
    }

    /**
     * Constructor - initializes this instance of AuthorizationManager for the
     * user
     *
     * @param userId
     * @throws Exception
     */
    public AuthorizationManager(Long userId) throws Exception {
		NCIASecurityManager mgr = (NCIASecurityManager)SpringApplicationContext.getBean("nciaSecurityManager");
        securityRights = mgr.getSecurityMap(String.valueOf(userId));
    }

    /**
     * Constructor - initializes this instance of AuthorizationManager with no
     * user name provided.  This constructor should only be used
     * authorizing against "public" data.
     *
     * @throws Exception
     */
    public AuthorizationManager() throws Exception {
		NCIASecurityManager mgr = (NCIASecurityManager)SpringApplicationContext.getBean("nciaSecurityManager");
        securityRights = mgr.getSecurityMapForPublicRole();
    }

    /**
     * Returns a list of collection names that the user is allowed to view.
     */
    public List<String> getAuthorizedCollections() {
        return getAuthorizedCollections(RoleType.READ);
    }

    /**
     * This method will get the authorized collections for a specific role
     */
    public List<String> getAuthorizedCollections(RoleType role) {
        return findValuesFromAuthorizationData(PROT_ELEM_TRIAL_DATA_PROVENANCE,
                                               PROT_ELEM_PROJECT,
                                               role);
    }

    /**
     * Returns a list of series security groups that the user is allowed to
     * view.
     */
    public List<String> getAuthorizedSeriesSecurityGroups() {
        return getAuthorizedSeriesSecurityGroups(RoleType.READ);
    }

    /**
     * This method will get the authorized series security groups for a specific
     * role
     */
    public List<String> getAuthorizedSeriesSecurityGroups(RoleType role) {
        return findValuesFromAuthorizationData(PROT_ELEM_GENERAL_SERIES,
                                               PROT_ELEM_SECURITY_GROUP,
                                               role);
    }

    /**
     * Returns a list of sites that the user is allowed to view.
     */
    public List<SiteData> getAuthorizedSites() {
        return getAuthorizedSites(RoleType.READ);
    }

    /**
     * Returns a list of sites a user can view based upon a certain role
     */
    public List<SiteData> getAuthorizedSites(RoleType role) {
        List<SiteData> returnList = new ArrayList<SiteData>();

        // Use prefix
        for (String comboString : findValuesFromAuthorizationData(
                PROT_ELEM_TRIAL_DATA_PROVENANCE,
                PROT_ELEM_PROJECT + SiteData.SITE_DELIMITER + PROT_ELEM_SITE,
                role)) {
            returnList.add(new SiteData(comboString));
        }

        return returnList;
    }

    /**
     * Adds the required criteria for authorization to the query for sites and
     * series security groups
     *
     * @param query
     */
    public void authorizeSitesAndSSGs(DICOMQuery query) {
        AuthorizationCriteria auth = query.getAuthorizationCriteria();

        if (auth == null) {
            // Add authorization criteria if it does not
            // already exist
            auth = new AuthorizationCriteria();
            query.setCriteria(auth);
        }

        auth.setSeriesSecurityGroups(getAuthorizedSeriesSecurityGroups());
        auth.setSites(getAuthorizedSites());
    }

    /**
     * Adds the required criteria for authorization to the query for
     * collections
     *
     * @param query
     * @return returns false if user attempted to view data with unauthorized
     *         collections
     */
    public boolean authorizeCollections(DICOMQuery query) {
        boolean returnValue = true;
        AuthorizationCriteria auth = query.getAuthorizationCriteria();

        if (auth == null) {
            // Add authorization criteria if it does not
            // already exist
            auth = new AuthorizationCriteria();
            query.setCriteria(auth);
        }

        CollectionCriteria cc = query.getCollectionCriteria();

        if (cc == null) {
            // User didn't choose any collections, so filter by the collections
            // they are authorized to see
            auth.setCollections(getAuthorizedCollections());
        } else {
            List<String> authorizedCollections = getAuthorizedCollections();
            List<String> collectionsToRemove = new ArrayList<String>();

            // Look at each collection to ensure that user is allowed to view it
            for (String selectedCollection : cc.getCollectionObjects()) {
                if (!authorizedCollections.contains(selectedCollection)) {
                    returnValue = false;
                    collectionsToRemove.add(selectedCollection);
                }
            }

            for (String collectionToRemove : collectionsToRemove) {
                cc.removeCollection(collectionToRemove);
            }
        }

        return returnValue;
    }

    /**
     * Determines whether or not a user has a specified role
     */
    public boolean hasRole(RoleType role) {
        for (TableProtectionElement tpe : securityRights) {
            if (tpe.hasRole(role)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Finds values in authorization data that meet certain criteria.
     *
     * <p>This returns the name of Protection elements like: NCIA.Phantom//WashU-IRL
     *
     * @param tableName - the 'table name' to look for in authorizatoin data
     * @param columnName - the 'column name' to look for in authorizatoin data
     */
    private List<String> findValuesFromAuthorizationData(String tableName,
                                                         String columnName,
                                                         RoleType role) {

        tableName = NCIAConfig.getProtectionElementPrefix() + tableName;
        columnName = NCIAConfig.getProtectionElementPrefix() + columnName;

        List<String> returnValues = new ArrayList<String>();

        for (TableProtectionElement tpe : securityRights) {
            if (tableName.equals(tpe.getTableName()) &&
                columnName.equals(tpe.getAttributeName()) &&
                tpe.hasRole(role)) {

                String value = tpe.getAttributeValue();

                if (value != null) {
                    // Strip off the prefix
                    returnValues.add(value.replace(
                            NCIAConfig.getProtectionElementPrefix(), ""));
                }
            }
        }

        return returnValues;
    }
}
