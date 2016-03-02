/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.domain.operation;

import java.util.Date;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import gov.nih.nci.nbia.internaldomain.GeneralSeries;
import gov.nih.nci.nbia.internaldomain.Patient;
import gov.nih.nci.nbia.internaldomain.Study;
import gov.nih.nci.nbia.internaldomain.SubmissionHistory;
import gov.nih.nci.nbia.internaldomain.TrialDataProvenance;
import gov.nih.nci.nbia.util.DicomConstants;
import gov.nih.nci.nbia.util.SpringApplicationContext;

public class ImageSubmissionHistoryOperation extends DomainOperation implements ImageSubmissionHistoryOperationInterface{

	/**
	 * This will add a row to the submission_history table.  
	 * 
	 * @param replacement whether the image had already been submitted and is now being submitted again
	 * @param dataProvenance the provenance info for the image
	 * @param patient the patient for the image
	 * @param study the study for the image
	 * @param series the series for the image
	 */
	public ImageSubmissionHistoryOperation() {}

	//temp solution, will change it to arguement list
	public void setProperties(boolean replacement,
            TrialDataProvenance dataProvenance,
            Patient patient,
            Study study,
            GeneralSeries series){
		this.replacement = replacement;
		
		this.dataProvenance = dataProvenance;
		this.patient = patient;
		this.study = study;
		this.series = series;	
		
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public Object validate(Map numbers) throws Exception {
		
		//yep, always create a new one regardless
		SubmissionHistory submissionHistory = (SubmissionHistory)SpringApplicationContext.getBean("submissionHistory");
		
		String sopInstanceUid = (String) numbers.get(DicomConstants.SOP_INSTANCE_UID);
		if(sopInstanceUid!=null) {
        	submissionHistory.setSOPInstanceUID(sopInstanceUid.trim());
        }
		else {
			throw new RuntimeException("Cannot have image submission history without SOP instance UID");
		}
        submissionHistory.setSubmissionDate((Date)numbers.get("current_timestamp"));
        submissionHistory.setPatientId(patient.getPatientId());
        submissionHistory.setStudyInstanceUID(study.getStudyInstanceUID());
        submissionHistory.setSeriesInstanceUID(series.getSeriesInstanceUID());
        submissionHistory.setProject(dataProvenance.getProject());
        submissionHistory.setSite(dataProvenance.getDpSiteName());

        if(replacement) {
        	submissionHistory.setOperationType(SubmissionHistory.REPLACE_IMAGE_SUBMISSION_OPERATION);
        }
        else {
        	submissionHistory.setOperationType(SubmissionHistory.NEW_IMAGE_SUBMISSION_OPERATION);
        }
        
		return submissionHistory;
	}
	
    ///////////////////////////////PRIVATE////////////////////////////////
    private TrialDataProvenance dataProvenance;
    private Patient patient;
    private Study study;
    private GeneralSeries series;

    private boolean replacement;
}
