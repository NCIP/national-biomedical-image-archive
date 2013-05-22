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
* Revision 1.1  2007/08/05 21:52:15  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:48:51  bauerd
* *** empty log message ***
*
* Revision 1.10  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
/*
 * Created on Aug 3, 2005
 *
 *
 *
 */
package gov.nih.nci.nbia.search;


/**
 * Returned from DICOMQueryHandler.  Represents one series for a patient.
 * @author shinohaa
 */
public class PatientStudySeriesTriple {
    private int seriesPkId;
    private int studyPkId;
    private int patientPkId;

    public PatientStudySeriesTriple() {
    }


    /**
     * @return Returns the patientPkId.
     */
    public int getPatientPkId() {
        return patientPkId;
    }

    /**
     * @param patientPkId The patientPkId to set.
     */
    public void setPatientPkId(int patientPkId) {
        this.patientPkId = patientPkId;
    }

    public int getSeriesPkId() {
        return seriesPkId;
    }

    public void setSeriesPkId(int seriesPkId) {
        this.seriesPkId = seriesPkId;
    }

    public Integer getStudyPkId() {
        return studyPkId;
    }

    public void setStudyPkId(int studyPkId) {
        this.studyPkId = studyPkId;
    }
}
