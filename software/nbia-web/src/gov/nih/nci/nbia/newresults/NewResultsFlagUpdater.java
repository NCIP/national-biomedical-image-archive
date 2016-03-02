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
* Revision 1.1  2007/08/05 21:06:53  bauerd
* Initial Check in of reorganized components
*
* Revision 1.7  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.newresults;

import java.util.Date;
import java.util.HashMap;


/**
 * This object will sit in a users session so that they will be able to access
 * all new data for a query during the course of their session.
 *
 * @author shinohaa
 *
 */
public class NewResultsFlagUpdater {

    /**
     * Holds the query ID's and the last time they were run
     */
    private HashMap<Long, Date> queryExecutionTimes;

    public NewResultsFlagUpdater() {
        queryExecutionTimes = new HashMap<Long, Date>();
    }

    /**
     * Adds an execution time for the saved query to be run
     *
     * @param savedQueryId
     * @param executeDate
     */
    public void addSavedQuery(long savedQueryId, Date executeDate) {
        queryExecutionTimes.put(savedQueryId, executeDate);
    }

    /**
     * Gets the last executed time that has been placed in the session.
     * Returns null if it does not exist.
     *
     * @param savedQueryId
     */
    public Date getLastExecuteTimeBeforeThisSession(long savedQueryId) {
        return queryExecutionTimes.get(savedQueryId);
    }
}
