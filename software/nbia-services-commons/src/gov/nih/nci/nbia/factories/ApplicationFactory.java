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
* Revision 1.5  2007/10/10 19:57:35  bauerd
* *** empty log message ***
*
* Revision 1.4  2007/08/15 20:02:32  bauerd
* *** empty log message ***
*
* Revision 1.3  2007/08/15 12:22:05  bauerd
* *** empty log message ***
*
* Revision 1.2  2007/08/07 12:05:16  bauerd
* *** empty log message ***
*
* Revision 1.11  2007/01/11 22:44:28  dietrich
* Defect 174
*
* Revision 1.10  2006/11/27 22:10:04  panq
* Modified for getting thumbnails from the grid.
*
* Revision 1.9  2006/11/27 18:10:13  shinohaa
* grid functionality
*
* Revision 1.8  2006/11/21 16:36:23  shinohaa
* data basket grid functionality
*
* Revision 1.7  2006/11/15 15:40:19  shinohaa
* commenting code
*
* Revision 1.6  2006/11/10 13:58:26  shinohaa
* grid prototype
*
* Revision 1.5  2006/09/27 20:46:28  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
/*
 * Created on Jun 15, 2005
 *
 *
 *
 */
package gov.nih.nci.nbia.factories;

import gov.nih.nci.nbia.lookup.ImageRegisterMap;
import gov.nih.nci.nbia.lookup.StudyNumberMap;

import java.util.Date;


/**
 * This class contains universal references to items that the entire application
 * needs
 *
 * @author shinohaa
 *
 */
public class ApplicationFactory {

    private static ApplicationFactory singleInstance;
    private StudyNumberMap studyNumberMap;
    private ImageRegisterMap imageRegisterMap;

    // The latest curation date found during the last daily check
    private Date latestCurationDate;

    static {
        singleInstance = new ApplicationFactory();
    }

    private ApplicationFactory() {
    }

    /**
     * Returns the single instance of the ApplicationFactory
     **/
    public static ApplicationFactory getInstance() {
        return singleInstance;
    }


    public StudyNumberMap getStudyNumberMap() {
        if(studyNumberMap == null) {
            studyNumberMap = new StudyNumberMap();
        }
        return studyNumberMap;
    }

    public ImageRegisterMap getImageRegisterMap() {
        if(imageRegisterMap == null) {
            imageRegisterMap = new ImageRegisterMap();
        }
        return imageRegisterMap;
    }


    /**
     * Returns the latest curation date
     *
     * @return latest curation date
     */
    public Date getLatestCurationDate() {
        return latestCurationDate;
    }

    /**
     * Sets the latest curation date
     *
     * @param latestCurationDate
     */
    public void setLatestCurationDate(Date latestCurationDate) {
        this.latestCurationDate = latestCurationDate;
    }
}
