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
* Revision 1.21  2006/11/27 18:06:36  shinohaa
* extracted constant
*
* Revision 1.20  2006/11/21 16:36:14  shinohaa
* data basket grid functionality
*
* Revision 1.19  2006/11/15 15:40:06  shinohaa
* grid prototype
*
* Revision 1.18  2006/11/10 13:58:15  shinohaa
* grid prototype
*
* Revision 1.17  2006/09/27 20:46:28  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.dto;

import java.io.Serializable;


/**
 * Represents a series for data transfer purposes
 *
 * @author dietrichj
 *
 */
public class MarkupDTO implements Serializable {
    private String seriesUID;
    private String usrLoginName;
    private String markupData;
    /**
     * @return Returns the markupData.
     */
    public String getMarkupData() {
        return markupData;
    }
    /**
     * @param markupData The markupData to set.
     */
    public void setMarkupData(String markupData) {
        this.markupData = markupData;
    }
    /**
     * @return Returns the seriesUID.
     */
    public String getSeriesUID() {
        return seriesUID;
    }
    /**
     * @param seriesUID The seriesUID to set.
     */
    public void setSeriesUID(String seriesUID) {
        this.seriesUID = seriesUID;
    }
    /**
     * @return Returns the usrLoginName.
     */
    public String getUsrLoginName() {
        return usrLoginName;
    }
    /**
     * @param usrLoginName The usrLoginName to set.
     */
    public void setUsrLoginName(String usrLoginName) {
        this.usrLoginName = usrLoginName;
    }

    
}
