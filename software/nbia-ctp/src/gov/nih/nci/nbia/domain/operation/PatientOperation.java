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
import gov.nih.nci.nbia.internaldomain.TrialDataProvenance;
import gov.nih.nci.nbia.internaldomain.TrialSite;
import gov.nih.nci.nbia.util.AdapterUtil;
import gov.nih.nci.nbia.util.DicomConstants;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import java.util.Map;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jimzhou
 *
 */
public class PatientOperation extends DomainOperation implements PatientOperationInterface{
	private static Logger log = Logger.getLogger(PatientOperation.class);
	private TrialDataProvenance tdp ;
	private TrialSite site;
	
	public PatientOperation() {

	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public Object validate(Map numbers) throws Exception {
	    Patient patient = (Patient)SpringApplicationContext.getBean("patient");
	    
	    try {		    
		    List rs = getHibernateTemplate().find(buildQueryToFindExistingPatient(numbers, tdp));
		    if(rs != null && rs.size() > 0) {
		    	if (rs.size() == 1) {
		    		patient = (Patient)rs.get(0);
		    	}
		    	else 
		    	if (rs.size() > 1) {
		    		throw new Exception("Patient table has duplicate records, please contact Data Team to fix data, then upload data again");
		    	}
		    }
		    else {
		    	setPatientId(patient, numbers);
			    patient.setDataProvenance(tdp);		
		    }
		    patient.setTrialSite(site);
	
		    //properties that can be revised through an update
		    setPatientName(patient, numbers);
	
		    setPatientBirthdate(patient, numbers);
	
		    setPatientSex(patient, numbers);
	
		    setPatientEthnicGroup(patient, numbers);
	
		    setClinicalTrialSubjectId(patient, numbers);
	
		    setClinicalTrialSubjectReadingId(patient, numbers);
	    }
	    catch(Exception e) {
	    	//log.error("Exception in PatientOperation " + e);
	    	throw new Exception("Exception in PatientOperation " + e);
	    }

	    return patient;
	}
	
	
	/**
	 * @param site the site to set
	 */
	public void setSite(TrialSite site) {
		this.site = site;
	}
	
	
	/**
	 * @param tdp the tdp to set
	 */
	public void setTdp(TrialDataProvenance tdp) {
		this.tdp = tdp;
	}
	
	//////////////////////////////////////////////PRIVATE/////////////////////////////////////////
	
	private static void setClinicalTrialSubjectReadingId(Patient patient, Map numbers) {
		String trialSubjectReadingId = (String) numbers.get(DicomConstants.CLINICAL_TRIAL_SUBJECT_READING_ID);
	    if (trialSubjectReadingId != null) {
	    	patient.setTrialSubjectReadingId(trialSubjectReadingId.trim());
	    }		
	}
	
	
	private static void setClinicalTrialSubjectId(Patient patient, Map numbers) {
		String trialSubjectId = (String) numbers.get(DicomConstants.CLINICAL_TRIAL_SUBJECT_ID);
	    if (trialSubjectId != null) {
	    	patient.setTrialSubjectId(trialSubjectId.trim());
	    }		
	}
	
	
	private static void setPatientEthnicGroup(Patient patient, Map numbers) {
		String ethnicGroup = (String) numbers.get(DicomConstants.ETHNIC_GROUP);
	    if (ethnicGroup != null) {
	        patient.setEthnicGroup(ethnicGroup.trim());
	    }		
	}
	
	
	private static void setPatientSex(Patient patient, Map numbers) {
		String patientSex = (String) numbers.get(DicomConstants.PATIENT_SEX);
	    if (patientSex != null) {
	        patient.setPatientSex(patientSex.trim());
	    }		
	}

	private static void setPatientBirthdate(Patient patient, Map numbers) throws Exception {
		String patientBirthDate = (String) numbers.get(DicomConstants.PATIENT_BIRTH_DATE);
	    if (patientBirthDate != null) {
	        patient.setPatientBirthDate(AdapterUtil.stringToDate(patientBirthDate.trim()));
	    }		
	}
	
	
	private static void setPatientName(Patient patient, Map numbers) {
		String patientName = (String) numbers.get(DicomConstants.PATIENT_NAME);
	    if (patientName != null) {
	        patient.setPatientName(patientName.trim());
	    }		
	}
	private static void setPatientId(Patient patient, Map numbers) {
    	String patientId = (String) numbers.get(DicomConstants.PATIENT_ID);
        
	    if (patientId != null) {
	        patient.setPatientId(patientId.trim());
	    }
	}

	private static String buildQueryToFindExistingPatient(Map numbers, TrialDataProvenance tdp) throws Exception {
	    StringBuilder hql = new StringBuilder("from Patient as patient where ");

    	String patientId = (String) numbers.get(DicomConstants.PATIENT_ID);
    
	    if (patientId != null) {
	        //patient.setPatientId(temp.trim());
	        hql.append(" patient.patientId = '" + patientId.trim() + "' ");
	    } 
	    else {
	    	String noPatientIdErrorMsg = "There is no Patient ID in the submitted image.";
	    	log.error(noPatientIdErrorMsg);
	        throw new Exception(noPatientIdErrorMsg);
	    }

	    hql.append(" and patient.dataProvenance.id = " + tdp.getId());
	    
	    return hql.toString();
	}
}
