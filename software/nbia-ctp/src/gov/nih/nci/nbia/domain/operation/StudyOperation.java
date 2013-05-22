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

import gov.nih.nci.nbia.internaldomain.Patient;
import gov.nih.nci.nbia.internaldomain.Study;
import gov.nih.nci.nbia.util.AdapterUtil;
import gov.nih.nci.nbia.util.DicomConstants;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 *
 */
public class StudyOperation extends DomainOperation implements StudyOperationInterface{
	private static final Logger log = Logger.getLogger(StudyOperation.class);
	
	private Patient patient;
	
	public StudyOperation() {
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public Object validate(Map numbers) throws Exception {
	    Study study = null;
	    
	    try {   				    
		    List rs = getHibernateTemplate().find(buildQueryToFindExistingStudy(numbers, patient));
		    if(rs != null && rs.size() > 0) {
		    	study = (Study)rs.get(0);
		    	//db constraints will make 1 the max		    	
		    }
		    else {
		    	study = (Study)SpringApplicationContext.getBean("study");
		    	setStudyInstanceUid(study, numbers);
		    	study.setPatient(patient);
		    }
		
		    populateStudyFromNumbers(numbers, study);
	    }
	    catch(Exception e) {
	    	log.error("Exception in StudyOperation " + e);
	    	throw new Exception("Exception in StudyOperation " + e);
	    }

	    return study;
	}
	
	/**
	 * @param patient the patient to set
	 */
	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	/////////////////////////////////PRIVATE/////////////////////////////////////////////
	
    /**
     * Given the "numbers" map with all the parsed out dicom tag values we
     * care about..... populate the study object with these values.
     * 
     * <p>the study could be new are already have existed and this
     * will write over old values from db.
     */
    private static void populateStudyFromNumbers(Map numbers, 
    		                                     Study study) throws Exception {
    	    
		setStudyDate(study, numbers);
	
		setStudyTime(study, numbers);
	
		setStudyDescription(study, numbers);
	
		setStudyId(study, numbers);
	
		setAdmittingDiagnosesCodeSequence(study, numbers);
	
	    setAdmittingDiagnosesDescription(study, numbers);
	
	    setClinicalTrialTimePointId(study, numbers);
	
	    setClinicalTrialTimePointDescription(study, numbers);
	
	    setAge(study, numbers);
	
	    setOccupation(study, numbers);
	
	    setPatientSize(study, numbers);
	
	    setPatientWeight(study, numbers);
	
	    setAdditionalPatientHistory(study, numbers);  	
    }
    
	private static void setStudyInstanceUid(Study study, Map numbers) {
	    String studyInstanceUid = (String) numbers.get(DicomConstants.STUDY_INSTANCE_UID);

	    if (studyInstanceUid != null) {
	        study.setStudyInstanceUID(studyInstanceUid.trim());
	    } 		
	}
	
	private static String buildQueryToFindExistingStudy(Map numbers, Patient patient) throws Exception {
	    String hql = "from Study as study where ";
	
	    String studyInstanceUid = (String) numbers.get(DicomConstants.STUDY_INSTANCE_UID);
	    
	    if (studyInstanceUid != null) {
	        //study.setStudyInstanceUID(temp.trim());
	        hql += ("study.studyInstanceUID = '" + studyInstanceUid.trim() + "' ");
	    } 
	    else {
	    	String noStudyInstanceUidErrorMsg = "There is no Study Instance UID in the submitted image.";
	    	log.error(noStudyInstanceUidErrorMsg);
	        throw new Exception(noStudyInstanceUidErrorMsg);
	    }
	
	    hql += (" and study.patient.id = " + patient.getId());
	    //study.setPatient(patient);
	    return hql;
	}

	private static void setStudyDate(Study study, Map numbers) throws Exception {
		String studyDate = (String) numbers.get(DicomConstants.STUDY_DATE);
	    if (studyDate != null) {
	        study.setStudyDate(AdapterUtil.stringToDate(studyDate.trim()));
	    }		
	}
	
	private static void setStudyTime(Study study, Map numbers) {
		String studyTime = (String) numbers.get(DicomConstants.STUDY_TIME);
	    if (studyTime != null) {
	        study.setStudyTime(studyTime.trim());
	    }		
	}
	
	private static void setStudyDescription(Study study, Map numbers) {
		String studyDescription = (String) numbers.get(DicomConstants.STUDY_DESCRIPTION);
	    if (studyDescription != null) {
	        study.setStudyDesc(studyDescription.trim());
	    }
	    
	}
	
	private static void setStudyId(Study study, Map numbers) {
		String studyId = (String) numbers.get(DicomConstants.STUDY_ID);
	    if (studyId != null) {
	        study.setStudyId(studyId.trim());
	    }		
	}

	private static void setAdmittingDiagnosesDescription(Study study, Map numbers) {
		String admittingDiagnosesDescription = (String) numbers.get(DicomConstants.ADMITTING_DIAGNOSES_DESCRIPTION);
	    if (admittingDiagnosesDescription != null) {
	    	study.setAdmittingDiagnosesDesc(admittingDiagnosesDescription.trim());
	    }		
	}
	
	private static void setAdmittingDiagnosesCodeSequence(Study study, Map numbers) {
		String admittingDiagnosesCodeSequence = (String) numbers.get(DicomConstants.ADMITTING_DIAGNOSES_CODE_SEQUENCE);
	    if (admittingDiagnosesCodeSequence != null) {
	    	study.setAdmittingDiagnosesCodeSeq(admittingDiagnosesCodeSequence.trim());
	    }
	}
	
	private static void setClinicalTrialTimePointId(Study study, Map numbers) {
		String clinicalTrialTimePointId = (String) numbers.get(DicomConstants.CLINICAL_TRIAL_TIME_POINT_ID);
	    if (clinicalTrialTimePointId != null) {
	    	study.setTimePointId(clinicalTrialTimePointId.trim());
	    }		
	}
	
	private static void setClinicalTrialTimePointDescription(Study study, Map numbers) {
		String clinicalTrialTimePointDescription = (String) numbers.get(DicomConstants.CLINICAL_TRIAL_TIME_POINT_DESCRIPTION);
	    if (clinicalTrialTimePointDescription != null) {
	    	study.setTimePointDesc(clinicalTrialTimePointDescription);
	    }		
	}
	
	private static void setAge(Study study, Map numbers) throws Exception  {
		String patientAge = (String) numbers.get(DicomConstants.PATIENT_AGE);
	    if (patientAge != null) {
	        study.setPatientAge(patientAge.trim());
	        study.setAgeGroup(AdapterUtil.convertToAgeGroup(patientAge.trim()));
	    }	
	}
	
	private static void setOccupation(Study study, Map numbers) {
		String occupation = (String) numbers.get(DicomConstants.OCCUPATION);
	    if (occupation != null) {
	        study.setOccupation(occupation.trim());
	    }		
	}
	
	private static void setPatientSize(Study study, Map numbers) {
		String patientSize = (String) numbers.get(DicomConstants.PATIENT_SIZE);
	    if (patientSize != null) {
	        study.setPatientSize(Double.valueOf(patientSize.trim()));
	    }		
	}
		
	private static void setPatientWeight(Study study, Map numbers) {
		String patientWeight = (String) numbers.get(DicomConstants.PATIENT_WEIGHT);
	    if (patientWeight != null) {
	        study.setPatientWeight(Double.valueOf(patientWeight.trim()));
	    }
	}
	
	private static void setAdditionalPatientHistory(Study study, Map numbers) {
		String additionalPatientHistory = (String) numbers.get(DicomConstants.ADDITIONAL_PATIENT_HISTORY);
	    if (additionalPatientHistory != null) {
	    	study.setAdditionalPatientHistory(additionalPatientHistory.trim());
	    }  		
	}    
}
