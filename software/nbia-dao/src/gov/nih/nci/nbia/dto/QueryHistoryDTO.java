/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id: QueryHistoryDTO.java 4417 2008-04-18 20:43:12Z saksass $
*
* $Log: not supported by cvs2svn $
* Revision 1.3  2007/09/27 23:30:39  bauerd
* This is the checked in seperation of dependent classes betweeen the grid and the commons module...
*
* Revision 1.1  2007/08/05 21:52:15  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:48:51  bauerd
* *** empty log message ***
*
* Revision 1.7  2006/09/27 20:46:28  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.dto;

import gov.nih.nci.ncia.criteria.Criteria;
import gov.nih.nci.nbia.util.ResourceBundleUtil;

import java.util.ArrayList;


/**
 * This object will hold the information neccessary to display a query history
 * record on the front end.
 *
 *
 */
public class QueryHistoryDTO extends AbstractStoredQueryDTO {
    // True if the associated saved query is inactive
    private boolean savedQueryIsInactive;

    // Id of the associated saved query
    long savedQueryId;

    /**
     * Constructor
     *
     */
    public QueryHistoryDTO() {
        criteriaList = new ArrayList<Criteria>();
    }

    /**
     * Returns true if the saved query associated with this query
     * history is inactive.
     */
    public boolean isSavedQueryIsInactive() {
        return savedQueryIsInactive;
    }

    /**
     * Set the saved query inactive flag
     *
     * @param savedQueryIsInactive
     */
    public void setSavedQueryIsInactive(boolean savedQueryIsInactive) {
        this.savedQueryIsInactive = savedQueryIsInactive;
    }

    /**
     * Get query name for display
     */
    public String getQueryName() {
        if ((queryName == null) || queryName.equals("")) {
            // If query was not a saved query, put some default text
            return ResourceBundleUtil.getString("noNameProvided");
        } else {
            // Include an asterisk if query is inactive
            return queryName + (savedQueryIsInactive ? " (*)" : "");
        }
    }

    public long getSavedQueryId() {
        return savedQueryId;
    }

    public void setSavedQueryId(long savedQueryId) {
        this.savedQueryId = savedQueryId;
    }
}
