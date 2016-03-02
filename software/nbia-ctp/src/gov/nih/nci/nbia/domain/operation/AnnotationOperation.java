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

import gov.nih.nci.nbia.dbadapter.NCIADatabase;
import gov.nih.nci.nbia.internaldomain.Annotation;
import gov.nih.nci.nbia.internaldomain.GeneralSeries;
import gov.nih.nci.nbia.internaldomain.Study;

import java.util.Map;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
public class AnnotationOperation extends DomainOperation implements AnnotationOperationInterface{
	
	private static Logger log = Logger.getLogger(AnnotationOperation.class);
	
	public AnnotationOperation() {
	}
	
	/* (non-Javadoc)
	 * @see gov.nih.nci.ncia.domain.operation.DomainOperation#validate(java.util.Hashtable)
	 */
	public Object validate(Map numbers) throws Exception {
		return null;
	}
	
	/**
	 * This method sets the "has annotations" flag on the series
	 * if there is indeed an annotation for a given series.
	 */
	@Transactional (propagation=Propagation.REQUIRED)
	public void updateAnnotation(GeneralSeries series) throws Exception{
		
		String hql = "from Annotation as annotation where ";
        hql += (" annotation.seriesInstanceUID = '" +
        series.getSeriesInstanceUID() + "' ");
        
        try {
            List annotationList = getHibernateTemplate().find(hql);
    
            if ((annotationList != null) && (annotationList.size() != 0)) {
                Iterator aIter = annotationList.iterator();
    
                while (aIter.hasNext()) {
                	Annotation annotation = (Annotation) aIter.next();
                    annotation.setGeneralSeries(series);
                    getHibernateTemplate().saveOrUpdate(annotation);
                    series.setAnnotationsFlag(Boolean.TRUE);
                    getHibernateTemplate().saveOrUpdate(series);
                }
            }
        }catch(Exception e) {        	
        	throw new Exception("Exception in AnnotationOperation " + e);
        }		
	}
	

	/**
	 * This method is used when annotation file is submitted. 
	 */
	@Transactional (propagation=Propagation.REQUIRED)
	public void insertOrReplaceAnnotation(Annotation annotation,
			                              String filename, 
			                              String studyInstanceUID) throws Exception {
		
		//for window only
		filename = filename.replace("\\", "\\\\");
		String hql = "from Annotation as annotation where ";
        hql += (" annotation.filePath = '" + filename + "' ");
        
        List res = getHibernateTemplate().find(hql);
        if(res != null && res.size() > 0) {
        	Annotation annotationDB = (Annotation)res.get(0);
        	//if found in db then update the annotation and save it 
        	annotationDB.setAnnotationType(annotation.getAnnotationType());
        	annotationDB.setFilePath(annotation.getFilePath());
        	annotationDB.setFileSize(annotation.getFileSize());
        	annotationDB.setSubmissionDate(annotation.getSubmissionDate());
        	annotationDB.setSeriesInstanceUID(annotation.getSeriesInstanceUID());
        	annotation = annotationDB;
        	log.debug("annotation id" + annotation.getId());
        	log.debug("annotation getFileSize" + annotation.getFileSize());
        	log.debug("annotation getSubmissionDate" + annotation.getSubmissionDate());
        	
        }
        //if we found it, why do we store it again?????
        getHibernateTemplate().saveOrUpdate(annotation);

        linkAnnotationToSeries(annotation, studyInstanceUID);                            
	}
	
	private void linkAnnotationToSeries(Annotation annotation,
			                            String studyInstanceUID) throws Exception {
		
		GeneralSeries series = (GeneralSeries)NCIADatabase.ctx.getBean("series");
        String hql = "from GeneralSeries as series where "+
                     " series.seriesInstanceUID = '" +
                     annotation.getSeriesInstanceUID().trim() +
                     "' ";
        
        List ret = getHibernateTemplate().find(hql);
        if(ret != null && ret.size() > 0) {
        	if(ret.size() == 1) {
        		series = (GeneralSeries)ret.get(0);
        	}
	    	else 
    		if (ret.size() > 1) {
    			throw new Exception("Annotation submission failed -- series table has duplicate records, please contact Data Team to fix data, then upload data again");
	    	}
        }
        else {
        	throw new Exception("Annotation submission failed -- GeneralSeries table does not have matching record for " + "Series Instance UID:"+annotation.getSeriesInstanceUID()
        	+"\nAnnotation cannot be submitted before an image from its series (anymore).");
        }
        
        //make sure the series link to right study
        Study study =(Study)series.getStudy();
        if(study != null) {
            if(studyInstanceUID.equals(study.getStudyInstanceUID())){
            	if (series.getId() != null) {
                    series.setAnnotationsFlag(Boolean.TRUE);
                    getHibernateTemplate().saveOrUpdate(series);
                    annotation.setGeneralSeries(series);
                    getHibernateTemplate().saveOrUpdate(annotation);
                }        	
            }
            else {
            	log.error("Annotation submission failed -- study instance UID in annotation file:" + studyInstanceUID +" is different with studyInstance UID in database (" + study.getStudyInstanceUID()+")");
            	throw new Exception ("Annotation submission failed -- study instance UID in annotation file:" + studyInstanceUID +" is different from studyInstance UID in database (" + study.getStudyInstanceUID()+")\n"+
            	"The prossible cause of the error is that the uid of annotation file is not unique. It conflict with the annotation uid for study "+ study.getStudyInstanceUID());
            }
        }
        //first one should probably take care of things
        else {
        	throw new Exception("Annotation submission failed -- null study with given series.  Series Instance UID:"+annotation.getSeriesInstanceUID());
        }  
	}	
}
