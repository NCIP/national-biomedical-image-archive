/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.domain.operation;

//import gov.nih.nci.ncia.dbadapter.NCIADatabase;

import gov.nih.nci.nbia.internaldomain.Patient;
import gov.nih.nci.nbia.internaldomain.Study;
import gov.nih.nci.nbia.internaldomain.SubmissionHistory;
import gov.nih.nci.nbia.internaldomain.TrialDataProvenance;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import java.util.Date;
import java.util.Map;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class AnnotationSubmissionHistoryOperation extends DomainOperation implements AnnotationSubmissionHistoryOperationInterface {

	public AnnotationSubmissionHistoryOperation() {}
	
	public void setOperation(String studyInstanceUid,
			                 String seriesInstanceUid)
	{
		this.seriesInstanceUid = seriesInstanceUid;
        this.studyInstanceUid = studyInstanceUid;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	public Object validate(Map numbers) throws Exception {
		
		//yep, always create a new one regardless
		//SubmissionHistory submissionHistory = (SubmissionHistory)NCIADatabase.ctx.getBean("submissionHistory");
		SubmissionHistory submissionHistory = (SubmissionHistory)SpringApplicationContext.getBean("submissionHistory");
        submissionHistory.setSubmissionDate(new Date());
        submissionHistory.setStudyInstanceUID(studyInstanceUid);
        submissionHistory.setSeriesInstanceUID(seriesInstanceUid);
        submissionHistory.setOperationType(SubmissionHistory.ANNOTATION_SUBMISSION_OPERATION);
        
        String patientId = figureOutPatientId(studyInstanceUid);
        if(patientId==null) {
        	throw new Exception("Cannot figure out patient ID for annotation submission:"+seriesInstanceUid);      	
        }
        else {
        	submissionHistory.setPatientId(patientId);
        	
            TrialDataProvenance tdp = figureOutTrialDataProvenance(patientId);
            if(tdp==null) {
            	throw new Exception("Cannot figure out Trial Data Provenance for annotation submission:"+seriesInstanceUid);      	
            }
            submissionHistory.setProject(tdp.getProject());
            submissionHistory.setSite(tdp.getDpSiteName());
        }
		return submissionHistory;
	}
	

    ///////////////////////////////PRIVATE////////////////////////////////
    private String studyInstanceUid;
    private String seriesInstanceUid;
    
    private TrialDataProvenance figureOutTrialDataProvenance(String patientId) {
    	
    	DetachedCriteria criteria = DetachedCriteria.forClass(Patient.class);
        criteria.setProjection(Projections.property("dataProvenance"));
        criteria.add(Restrictions.eq("patientId", patientId));

        
        List tdpList = getHibernateTemplate().findByCriteria(criteria);
        if(tdpList.size()==0) {
        	return null;
        }
        else {
        	return (TrialDataProvenance)tdpList.get(0);
        }
    }
    
    
    private String figureOutPatientId(String studyInstanceUid) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Study.class);
        criteria.setProjection(Projections.property("patient"));
        criteria.add(Restrictions.eq("studyInstanceUID", studyInstanceUid));

        
        List patientList = getHibernateTemplate().findByCriteria(criteria);
        if(patientList.size()==0) {
        	return null;
        }
        else {
        	Patient patient = (Patient)patientList.get(0);
        	return (String)patient.getPatientId();
        }
    }
}
