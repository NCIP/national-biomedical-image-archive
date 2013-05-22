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
package gov.nih.nci.ncia;

import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Group;

import org.apache.log4j.Logger;

/**
 * @author lethai
 *
 */
public class CustomizedCQLQuery {
	
	
	/**
	 * This method takes the requested cqlQuery, modify it so that Image is the returned target. This is to be used in the 
	 * retrieveDicomData method only so that we can access SOPInstanceUID from Image object before using JDBC to query the 
	 * DICOM_FILE_URI for each image.
	 * 
	 * <P>If the target cannot be reconciled with image, null is returned.
	 */
	public static CQLQuery modifyCQLQueryToTargetImage(CQLQuery cqlQuery){
		
		gov.nih.nci.cagrid.cqlquery.Object cqlTarget = cqlQuery.getTarget();
		
		if (cqlTarget.getName().equalsIgnoreCase(IMAGE_DOMAIN)){
			logger.debug("target is image, do not need to do anything");
			return cqlQuery;
		}
		else 
		if(cqlTarget.getName().equalsIgnoreCase(SERIES_DOMAIN)){
			cqlQuery = processSeriesDomain(cqlQuery);			
			
		}
		else 
		if(cqlTarget.getName().equalsIgnoreCase(STUDY_DOMAIN)){	
			cqlQuery = processStudyDomain(cqlQuery);
			
		}
		else 
		if(cqlTarget.getName().equalsIgnoreCase(PATIENT_DOMAIN)){
			cqlQuery = processPatientDomain(cqlQuery);
			
		}
		else {
			return null;
		}
		
		return cqlQuery;
	}
	
	////////////////////////////////////////PRIVATE////////////////////////////////////////////////
	private static final String IMAGE_DOMAIN = "gov.nih.nci.ncia.domain.Image";
	private static final String SERIES_DOMAIN = "gov.nih.nci.ncia.domain.Series";
	private static final String STUDY_DOMAIN = "gov.nih.nci.ncia.domain.Study";
	private static final String PATIENT_DOMAIN = "gov.nih.nci.ncia.domain.Patient";
	private static final String SERIES_ROLE = "series";
	private static final String STUDY_ROLE = "study";
	private static final String PATIENT_ROLE = "patient";
	
	private static Logger logger = Logger.getLogger(CustomizedCQLQuery.class);
	
	private CustomizedCQLQuery() {
		
	}
	
	private static CQLQuery processSeriesDomain(CQLQuery cqlQuery){
		gov.nih.nci.cagrid.cqlquery.Object cqlTarget = cqlQuery.getTarget();
		Association assoc = cqlTarget.getAssociation();
		Attribute attr = cqlTarget.getAttribute();
		Group group = cqlTarget.getGroup();		
		
		gov.nih.nci.cagrid.cqlquery.Object newImageTarget = new gov.nih.nci.cagrid.cqlquery.Object();
		newImageTarget.setName(IMAGE_DOMAIN);
		
		Association seriesAssociation = new Association();	
		seriesAssociation.setName(SERIES_DOMAIN);
		seriesAssociation.setRoleName(SERIES_ROLE);
		if(assoc != null){
			seriesAssociation.setAssociation(assoc);
		}	
		
		if(attr != null){
			logger.debug("attribute name is " + attr.getName() + " value is " + attr.getValue() );
			seriesAssociation.setAttribute(attr);
		}
		if(group != null){
			logger.debug("group logical relation is " + group.getLogicRelation() );
			seriesAssociation.setGroup(group);
		}
		newImageTarget.setAssociation(seriesAssociation);
		cqlQuery.setTarget(newImageTarget);
		
		return cqlQuery;
		
	}
	
	private static CQLQuery processStudyDomain(CQLQuery cqlQuery){
		gov.nih.nci.cagrid.cqlquery.Object cqlTarget = cqlQuery.getTarget();
		Association assoc = cqlTarget.getAssociation();
		Attribute attr = cqlTarget.getAttribute();
		Group group = cqlTarget.getGroup();	
		
		gov.nih.nci.cagrid.cqlquery.Object newImageTarget = new gov.nih.nci.cagrid.cqlquery.Object();
		newImageTarget.setName(IMAGE_DOMAIN);
		
		Association seriesAssociation = new Association();	
		seriesAssociation.setName(SERIES_DOMAIN);
		seriesAssociation.setRoleName(SERIES_ROLE);
		
		Association studyAssociation = new Association();
		studyAssociation.setName(STUDY_DOMAIN);
		studyAssociation.setRoleName(STUDY_ROLE);
		
		seriesAssociation.setAssociation(studyAssociation);
		newImageTarget.setAssociation(seriesAssociation);
		
		if(assoc != null){
			logger.debug(" association is " + assoc.getName());
			studyAssociation.setAssociation(assoc);
		}
		
		if(attr != null){
			logger.debug("attribute name is " + attr.getName() + " value is " + attr.getValue() );
			studyAssociation.setAttribute(attr);
		}
		if(group != null){
			logger.debug("group logical relation is " + group.getLogicRelation() );
			studyAssociation.setGroup(group);
		}
		cqlQuery.setTarget(newImageTarget);		
		return cqlQuery;
	}
	
	private static CQLQuery processPatientDomain(CQLQuery cqlQuery){
		gov.nih.nci.cagrid.cqlquery.Object cqlTarget = cqlQuery.getTarget();
		Association assoc = cqlTarget.getAssociation();
		Attribute attr = cqlTarget.getAttribute();
		Group group = cqlTarget.getGroup();	
		
		gov.nih.nci.cagrid.cqlquery.Object newImageTarget = new gov.nih.nci.cagrid.cqlquery.Object();
		newImageTarget.setName(IMAGE_DOMAIN);
		
		Association seriesAssociation = new gov.nih.nci.cagrid.cqlquery.Association();	
		seriesAssociation.setName(SERIES_DOMAIN);
		seriesAssociation.setRoleName(SERIES_ROLE);
		
		Association associationStudy = new Association();
		associationStudy.setName(STUDY_DOMAIN);
		associationStudy.setRoleName(STUDY_ROLE);
		seriesAssociation.setAssociation(associationStudy);
		
		Association associationPatient = new Association();
		associationPatient.setName(PATIENT_DOMAIN);
		associationPatient.setRoleName(PATIENT_ROLE);
		
		associationStudy.setAssociation(associationPatient);
		newImageTarget.setAssociation(seriesAssociation);
		if(assoc != null){
			associationPatient.setAssociation(assoc);
		}
		
		if(attr != null){
			logger.debug("attribute name is " + attr.getName() + " value is " + attr.getValue() );
			associationPatient.setAttribute(attr);
		}
		if(group != null){
			logger.debug("group logical relation is " + group.getLogicRelation() );
			associationPatient.setGroup(group);
		}
		cqlQuery.setTarget(newImageTarget);	
		return cqlQuery;
	}
}
