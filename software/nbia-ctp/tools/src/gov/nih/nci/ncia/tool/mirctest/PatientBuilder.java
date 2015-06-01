/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id: PatientBuilder.java 6366 2008-09-16 19:16:23Z kascice $
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
import gov.nih.nci.ncia.domain.TrialDataProvenance;
import gov.nih.nci.ncia.domain.TrialSite;
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
public class PatientBuilder extends DataBuilder  {
    Logger log = Logger.getLogger(PatientBuilder.class);

    public PatientBuilder(IDataAccess ida) throws Exception{
    	super(ida);
    }

   	public Patient buildPatient(Hashtable numbers, TrialDataProvenance tdp,
    			TrialSite site) throws Exception {
    		String temp;
    		String hql = "from Patient as patient where ";
    		Patient patient = new Patient();

    		if ((temp = (String) numbers.get("27")) != null) {
    			patient.setPatientId(temp.trim());
    			hql += (" patient.patientId = '" + temp.trim() + "' ");
    		} else {
    			hql += " patient.patientId is null ";
    		}

    		hql += (" and patient.dataProvenance.id = " + tdp.getId());
    		patient.setDataProvenance(tdp);
    		// System.out.println( "HQL: " + hql );
    		patient = (Patient) this.update(hql, patient);
    		patient.setTrialSite(site);

    		if ((temp = (String) numbers.get("26")) != null) {
    			patient.setPatientName(temp.trim());
    		}

    		if ((temp = (String) numbers.get("28")) != null) {
    			patient.setPatientBirthDate(stringToDate(temp.trim()));
    		}

    		if ((temp = (String) numbers.get("30")) != null) {
    			patient.setPatientSex(temp.trim());
    		}

    		if ((temp = (String) numbers.get("36")) != null) {
    			patient.setEthnicGroup(temp.trim());
    		}

    		if ((temp = (String) numbers.get("47")) != null) {
    			patient.setTrialSubjectId(temp.trim());
    		}

    		if ((temp = (String) numbers.get("48")) != null) {
    			patient.setTrialSubjectReadingId(temp.trim());
    		}

    		return patient;
    }


	public Patient retrievePaitentFromDB(Integer patientPkId) throws Exception {
        	
            String hql = "from Patient as patient where ";

            if (patientPkId != null) {
                hql += (" patient.id = " + patientPkId + " ");
            }

            List retList = null;
            retList = (List) getIDataAccess().search(hql);

            Patient patient=null;
            if ((retList != null) && (retList.size() != 0)) {
                patient = (Patient) retList.get(0);
            } else {
                System.out.println("cannot find the patient");
            }
            return patient;
    }
   
    public Hashtable addPatient(Hashtable numbersInDb, Patient pt) {
    	if (pt==null) {
    		System.out.println("Patient is null");
    		return numbersInDb;
    	}
    	if (pt.getPatientId()!=null) {	
    		numbersInDb.put(DicomConstants.PATIENT_ID, pt.getPatientId());
    	}
    	if (pt.getPatientName()!=null) {
    		numbersInDb.put(DicomConstants.PATIENT_NAME, pt.getPatientName());
    	}
    	if (pt.getPatientBirthDate()!=null) {
    		numbersInDb.put(DicomConstants.PATIENT_BIRTH_DATE, pt.getPatientBirthDate());
    	}
    	//get this field from general_series
//    	if (pt.getPatientSex()!=null)
//    		numbersInDb.put(DicomConstants.PATIENT_SEX, pt.getPatientSex());
    	if (pt.getEthnicGroup()!=null) {
    		numbersInDb.put(DicomConstants.ETHNIC_GROUP, pt.getEthnicGroup());
    	}
    	if (pt.getTrialSubjectId()!=null) {
    		numbersInDb.put(DicomConstants.CLINICAL_TRIAL_SUBJECT_ID, pt.getTrialSubjectId());
    	}
    	if (pt.getTrialSubjectReadingId()!=null) {
    		numbersInDb.put(DicomConstants.CLINICAL_TRIAL_SUBJECT_READING_ID, pt.getTrialSubjectReadingId());
    	}
    	
    	return numbersInDb;
    }
    
    
}
