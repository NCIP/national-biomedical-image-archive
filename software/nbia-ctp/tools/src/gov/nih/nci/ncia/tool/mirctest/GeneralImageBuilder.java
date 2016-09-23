/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id: GeneralImageBuilder.java 6360 2008-09-16 17:29:51Z kascice $
*
* $Log: not supported by cvs2svn $
* Revision 1.17  2006/12/19 16:24:42  zhouro
* changed data type from Integer to Double
*
* Revision 1.16  2006/09/28 19:29:00  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
/*
 * Created on Jul 24, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gov.nih.nci.ncia.tool.mirctest;

import gov.nih.nci.ncia.db.IDataAccess;
import gov.nih.nci.ncia.domain.GeneralImage;
import gov.nih.nci.ncia.domain.GeneralSeries;
import gov.nih.nci.ncia.domain.Patient;
import gov.nih.nci.ncia.domain.Study;
import gov.nih.nci.ncia.domain.TrialDataProvenance;
import gov.nih.nci.ncia.util.DicomConstants;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * @author Rona Zhou
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class GeneralImageBuilder extends DataBuilder  {
    Logger log = Logger.getLogger(GeneralImageBuilder.class);
//    String filename;
//    String url;
//    long filesize;

    public GeneralImageBuilder(IDataAccess ida) throws Exception{
    	super(ida);
    }

    public GeneralImage buildGeneralImage(Hashtable numbers, TrialDataProvenance dataProvenance,
        Patient patient, Study study, GeneralSeries series)
        throws Exception {
        String temp;
        GeneralImage gi = new GeneralImage();
        String hql = "from GeneralImage as image where ";

        hql += (" image.generalSeries.id = " + series.getId());

        gi.setGeneralSeries(series);

        if ((temp = (String) numbers.get("4")) != null) {
            gi.setSOPInstanceUID(temp.trim());
            hql += (" and image.SOPInstanceUID = '" + temp.trim() + "' ");
        } else {
            hql += " and image.SOPInstanceUID is null ";
        }

        // System.out.println( "GENERAL_IMAGE HQL: " + hql );
        gi = (GeneralImage) update(hql, gi);

        gi.setDataProvenance(dataProvenance);
        gi.setPatient(patient);
        gi.setStudy(study);

        if ((temp = (String) numbers.get("88")) != null) {
            gi.setInstanceNumber(Integer.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get("9")) != null) {
            gi.setContentDate(stringToDate(temp.trim()));
        }

        if ((temp = (String) numbers.get("13")) != null) {
            gi.setContentTime(temp.trim());
        }

        if ((temp = (String) numbers.get("2")) != null) {
            gi.setImageType(temp.trim());
        }

        if ((temp = (String) numbers.get("87")) != null) {
            gi.setAcquisitionNumber(Integer.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get("8")) != null) {
            gi.setAcquisitionDate(this.stringToDate(temp.trim()));
        }

        if ((temp = (String) numbers.get("12")) != null) {
            gi.setAcquisitionTime(temp.trim());
        }

        if ((temp = (String) numbers.get("10")) != null) {
            gi.setAcquisitionDatetime(temp.trim());
        }

        if ((temp = (String) numbers.get("97")) != null) {
            gi.setImageComments(temp.trim());
        }

        if ((temp = (String) numbers.get("101")) != null) {
            gi.setLossyImageCompression(temp.trim());
        }

        if ((temp = (String) numbers.get("100")) != null) {
            gi.setPixelSpacing(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get("90")) != null) {
            gi.setImageOrientationPatient(temp.trim());
        }

        if ((temp = (String) numbers.get("89")) != null) {
            gi.setImagePositionPatient(temp.trim());
        }

        if ((temp = (String) numbers.get("58")) != null) {
            gi.setSliceThickness(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get("96")) != null) {
            gi.setSliceLocation(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get("98")) != null) {
            gi.setRows(Integer.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get("99")) != null) {
            gi.setColumns(Integer.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get("53")) != null) {
            gi.setContrastBolusAgent(temp.trim());
        }

        if ((temp = (String) numbers.get("63")) != null) {
            gi.setContrastBolusRoute(temp.trim());
        }

        if ((temp = (String) numbers.get("3")) != null) {
            gi.setSOPClassUID(temp.trim());
        }

        if ((temp = (String) numbers.get("4")) != null) {
            gi.setSOPInstanceUID(temp.trim());
        }

        if ((temp = (String) numbers.get("75")) != null) {
            gi.setPatientPosition(temp.trim());
        }

        if ((temp = (String) numbers.get("65")) != null) {
            gi.setSourceToDetectorDistance(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get("66")) != null) {
            gi.setSourceSubjectDistance(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get("73")) != null) {
            gi.setFocalSpotSize(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get("103")) != null) {
            gi.setStorageMediaFileSetUID(temp.trim());
        }

        if (numbers.get("FILE_NAME")  != null) {
            gi.setFilename((String)numbers.get("FILE_NAME"));
        }

        if ((temp = (String) numbers.get(DicomConstants.IMAGE_LATERALITY)) != null) {
            gi.setImageLaterality(temp.trim());
        }

        gi.setDicomSize((Long)(numbers.get("DICOM_SIZE")));
        gi.setSubmissionDate(new Date());

        gi.setPatientId(patient.getPatientId());
        gi.setStudyInstanceUID(study.getStudyInstanceUID());
        gi.setSeriesInstanceUID(series.getSeriesInstanceUID());

        String project = dataProvenance.getProject() + "-TRIAL-" +
            dataProvenance.getId();
        gi.setProject(project);

        return gi;
    }
    
	public   GeneralImage retrieveGeneralImageFromDB(Hashtable numbers) throws Exception {
        // Pass the check if none of the project name, site id, site name and
		// trial name is null.
// if ((numbers.get(DicomConstants.PROJECT_NAME) != null) &&
// (numbers.get(DicomConstants.SITE_ID) != null) &&
// (numbers.get(DicomConstants.SITE_NAME) != null) &&
// ((numbers.get(DicomConstants.TRIAL_NAME)) != null)) {
// return passedCheck;
        	
            String sopInUid = (String) numbers.get(DicomConstants.SOP_INSTANCE_UID);
            String seriesInUid = (String) numbers.get(DicomConstants.SERIES_INSTANCE_UID);
            String studyInUid = (String) numbers.get(DicomConstants.STUDY_INSTANCE_UID);
            String patId = (String) numbers.get(DicomConstants.PATIENT_ID);

            String hql = "from GeneralImage as image where ";
            boolean needsAnd = false;

            if (null != sopInUid) {
                hql += (" image.SOPInstanceUID = '" + sopInUid + "' ");
                needsAnd = true;
            }

            if (needsAnd && (null != seriesInUid)) {
                hql += (" and image.seriesInstanceUID = '" + seriesInUid +
                "' ");
            } else if (!needsAnd && (null != seriesInUid)) {
                hql += (" image.seriesInstanceUID = '" + seriesInUid + "' ");
                needsAnd = true;
            }

            if (needsAnd && (null != studyInUid)) {
                hql += (" and image.studyInstanceUID = '" + studyInUid + "' ");
            } else if (!needsAnd && (null != studyInUid)) {
                hql += (" image.studyInstanceUID = '" + studyInUid + "' ");
                needsAnd = true;
            }

            if (needsAnd && (null != patId)) {
                hql += (" and image.patientId = '" + patId + "' ");
            } else if (!needsAnd && (null != patId)) {
                hql += (" image.patientId = '" + patId + "' ");
            }

            List retList = null;
            retList = (List) getIDataAccess().search(hql);

            GeneralImage gi=null;
            if ((retList != null) && (retList.size() != 0)) {
                gi = (GeneralImage) retList.get(0);
                TrialDataProvenance tpi = gi.getDataProvenance();
            } else {
                System.out.println("cannot find the image");
            }
            return gi;
    }
    
    public Hashtable addGeneralImage(Hashtable numbersInDb, GeneralImage gi) {

    	//Hashtable numbersInDb=new Hashtable();
    	if (gi==null) {
    		System.out.println("GeneralImage is null");
    		return numbersInDb;
    	}
    	if (gi.getStudyInstanceUID()!=null)	{
    		numbersInDb.put(DicomConstants.STUDY_INSTANCE_UID, gi.getStudyInstanceUID());
    	}
    	if (gi.getSeriesInstanceUID()!=null) {
    		numbersInDb.put(DicomConstants.SERIES_INSTANCE_UID, gi.getSeriesInstanceUID());
    	}
    	if (gi.getSOPInstanceUID()!=null) {
    		numbersInDb.put(DicomConstants.SOP_INSTANCE_UID, gi.getSOPInstanceUID());
    	}
    	if (gi.getPatientId()!=null) {
    		numbersInDb.put(DicomConstants.PATIENT_ID, gi.getPatientId());
    	}
    	if (gi.getInstanceNumber()!=null) {
    		numbersInDb.put(DicomConstants.INSTANCE_NUMBER, gi.getInstanceNumber());
    	}
    	if (gi.getContentDate()!=null) {
    		numbersInDb.put(DicomConstants.CONTENT_DATE, gi.getContentDate());
    	}
    	if (gi.getContentTime()!=null) {
    		numbersInDb.put(DicomConstants.CONTENT_TIME, gi.getContentTime());
    	}
    	if (gi.getImageType()!=null) {
    		numbersInDb.put(DicomConstants.IMAGE_TYPE, gi.getImageType());
    	}
    	if (gi.getAcquisitionNumber()!=null) {
    		numbersInDb.put(DicomConstants.ACQUISITION_NUMBER, gi.getAcquisitionNumber());
    	}
    	if (gi.getAcquisitionDate()!=null) {
    		numbersInDb.put(DicomConstants.ACQUISITION_DATE, gi.getAcquisitionDate());
    	}
    	if (gi.getAcquisitionTime()!=null) {
    		numbersInDb.put(DicomConstants.ACQUISITION_TIME, gi.getAcquisitionTime());
    	}
    	if (gi.getAcquisitionDatetime()!=null) {
    		numbersInDb.put(DicomConstants.ACQUISITION_DATETIME, gi.getAcquisitionDatetime());
    	}
    	if (gi.getImageComments()!=null) {
    		numbersInDb.put(DicomConstants.IMAGE_COMMENTS, gi.getImageComments());
    	}
    	if (gi.getLossyImageCompression()!=null) {
    		numbersInDb.put(DicomConstants.LOSSY_IMAGE_COMPRESSION, gi.getLossyImageCompression());
    	}
    	if (gi.getPixelSpacing()!=null) {
    		numbersInDb.put(DicomConstants.PIXEL_SPACING, gi.getPixelSpacing());
    	}
    	if (gi.getImageOrientationPatient()!=null) {
    		numbersInDb.put(DicomConstants.PATIENT_IMAGE_ORIENTATION, gi.getImageOrientationPatient());
    	}
    	if (gi.getImagePositionPatient()!=null) {
    		numbersInDb.put(DicomConstants.PATIENT_IMAGE_POSITION, gi.getImagePositionPatient());
    	}
    	if (gi.getImageLaterality()!=null) {
    		numbersInDb.put(DicomConstants.IMAGE_LATERALITY, gi.getImageLaterality());
    	}
    	if (gi.getSliceThickness()!=null) {
    		numbersInDb.put(DicomConstants.SLICE_THICKNESS, gi.getSliceThickness());
    	}
    	if (gi.getSliceLocation()!=null) {
    		numbersInDb.put(DicomConstants.SLICE_LOCATION, gi.getSliceLocation());
    	}
    	if (gi.getRows()!=null) {
    		numbersInDb.put(DicomConstants.ROWS, gi.getRows());
    	}
    	if (gi.getColumns()!=null) {
    		numbersInDb.put(DicomConstants.COLUMNS, gi.getColumns());
    	}
    	if (gi.getContrastBolusAgent()!=null) {
    		numbersInDb.put(DicomConstants.CONTRAST_BOLUS_AGENT, gi.getContrastBolusAgent());
    	}
    	if (gi.getContrastBolusRoute()!=null) {
    		numbersInDb.put(DicomConstants.CONTRAST_BOLUS_ROUTE, gi.getContrastBolusRoute());
    	}
    	if (gi.getSOPClassUID()!=null) {
    		numbersInDb.put(DicomConstants.SOP_CLASS_UID, gi.getSOPClassUID());
    	}
    	if (gi.getPatientPosition()!=null) {
    		numbersInDb.put(DicomConstants.PATIENT_POSITION, gi.getPatientPosition());
    	}
    	if (gi.getSourceToDetectorDistance()!=null) {
    		numbersInDb.put(DicomConstants.SOURCE_TO_DETECTOR_DISTANCE, gi.getSourceToDetectorDistance());
    	}
    	if (gi.getSourceSubjectDistance()!=null) {
    		numbersInDb.put(DicomConstants.SOURCE_SUBJECT_DISTANCE, gi.getSourceSubjectDistance());
    	}
    	if (gi.getFocalSpotSize()!=null) {
    		numbersInDb.put(DicomConstants.FOCAL_SPOT_SIZE, gi.getFocalSpotSize());
    	}
    	if (gi.getStorageMediaFileSetUID()!=null) {
    		numbersInDb.put(DicomConstants.STORAGE_MEDIA_FILE_SET_UID, gi.getStorageMediaFileSetUID());
    	}
    	if (gi.getAcquisitionMatrix()!=null) {
    		numbersInDb.put(DicomConstants.ACQUISITION_MATRIX,gi.getAcquisitionMatrix());
    	}
    	if (gi.getDxDataCollectionDiameter()!=null) {
    		numbersInDb.put(DicomConstants.DX_DATA_COLLECTION_DIAMETER, gi.getDxDataCollectionDiameter());
    	}
    	if (gi.getVisibility()!=null) {
    		numbersInDb.put(DicomConstants.TRIAL_VISIBILITY, gi.getVisibility());
    	}
    	if (gi.getProject()!=null) {
    		numbersInDb.put(DicomConstants.PROJECT_NAME, gi.getProject());
    	}
		// not in original dicom file
    	if (gi.getSubmissionDate()!=null) {
    		numbersInDb.put("SUBMISSION_DATE", gi.getSubmissionDate());
    	}
    	if (gi.getFilename()!=null) {
    		numbersInDb.put("DICOM_FILE_URI", gi.getFilename());
    	}
    	if (gi.getDicomSize()!=null) {
    		numbersInDb.put("DICOM_SIZE", gi.getDicomSize());
    	}
		// these fields are not handled in domain level, although they are in
		// schema
		// numbersInDb.put("IMAGE_RECEIVING_TIMESTAMP", "");
		// numbersInDb.put("VERSION", "");

		// many to one relation
    	if (gi.getDataProvenance()!=null) {
    		numbersInDb.put("TRIAL_DP_PK_ID", gi.getDataProvenance().getId());
    	}
    	if (gi.getGeneralSeries()!=null) {
    		numbersInDb.put("GENERAL_SERIES_PK_ID", gi.getGeneralSeries().getId());
    	}
    	if (gi.getStudy()!=null) {
    		numbersInDb.put("STUDY_PK_ID", gi.getStudy().getId());
    	}
    	if (gi.getPatient()!=null) {
    		numbersInDb.put("PATIENT_PK_ID", gi.getPatient().getId());
    	}
    	if (gi.getCtimage()!=null){
    		if (gi.getCtimage()==null){
    			System.out.println("Ct image in gi is null");
    			
    		}
    		else {
    			numbersInDb.put("CTIMAGE_PK_ID", gi.getCtimage().getId());
    		}
    	}

		return numbersInDb;
    }
}
