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
* Revision 1.1  2007/08/07 12:05:15  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:38  bauerd
* Initial Check in of reorganized components
*
* Revision 1.4  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.ncia.criteria;

import gov.nih.nci.nbia.util.SiteData;

import java.util.ArrayList;
import java.util.List;


/**
 * Criteria used to restrict access based on a user's security rights
 *
 */
public class AuthorizationCriteria extends TransientCriteria {
    // List of sites that the user is allowed to view
    List<SiteData> sites;
    // List of series security groups that the user is allowed to view
    List<String> seriesSecurityGroups;
    // List of collections they are allowed to see
    // This is null if the user specified collections on the search page
    List<String> collections;

    /**
     * Returns true if sites and SSGs are not included
     *
     */
    public boolean isEmpty() {
        return (sites == null) || (seriesSecurityGroups == null);
    }

    /**
     *
     * @param siteData
     */
    public void setSites(List<SiteData> siteData) {
        sites = siteData;
    }

    /**
     *
     * @param ssgs
     */
    public void setSeriesSecurityGroups(List<String> ssgs) {
        seriesSecurityGroups = new ArrayList<String>(ssgs);
    }

    /**
     *
     * @param collections
     */
    public void setCollections(List<String> collections) {
        this.collections = new ArrayList<String>(collections);
    }

    /**
     *
     */
    public List<SiteData> getSites() {
        return sites;
    }

    /**
     *
     */
    public List<String> getCollections() {
        return collections;
    }

    public List<String> getSeriesSecurityGroups() {
        return seriesSecurityGroups;
    }
}
