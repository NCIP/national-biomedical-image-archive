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
* Revision 1.1  2007/08/05 21:44:39  bauerd
* Initial Check in of reorganized components
*
* Revision 1.12  2006/09/27 20:46:28  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
/*
 * Created on Jul 27, 2005
 *
 *
 *
 */
package gov.nih.nci.nbia.query;

import java.io.Serializable;
import java.util.Date;


/**
 * @author Prashant Shah - NCICB/SAIC
 *
 */
public abstract class Query implements Serializable {
    private String userID;
    private Date executeTime;


    /**
     * The user that is executing the query
     *
     * @return userID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Setter for the userID
     *
     * @param userID the user that is executing the query
     */
    public void setUserID(String userID) {
        if (userID != null) {
            this.userID = userID;
        }
    }

    /**
     * @return the last execute time for the query
     *
     */
    public Date getExecuteTime() {
        return executeTime;
    }

    /**
     * Sets the last execute time for this query
     *
     * @param executeTime the last execute time
     */
    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }
}
