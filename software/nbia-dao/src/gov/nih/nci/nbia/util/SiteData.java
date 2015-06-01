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
* Revision 1.1  2007/08/05 21:44:38  bauerd
* Initial Check in of reorganized components
*
* Revision 1.4  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.util;

import java.io.Serializable;


/**
 * Represents a site used in authorization criteria
 * It is the combination of a project name and site name
 * to ensure uniqueness
 *
 */
public class SiteData implements Serializable {
	/**
	 * The delimiter used when representing site data as one string
	 **/
	public static final String SITE_DELIMITER = "//";


    /**
     *
     */
    private static final long serialVersionUID = 1L;

    // The data
    private String collection;
    private String siteName;

    /**
     * Parses a string containing both site and project
     * into two fields.
     *
     * @param collectionSiteCombination
     */
    public SiteData(String collectionSiteCombination) {
        // Use the delimiter to split
        int index = collectionSiteCombination.indexOf(SITE_DELIMITER);
        collection = collectionSiteCombination.substring(0, index);
        siteName = collectionSiteCombination.substring(index + SITE_DELIMITER.length(),
                                                       collectionSiteCombination.length());
    }

    /**
     * Getter for collection
     */
    public String getCollection() {
        return collection;
    }

    /**
     * Getter for site
     */
    public String getSiteName() {
        return siteName;
    }
}
