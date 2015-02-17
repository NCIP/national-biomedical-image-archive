/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id: SavedQueryLastExec.java 4417 2008-04-18 20:43:12Z saksass $
*
* $Log: not supported by cvs2svn $
* Revision 1.1  2007/08/05 21:44:38  bauerd
* Initial Check in of reorganized components
*
* Revision 1.2  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
/**
 *
 * copyright
 *
 */
package gov.nih.nci.nbia.internaldomain;

import java.io.Serializable;

import java.util.Date;


/**
 * Domain class to represent a saved query last exec view
 *
 * @author NCIA Team
 */
public class SavedQueryLastExec implements Serializable {
    private static final long serialVersionUID = 4270899458894752407L;

    // primary key of the saved query
    private long savedQueryId;

    // the maximum last execution date for the query
    private Date lastExecuteDate;

    /**
     *
     */
    public Date getLastExecuteDate() {
        return lastExecuteDate;
    }

    /**
     *
     * @param lastExecuteDate
     */
    public void setLastExecuteDate(Date lastExecuteDate) {
        this.lastExecuteDate = lastExecuteDate;
    }

    /**
     *
     */
    public long getSavedQueryId() {
        return savedQueryId;
    }

    /**
     *
     * @param savedQueryId
     */
    public void setSavedQueryId(long savedQueryId) {
        this.savedQueryId = savedQueryId;
    }
}
