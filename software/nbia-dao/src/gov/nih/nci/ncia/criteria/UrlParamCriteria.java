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
* Revision 1.3  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
/*
 * Created on Jul 24, 2005
 *
 *
 *
 */
package gov.nih.nci.ncia.criteria;

import java.io.Serializable;


public class UrlParamCriteria extends TransientCriteria implements Serializable {
    private static final long serialVersionUID = 1L;
    private String patientId;
    private String studyInstanceUid;
    private String seriesInstanceUid;
    private String imageSopInstanceUid;

    //Constructor
    public UrlParamCriteria() {
        patientId = null;
        studyInstanceUid = null;
        seriesInstanceUid = null;
        imageSopInstanceUid = null;
    }

    /**
     * 
     * NOTE: the criteria handler doesnt appear to use this - used
     * "higher up"???
     * 
     * @return Returns the imageSopInstanceUid.
     */
    public String getImageSopInstanceUid() {
        return imageSopInstanceUid;
    }

    /**
     * NOTE: the criteria handler doesnt appear to use this - used
     * "higher up"???
     * 
     * @param imageSopInstanceUid The imageSopInstanceUid to set.
     */
    public void setImageSopInstanceUid(String imageSopInstanceUid) {
        this.imageSopInstanceUid = imageSopInstanceUid;
    }

    /**
     * @return Returns the patientId.
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * @param patientId The patientId to set.
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * @return Returns the seriesInstanceUid.
     */
    public String getSeriesInstanceUid() {
        return seriesInstanceUid;
    }

    /**
     * @param seriesInstanceUid The seriesInstanceUid to set.
     */
    public void setSeriesInstanceUid(String seriesInstanceUid) {
        this.seriesInstanceUid = seriesInstanceUid;
    }

    /**
     * @return Returns the studyInstanceUid.
     */
    public String getStudyInstanceUid() {
        return studyInstanceUid;
    }

    /**
     * @param studyInstanceUid The studyInstanceUid to set.
     */
    public void setStudyInstanceUid(String studyInstanceUid) {
        this.studyInstanceUid = studyInstanceUid;
    }

    public boolean isEmpty() {
        return ((getImageSopInstanceUid() == null) || (getPatientId() == null) ||
        (getSeriesInstanceUid() == null) || (getStudyInstanceUid() == null));
    }
}
