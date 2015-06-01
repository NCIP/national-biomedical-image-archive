/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
 * $Id: StudyBuilder.java 6360 2008-09-16 17:29:51Z kascice $
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
import gov.nih.nci.ncia.domain.Patient;
import gov.nih.nci.ncia.domain.Study;
import gov.nih.nci.ncia.util.DicomConstants;

import java.util.Hashtable;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * @author Rona Zhou
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class StudyBuilder extends DataBuilder {
	Logger log = Logger.getLogger(StudyBuilder.class);

	public StudyBuilder(IDataAccess ida) throws Exception {
		super(ida);
	}

	public Study buildStudy(Hashtable numbers, Patient patient)
			throws Exception {
		String temp;
		String hql = "from Study as study where ";
		Study study = new Study();

		if ((temp = (String) numbers.get("82")) != null) {
			study.setStudyInstanceUID(temp.trim());
			hql += ("study.studyInstanceUID = '" + temp.trim() + "' ");
		} else {
			hql += "study.studyInstanceUID is null ";
		}

		hql += (" and study.patient.id = " + patient.getId());
		study.setPatient(patient);

		study = (Study) this.update(hql, study);

		if ((temp = (String) numbers.get("6")) != null) {
			study.setStudyDate(this.stringToDate(temp.trim()));
		}

		if ((temp = (String) numbers.get("11")) != null) {
			study.setStudyTime(temp.trim());
		}

		if ((temp = (String) numbers.get("18")) != null) {
			study.setStudyDesc(temp.trim());
		}

		if ((temp = (String) numbers.get("84")) != null) {
			study.setStudyId(temp.trim());
		}

		if ((temp = (String) numbers.get("22")) != null) {
			study.setAdmittingDiagnosesCodeSeq(temp.trim());
		}

		if ((temp = (String) numbers.get("21")) != null) {
			study.setAdmittingDiagnosesDesc(temp.trim());
		}

		if ((temp = (String) numbers.get("49")) != null) {
			study.setTimePointId(temp.trim());
		}

		if ((temp = (String) numbers.get("50")) != null) {
			study.setTimePointDesc(temp.trim());
		}

		if ((temp = (String) numbers.get("32")) != null) {
			study.setPatientAge(temp.trim());
		}

		if ((temp = (String) numbers.get("32")) != null) {
			study.setAgeGroup(temp.trim());
		}

		if ((temp = (String) numbers.get("37")) != null) {
			study.setOccupation(temp.trim());
		}

		if ((temp = (String) numbers.get("33")) != null) {
			study.setPatientSize(Double.valueOf(temp.trim()));
		}

		if ((temp = (String) numbers.get("35")) != null) {
			study.setPatientWeight(Double.valueOf(temp.trim()));
		}

		if ((temp = (String) numbers.get("38")) != null) {
			study.setAdditionalPatientHistory(temp.trim());
		}

		return study;
	}

	public Study retrieveStudyFromDB(Integer studyPkId) throws Exception {
    	
        String hql = "from Study as obj where ";

        if (studyPkId != null) {
            hql += (" obj.id = " + studyPkId + " ");
        }

        List retList = null;
        retList = (List) getIDataAccess().search(hql);

        Study study=null;
        if ((retList != null) && (retList.size() != 0)) {
        	study = (Study) retList.get(0);
        } else {
            System.out.println("cannot find the StudyImpl");
        }
        return study;
	}
	public Hashtable addStudy(Hashtable numbersInDb, Study study) {

		//Hashtable numbersInDb=new Hashtable();
		if (study == null) {
			System.out.println("GeneralImage is null");
			return numbersInDb;
		}
		if (study.getStudyInstanceUID() != null) {
			numbersInDb.put(DicomConstants.STUDY_INSTANCE_UID, study.getStudyInstanceUID());
		}
		if (study.getStudyDate() != null) {
			numbersInDb.put(DicomConstants.STUDY_DATE, study
					.getStudyDate());
		}
		if (study.getStudyTime() != null) {
			numbersInDb.put(DicomConstants.STUDY_TIME, study.getStudyTime());
		}
		if (study.getStudyDesc() != null) {
			numbersInDb.put(DicomConstants.STUDY_DESCRIPTION, study.getStudyDesc());
		}
		if (study.getAdmittingDiagnosesDesc() != null) {
			numbersInDb.put(DicomConstants.ADMITTING_DIAGNOSES_DESCRIPTION, study.getAdmittingDiagnosesDesc());
		}
		if (study.getAdmittingDiagnosesCodeSeq() != null) {
			numbersInDb.put(DicomConstants.ADMITTING_DIAGNOSES_CODE_SEQUENCE, study
					.getAdmittingDiagnosesCodeSeq());
		}
		if (study.getStudyId() != null) {
			numbersInDb.put(DicomConstants.STUDY_ID, study
					.getStudyId());
		}
		if (study.getTimePointId() != null) {
			numbersInDb.put(DicomConstants.CLINICAL_TRIAL_TIME_POINT_ID,study
					.getTimePointId());
		}
		if (study.getPatientAge() != null) {
			numbersInDb.put(DicomConstants.PATIENT_AGE, study
					.getPatientAge());
		}
		if (study.getTimePointDesc() != null) {
			numbersInDb.put(DicomConstants.CLINICAL_TRIAL_TIME_POINT_DESCRIPTION, study.getTimePointDesc());
		}
		if (study.getStudyDesc() != null) {
			numbersInDb
					.put(DicomConstants.STUDY_DESCRIPTION, study.getStudyDesc());
		}
		if (study.getAdmittingDiagnosesDesc() != null) {
			numbersInDb.put(DicomConstants.ADMITTING_DIAGNOSES_DESCRIPTION, study
					.getAdmittingDiagnosesDesc());
		}
		if (study.getPatientAge() != null) {
			numbersInDb.put(DicomConstants.PATIENT_AGE, study.getPatientAge());
		}
		if (study.getPatientSize() != null) {
			numbersInDb.put(DicomConstants.PATIENT_SIZE, study.getPatientSize());
		}
		if (study.getOccupation() != null) {
			numbersInDb.put(DicomConstants.OCCUPATION, study
					.getOccupation());
		}
		if (study.getAgeGroup() != null) {
			numbersInDb.put(DicomConstants.PATIENT_AGE, study.getAgeGroup());
		}
		if (study.getPatientWeight() != null) {
			numbersInDb.put(DicomConstants.PATIENT_WEIGHT, study.getPatientWeight());
		}
		if (study.getAdditionalPatientHistory() != null) {
			numbersInDb.put(DicomConstants.ADDITIONAL_PATIENT_HISTORY, study
					.getAdditionalPatientHistory());
		}
		return numbersInDb;
	}
}
