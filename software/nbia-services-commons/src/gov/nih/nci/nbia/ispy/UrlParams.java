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
* Revision 1.1  2007/08/07 12:05:11  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:38  bauerd
* Initial Check in of reorganized components
*
* Revision 1.2  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.ispy;

import gov.nih.nci.nbia.util.NameValueObj;

import java.util.List;


public class UrlParams {
    private String source;
    private String image1Label;
    private String image1TrialId;
    private String image1PatientId;
    private String image1StudyInstanceUid;
    private String image1SeriesInstanceUid;
    private String image1ImageSopInstanceUid;
    private List<NameValueObj> image1dataNameValues;
    private String image2Label;
    private String image2TrialId;
    private String image2PatientId;
    private String image2StudyInstanceUid;
    private String image2SeriesInstanceUid;
    private String image2ImageSopInstanceUid;
    private List<NameValueObj> image2dataNamesValues;

    public UrlParams() {
        super();
    }

    /**
     * @return Returns the image1ImageSopInstanceUid.
     */
    public String getImage1ImageSopInstanceUid() {
        return image1ImageSopInstanceUid;
    }

    /**
     * @param image1ImageSopInstanceUid The image1ImageSopInstanceUid to set.
     */
    public void setImage1ImageSopInstanceUid(String image1ImageSopInstanceUid) {
        this.image1ImageSopInstanceUid = image1ImageSopInstanceUid;
    }

    /**
     * @return Returns the image1Label.
     */
    public String getImage1Label() {
        return image1Label;
    }

    /**
     * @param image1Label The image1Label to set.
     */
    public void setImage1Label(String image1Label) {
        this.image1Label = image1Label;
    }

    /**
     * @return Returns the image1PatientId.
     */
    public String getImage1PatientId() {
        return image1PatientId;
    }

    /**
     * @param image1PatientId The image1PatientId to set.
     */
    public void setImage1PatientId(String image1PatientId) {
        this.image1PatientId = image1PatientId;
    }

    /**
     * @return Returns the image1SeriesInstanceUid.
     */
    public String getImage1SeriesInstanceUid() {
        return image1SeriesInstanceUid;
    }

    /**
     * @param image1SeriesInstanceUid The image1SeriesInstanceUid to set.
     */
    public void setImage1SeriesInstanceUid(String image1SeriesInstanceUid) {
        this.image1SeriesInstanceUid = image1SeriesInstanceUid;
    }

    /**
     * @return Returns the image1StudyInstanceUid.
     */
    public String getImage1StudyInstanceUid() {
        return image1StudyInstanceUid;
    }

    /**
     * @param image1StudyInstanceUid The image1StudyInstanceUid to set.
     */
    public void setImage1StudyInstanceUid(String image1StudyInstanceUid) {
        this.image1StudyInstanceUid = image1StudyInstanceUid;
    }

    /**
     * @return Returns the image1TrialId.
     */
    public String getImage1TrialId() {
        return image1TrialId;
    }

    /**
     * @param image1TrialId The image1TrialId to set.
     */
    public void setImage1TrialId(String image1TrialId) {
        this.image1TrialId = image1TrialId;
    }

    /**
     * @return Returns the image2ImageSopInstanceUid.
     */
    public String getImage2ImageSopInstanceUid() {
        return image2ImageSopInstanceUid;
    }

    /**
     * @param image2ImageSopInstanceUid The image2ImageSopInstanceUid to set.
     */
    public void setImage2ImageSopInstanceUid(String image2ImageSopInstanceUid) {
        this.image2ImageSopInstanceUid = image2ImageSopInstanceUid;
    }

    /**
     * @return Returns the image2Label.
     */
    public String getImage2Label() {
        return image2Label;
    }

    /**
     * @param image2Label The image2Label to set.
     */
    public void setImage2Label(String image2Label) {
        this.image2Label = image2Label;
    }

    /**
     * @return Returns the image2PatientId.
     */
    public String getImage2PatientId() {
        return image2PatientId;
    }

    /**
     * @param image2PatientId The image2PatientId to set.
     */
    public void setImage2PatientId(String image2PatientId) {
        this.image2PatientId = image2PatientId;
    }

    /**
     * @return Returns the image2SeriesInstanceUid.
     */
    public String getImage2SeriesInstanceUid() {
        return image2SeriesInstanceUid;
    }

    /**
     * @param image2SeriesInstanceUid The image2SeriesInstanceUid to set.
     */
    public void setImage2SeriesInstanceUid(String image2SeriesInstanceUid) {
        this.image2SeriesInstanceUid = image2SeriesInstanceUid;
    }

    /**
     * @return Returns the image2StudyInstanceUid.
     */
    public String getImage2StudyInstanceUid() {
        return image2StudyInstanceUid;
    }

    /**
     * @param image2StudyInstanceUid The image2StudyInstanceUid to set.
     */
    public void setImage2StudyInstanceUid(String image2StudyInstanceUid) {
        this.image2StudyInstanceUid = image2StudyInstanceUid;
    }

    /**
     * @return Returns the image2TrialId.
     */
    public String getImage2TrialId() {
        return image2TrialId;
    }

    /**
     * @param image2TrialId The image2TrialId to set.
     */
    public void setImage2TrialId(String image2TrialId) {
        this.image2TrialId = image2TrialId;
    }

    /**
     * @return Returns the source.
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source The source to set.
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return Returns the image1dataNameValues.
     */
    public List<NameValueObj> getImage1dataNameValues() {
        return image1dataNameValues;
    }

    /**
     * @param image1dataNameValues The image1dataNameValues to set.
     */
    public void setImage1dataNameValues(List<NameValueObj> image1dataNameValues) {
        this.image1dataNameValues = image1dataNameValues;
    }

    /**
     * @return Returns the image2dataNamesValues.
     */
    public List<NameValueObj> getImage2dataNamesValues() {
        return image2dataNamesValues;
    }

    /**
     * @param image2dataNamesValues The image2dataNamesValues to set.
     */
    public void setImage2dataNamesValues(List<NameValueObj> image2dataNamesValues) {
        this.image2dataNamesValues = image2dataNamesValues;
    }
}
