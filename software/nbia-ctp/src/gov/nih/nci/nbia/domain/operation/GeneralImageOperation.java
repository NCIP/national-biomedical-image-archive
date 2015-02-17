/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
 *
 */
package gov.nih.nci.nbia.domain.operation;

import gov.nih.nci.nbia.internaldomain.GeneralImage;
import gov.nih.nci.nbia.internaldomain.GeneralSeries;
import gov.nih.nci.nbia.internaldomain.Patient;
import gov.nih.nci.nbia.internaldomain.Study;
import gov.nih.nci.nbia.internaldomain.TrialDataProvenance;
import gov.nih.nci.nbia.util.AdapterUtil;
import gov.nih.nci.nbia.util.DicomConstants;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.nbia.util.UltrasoundUtil;

import java.util.Date;
import java.util.Map;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
public class GeneralImageOperation extends DomainOperation implements GeneralImageOperationInterface {
    private static Logger log = Logger.getLogger(GeneralImageOperation.class);

    private TrialDataProvenance dataProvenance;
    private Patient patient;
    private Study study;
    private GeneralSeries series;
   // private boolean visibility;
    private boolean replacement = false;

    private static final String SOP_INSTANCE_UID_LPAREN = "SOP Instance UID (";
    private static final String IN_THE_SUBMITTED_IMAGE = " in the submitted image.";
    private static final String IN_DATABASE_AND = " in database and ";

    public GeneralImageOperation() {
    }


    @Transactional(propagation=Propagation.REQUIRED)
    public Object validate(Map numbers) throws Exception {
    	replacement = false;
        GeneralImage gi = (GeneralImage)SpringApplicationContext.getBean("generalImage");
        try {
            String sopInstanceUid = getTrimmedSopInstanceUid(numbers);
            //we know sopInstanceUid is not null and trimmed if we survive to this point
            gi.setSOPInstanceUID(sopInstanceUid);

            List ret = queryForExistingImage(series.getId(),sopInstanceUid);

            if(ret !=null && ret.size() > 0) {
                gi = (GeneralImage) ret.get(0);
                validateImageReplacement(numbers, gi, sopInstanceUid, series);
                replacement = true;
                //db constraints will make 1 the max
            }
            //else it doesnt exist so create new one


            gi.setGeneralSeries(series);
            gi.setDataProvenance(dataProvenance);
            gi.setPatient(patient);
            gi.setStudy(study);

            populateGeneralImageFromNumbers(numbers, gi);

            gi.setSubmissionDate(new Date());
            gi.setPatientId(patient.getPatientId());
            gi.setStudyInstanceUID(study.getStudyInstanceUID());
            gi.setSeriesInstanceUID(series.getSeriesInstanceUID());
            gi.setProject(dataProvenance.getProject());
            //Since visibility is forced to set to "0" (not yet reviewed)
            //There is no reason to set curationTimestamp here
//            if (this.visibility) {
//                gi.setVisibility("1");
//                gi.setCurationTimestamp(new java.util.Date());
//            }
//            else
//            {
//                gi.setVisibility("0");
//            }
        }
        catch(Exception e) {
            log.error("Exception in GeneralImageOperation " + e);
            throw new Exception("Exception in GeneralImageOperation " + e);
        }


        return gi;
    }
    /**
     * @param dataProvenance the dataProvenance to set
     */
    public void setDataProvenance(TrialDataProvenance dataProvenance) {
        this.dataProvenance = dataProvenance;
    }
    /**
     * @param patient the patient to set
     */
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    /**
     * @param series the series to set
     */
    public void setSeries(GeneralSeries series) {
        this.series = series;
    }
    /**
     * @param study the study to set
     */
    public void setStudy(Study study) {
        this.study = study;
    }
    /**
     * @param visibility the visibility to set
     */
//    public void setVisibility(boolean visibility) {
//        this.visibility = visibility;
//    }

    /**
     * This tells whether the image operation replaced an existing
     * image (aka update/correction/resubmission).  It is illegal
     * to call this before validate() has been invoked.  This will only
     * return true if the image was successfuly replaced... in other words
     * a replacement where the dicom tags are screwed up (and the image is
     * quarantined) will see this method return false.
     */
    public boolean isReplacement() {
//        if(!validateCalled) {
//            throw new RuntimeException("cant ask if replacement until validate invoked");
//        }
        return this.replacement;
    }

    /**
     * When an image is updated/resubmitted/replaced, certain fields between
     * the existing image and the new one need to match up, or else we
     * quarantine the new one.
     */
    private static void validateImageReplacement(Map numbers,
                                                 GeneralImage existingGeneralImage,
                                                 String sopInstanceUid,
                                                 GeneralSeries seriesForNewGeneralImage) throws Exception {
        //Make sure this record has same series, study, patient, project and site
        String thisPatient = (String)numbers.get(DicomConstants.PATIENT_ID);
        String thisProject = (String)numbers.get(DicomConstants.PROJECT_NAME);
        String thisStudyID = (String)numbers.get(DicomConstants.STUDY_INSTANCE_UID);
        String thisSeries = (String)numbers.get(DicomConstants.SERIES_INSTANCE_UID);
        String thisSiteID = (String)numbers.get(DicomConstants.SITE_ID);
        log.debug("SOP instance uid: " + sopInstanceUid + " The image PatientID: " + thisPatient + "\tstudyUID: "+ thisStudyID + "\tseriesUID: " + thisSeries + "\tProject: " +thisProject + "\tsiteID: " + thisSiteID );

        String patientID = existingGeneralImage.getPatientId();
        String project = existingGeneralImage.getDataProvenance().getProject();
        String studyID = existingGeneralImage.getStudyInstanceUID();
        String seriesID = existingGeneralImage.getSeriesInstanceUID();
        String siteID = existingGeneralImage.getDataProvenance().getDpSiteId();
        log.debug("PatientID: " + patientID + "\tstudyUID: "+ studyID + "\tseriesUID: " + seriesID + "\tProject: " +project + "\tsiteID: " + siteID );

        if(!patientID.equalsIgnoreCase(thisPatient) ) {
            log.error(SOP_INSTANCE_UID_LPAREN+ sopInstanceUid + ") has different patient information: " + patientID + IN_DATABASE_AND + thisPatient + IN_THE_SUBMITTED_IMAGE  );
            throw new Exception(SOP_INSTANCE_UID_LPAREN+ sopInstanceUid + ") has different patient information: " + patientID + IN_DATABASE_AND + thisPatient + IN_THE_SUBMITTED_IMAGE  );
        }
        if(!studyID.equalsIgnoreCase(thisStudyID) ) {
            log.error(SOP_INSTANCE_UID_LPAREN+ sopInstanceUid + ") has different study information: " + studyID + IN_DATABASE_AND + thisStudyID + IN_THE_SUBMITTED_IMAGE );
            throw new Exception(SOP_INSTANCE_UID_LPAREN+ sopInstanceUid + ") has different study information: " + studyID + IN_DATABASE_AND + thisStudyID + IN_THE_SUBMITTED_IMAGE );
        }
        if(!seriesID.equalsIgnoreCase(thisSeries) ) {
            log.error(SOP_INSTANCE_UID_LPAREN+ sopInstanceUid + ") has different series information: " + seriesID + IN_DATABASE_AND + thisSeries + IN_THE_SUBMITTED_IMAGE );
            throw new Exception(SOP_INSTANCE_UID_LPAREN+ sopInstanceUid + ") has different series information: " + seriesID + IN_DATABASE_AND + thisSeries + IN_THE_SUBMITTED_IMAGE );
        }
        if(!project.equalsIgnoreCase(thisProject) ) {
            log.error(SOP_INSTANCE_UID_LPAREN+ sopInstanceUid + ") has different project/collection information: " + project + IN_DATABASE_AND + thisProject + IN_THE_SUBMITTED_IMAGE );
            throw new Exception(SOP_INSTANCE_UID_LPAREN+ sopInstanceUid + ") has different project/collection information: " + project + IN_DATABASE_AND + thisProject + IN_THE_SUBMITTED_IMAGE );
        }
        if(!siteID.equalsIgnoreCase(thisSiteID) ) {
            log.error(SOP_INSTANCE_UID_LPAREN+ sopInstanceUid + ") has different site information: " + siteID + IN_DATABASE_AND + thisSiteID + IN_THE_SUBMITTED_IMAGE );
            throw new Exception(SOP_INSTANCE_UID_LPAREN+ sopInstanceUid + ") has different site information: " + siteID + IN_DATABASE_AND + thisSiteID + IN_THE_SUBMITTED_IMAGE );
        }

        GeneralSeries theSeries = (GeneralSeries)existingGeneralImage.getGeneralSeries();
        log.debug("GeneralSeries from GeneralImage is " + theSeries);
        if(theSeries != null) {
            Integer series_pk_id = theSeries.getId();
            log.debug("series_pk_id : " + series_pk_id);

            if(series_pk_id.intValue() == seriesForNewGeneralImage.getId().intValue()) {
                log.debug("same series pk id " + series_pk_id);
            }else{
                log.error(SOP_INSTANCE_UID_LPAREN + sopInstanceUid + ") belongs to 2 different series pk id " + series_pk_id + " from query (database), " + seriesForNewGeneralImage.getId() + " from series in parameter (submitting image)");
                throw new Exception(SOP_INSTANCE_UID_LPAREN+ sopInstanceUid + ") has different series pk id");
            }
        }
        log.debug("PatientID: " + patientID + "\tstudyUID: "+ studyID + "\tseriesUID: " + seriesID + "\tProject: " +project + "\tsiteID: " + siteID );

    }

    /**
     * Given the "numbers" map with all the parsed out dicom tag values we
     * care about..... populate the general image object with these values.
     */
    private static void populateGeneralImageFromNumbers(Map numbers,
                                                        GeneralImage gi) throws Exception {
    	String temp;
        if ((temp = (String) numbers.get(DicomConstants.INSTANCE_NUMBER)) != null) {
            gi.setInstanceNumber(Integer.valueOf(temp.trim()));
        }
        if ((temp = (String) numbers.get(DicomConstants.CONTENT_DATE)) != null) {
            gi.setContentDate(AdapterUtil.stringToDate(temp.trim()));
        }

        if ((temp = (String) numbers.get(DicomConstants.CONTENT_TIME)) != null) {
            gi.setContentTime(temp.trim());
        }
        if ((temp = (String) numbers.get(DicomConstants.IMAGE_TYPE)) != null) {
        	String first = getFirstValueOfImageType(temp);
            gi.setImageType(first.trim());
            String multiModality = null;
            try{
            	String multi_modality = getMultiModalityEelement(temp);
            	multiModality = UltrasoundUtil.getMultiModalityByCode(multi_modality);
            }catch (Exception e){
            	log.warn("The image is not UTRASOUND image. Continue processing....");
            }
        	gi.setUsMultiModality(multiModality);
        }
        if ((temp = (String) numbers.get(DicomConstants.ACQUISITION_NUMBER)) != null) {
            gi.setAcquisitionNumber(Integer.valueOf(temp.trim()));
        }
        if ((temp = (String) numbers.get(DicomConstants.ACQUISITION_DATE)) != null) {
            gi.setAcquisitionDate(AdapterUtil.stringToDate(temp.trim()));
        }
        if ((temp = (String) numbers.get(DicomConstants.ACQUISITION_TIME)) != null) {
            gi.setAcquisitionTime(temp.trim());
        }
        if ((temp = (String) numbers.get(DicomConstants.ACQUISITION_DATETIME)) != null) {
            gi.setAcquisitionDatetime(temp.trim());
        }
        if ((temp = (String) numbers.get(DicomConstants.IMAGE_COMMENTS)) != null) {
            gi.setImageComments(temp.trim());
        }
        if ((temp = (String) numbers.get(DicomConstants.LOSSY_IMAGE_COMPRESSION)) != null) {
            gi.setLossyImageCompression(temp.trim());
        }
        if ((temp = (String) numbers.get(DicomConstants.PIXEL_SPACING)) != null) {
            gi.setPixelSpacing(Double.valueOf(temp.trim()));
        }
        if ((temp = (String) numbers.get(
                        DicomConstants.PATIENT_IMAGE_ORIENTATION)) != null) {
            gi.setImageOrientationPatient(temp.trim());
        }
        if ((temp = (String) numbers.get(DicomConstants.PATIENT_IMAGE_POSITION)) != null) {
            gi.setImagePositionPatient(temp.trim());
        }
        if ((temp = (String) numbers.get(DicomConstants.SLICE_THICKNESS)) != null) {
            gi.setSliceThickness(Double.valueOf(temp.trim()));
        }
        if ((temp = (String) numbers.get(DicomConstants.SLICE_LOCATION)) != null) {
            gi.setSliceLocation(Double.valueOf(temp.trim()));
        }
        if ((temp = (String) numbers.get(DicomConstants.ROWS)) != null) {
            gi.setRows(Integer.valueOf(temp.trim()));
        }
        if ((temp = (String) numbers.get(DicomConstants.COLUMNS)) != null) {
            gi.setColumns(Integer.valueOf(temp.trim()));
        }
        if ((temp = (String) numbers.get(DicomConstants.CONTRAST_BOLUS_AGENT)) != null) {
            gi.setContrastBolusAgent(temp.trim());
        }
        if ((temp = (String) numbers.get(DicomConstants.CONTRAST_BOLUS_ROUTE)) != null) {
            gi.setContrastBolusRoute(temp.trim());
        }
        if ((temp = (String) numbers.get(DicomConstants.SOP_CLASS_UID)) != null) {
            gi.setSOPClassUID(temp.trim());
        }
        if ((temp = (String) numbers.get(DicomConstants.SOP_INSTANCE_UID)) != null) {
            gi.setSOPInstanceUID(temp.trim());
        }
        if ((temp = (String) numbers.get(DicomConstants.PATIENT_POSITION)) != null) {
            gi.setPatientPosition(temp.trim());
        }
        if ((temp = (String) numbers.get(
                        DicomConstants.SOURCE_TO_DETECTOR_DISTANCE)) != null) {
            gi.setSourceToDetectorDistance(Double.valueOf(temp.trim()));
        }
        if ((temp = (String) numbers.get(DicomConstants.SOURCE_SUBJECT_DISTANCE)) != null) {
            gi.setSourceSubjectDistance(Double.valueOf(temp.trim()));
        }
        if ((temp = (String) numbers.get(DicomConstants.FOCAL_SPOT_SIZE)) != null) {
            gi.setFocalSpotSize(Double.valueOf(temp.trim()));
        }
        if ((temp = (String) numbers.get(
                        DicomConstants.STORAGE_MEDIA_FILE_SET_UID)) != null) {
            gi.setStorageMediaFileSetUID(temp.trim());
        }
        if ((temp = (String) numbers.get(DicomConstants.ACQUISITION_MATRIX)) != null) {
            gi.setAcquisitionMatrix(Double.valueOf(temp.trim()));
        }
        if ((temp = (String) numbers
                .get(DicomConstants.DX_DATA_COLLECTION_DIAMETER)) != null) {
            gi.setDxDataCollectionDiameter(Double.valueOf(temp.trim()));
        }
        if ((temp = (String) numbers.get(DicomConstants.IMAGE_LATERALITY)) != null) {
            gi.setImageLaterality(temp.trim());
        }
        if ((temp = (String)numbers.get(DicomConstants.US_COLOR_DATA_PRESENT)) != null){
        	if(temp != null)
        	{
        		temp = temp.trim();
        	   	if (temp.equals("1")){
        	   		temp = "True";
        	   	}else
        	   	{
        	   		temp = "False";
        	   	}
        	}
        	gi.setUsColorDataPresent(temp);
        }
        if ((temp = (String)numbers.get(DicomConstants.US_NUM_FRAME)) != null) {
        	gi.setUsFrameNum(temp.trim());
        }
    }
    private static String getFirstValueOfImageType(String temp)
    {
    	String returnStr = null;
    	String[] token = temp.split("\\\\");

    	if (token.length > 0){
    		returnStr = token[0];
    	}

    	return returnStr;
    }

    private static String getMultiModalityEelement(String temp){
    	String returnStr = null;
    	String[] token = temp.split("\\\\");
    	if (token.length == 4){
    		returnStr = token[3];
    	}

    	return returnStr;
    }


    private static String getTrimmedSopInstanceUid(Map numbers) throws Exception {
        String sopInstanceUid = (String) numbers.get(DicomConstants.SOP_INSTANCE_UID);
        if (sopInstanceUid != null) {
            sopInstanceUid = sopInstanceUid.trim();
            return sopInstanceUid;
        }
        else {
            String noSopInstanceUidErrorMsg = "There is no SOP Instance UID in the submitted image.";
            log.error(noSopInstanceUidErrorMsg);
            throw new Exception(noSopInstanceUidErrorMsg);
        }
    }

    private List queryForExistingImage(Integer seriesInstanceUid,
                                              String sopInstanceUid) throws Exception {
        String hql = "from GeneralImage as image where ";
        hql += (" image.generalSeries.id = " + seriesInstanceUid);
        hql += (" and image.SOPInstanceUID = '" + sopInstanceUid + "' ");

        List ret = getHibernateTemplate().find(hql);
        if(ret == null || ret.size()==0) {
            //it's possible that series is different in database
            hql="from GeneralImage as image where ";
            hql += (" image.SOPInstanceUID = '" + sopInstanceUid + "' ");

            ret = getHibernateTemplate().find(hql);
        }
        return ret;
    }
}
