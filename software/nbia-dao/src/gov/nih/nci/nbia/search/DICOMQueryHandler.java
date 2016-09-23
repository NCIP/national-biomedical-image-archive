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
 * Revision 1.8  2007/12/06 15:33:38  lethai
 * Tracker id 8478 - Modality AND search
 * Tracker id 7129 - Anatomical Site
 *
 * Revision 1.7  2007/11/16 19:32:56  lethai
 * Rollback changes to Tracker id 10169
 *
 * Revision 1.3  2007/10/01 12:22:10  bauerd
 * *** empty log message ***
 *
 * Revision 1.2  2007/08/24 15:13:33  bauerd
 * *** empty log message ***
 *
 * Revision 1.1  2007/08/07 12:05:16  bauerd
 * *** empty log message ***
 *
 * Revision 1.1  2007/08/05 21:44:39  bauerd
 * Initial Check in of reorganized components
 *
 * Revision 1.52  2007/01/06 06:36:29  zhouro
 * added new search criteria AcquisitionMatrix, ReconstructionDiameter, DataCollectionDiameter(CT) and DataCollectionDiameter(DX)
 *
 * Revision 1.51  2006/12/13 14:04:14  dietrich
 * Grid enhancement
 *
 * Revision 1.50  2006/12/06 22:18:13  zhouro
 * enhancement on annotation option
 *
 * Revision 1.49  2006/10/19 20:32:56  dietrich
 * Changed to OR numeric and string curation data together instead of ANDing them
 * Revision 1.48 2006/10/10 18:48:32 shinohaa
 * 2.1 enhancements
 *
 * Revision 1.47 2006/09/27 20:46:27 panq Reformated with Sun Java Code Style
 * and added a header for holding CVS history.
 *
 */
/**
 * @copyright
 */
package gov.nih.nci.nbia.search;

import gov.nih.nci.nbia.query.DICOMQuery;

import java.util.List;
import org.springframework.dao.DataAccessException;

/**
 * Process a provided query in to an HQL statement and pass it to Hibernate
 *
 * @author NCIA Team
 */
public interface DICOMQueryHandler {
    public static final String MANUFACTURER_FIELD = "ge.manufacturer ";
    public static final String MODEL_FIELD = "ge.manufacturerModelName ";
    public static final String SOFTWARE_VERSION_FIELD = "ge.softwareVersions ";
    public static final String PATIENT_ID = "p.patientId ";
    public static final String STUDY_INSTANCE_UID = "study.studyInstanceUID ";
    public static final String SERIES_INSTANCE_UID = "series.seriesInstanceUID ";
    public static final String IMAGE_INSTANCE_UID = "gi.SOPInstanceUID ";
    public static final String AND = "AND ";
    public static final String OR = "OR ";
    public static final String OPEN_PARENTHESIS = " ( ";
    public static final String CLOSE_PARENTHESIS = " ) ";
    public static final String PERCENTAGE = " % ";
    public static final String IN = " IN ";
    public static final String STUDY_NUMBER = "sn.studyNumber > ";
    public static final String SERIES_ANNOTATIONS_FLAG = "series.annotationsFlag = 1";
    public static final String SERIES_ANNOTATIONS_FLAG_TRUE = "series.annotationsFlag = '1' ";
    public static final String SERIES_ANNOTATIONS_FLAG_FALSE = "series.annotationsFlag is null ";
    public static final String SERIES_DESC = "and lower(series.seriesDesc) like '%'||lower('";
    public static final String SERIES_GENERAL_EQUIPMENT = " join series.generalEquipment ge ";
    public static final String SERIES_SECURITY_GROUP_NULL = " and (series.securityGroup is null ";
    public static final String SERIES_SECURITY_GROUP_IN = " or series.securityGroup in ";
    public static final String GI_CONSTRAST_BOLUS_AGENT_NOT_NULL = " and gi.contrastBolusAgent is not null ";
    public static final String GI_CONSTRAST_BOLUS_AGENT_NULL = " and gi.contrastBolusAgent is null ";
    public static final String GI_MULTI_FRAME = " and gi.usFrameNum > 1 ";
    public static final String GI_SINGLE_FRAME = " and gi.usFrameNum <= 1 ";
    public static final String GI_BMODE = " and gi.usColorDataPresent = 'False' ";
    public static final String GI_COLOR_MODE = " and gi.usColorDataPresent = 'True' ";
    public static final String GI_MULTI_MODALITY = " and gi.usMultiModality == ";
    public static final String CURATION_TIMESTAMP = " and gi.curationTimestamp > ";
    public static final String JOIN_CTIMAGE = " join  gi.ctimage ci ";
    public static final String CONVOLUTION_KERNEL = " and ci.convolutionKernel in ";
    public static final String SERIES_ID_IN = " series.id in (";

    /**
     * This will process a provided query in to an HQL statement and pass it to
     * Hibernate.
     *
     * <p>I think it returns a PatientResultSet per series that mataches
     * the specified criteria (so not necessarily one object returned per patient)
     *
     * <p>Beware that this thing sits on ALL exceptions even though
     * it declares that it throws exceptions.... a little redesign is in
     * order wrt exception handling  A failure will look the same as
     * no results found....
     */
    public List<PatientStudySeriesTriple> findTriples(DICOMQuery theQuery) throws DataAccessException;
}
