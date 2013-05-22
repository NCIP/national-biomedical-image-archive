/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.annotations;

import java.io.File;
import java.util.Date;

import gov.nih.nci.nbia.domain.operation.AnnotationOperationInterface;
import gov.nih.nci.nbia.domain.operation.AnnotationSubmissionHistoryOperationInterface;
import gov.nih.nci.nbia.internaldomain.Annotation;
import gov.nih.nci.nbia.internaldomain.SubmissionHistory;
import gov.nih.nci.nbia.util.AnnotationUtil;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import org.apache.log4j.Logger;
import org.rsna.ctp.objects.XmlObject;
import org.rsna.ctp.objects.ZipObject;
import org.rsna.ctp.pipeline.Status;
import org.w3c.dom.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class TraditionalAnnotationSubmissionProcessor extends HibernateDaoSupport
                                                      implements AnnotationSubmissionProcessor {
	@Transactional(propagation=Propagation.REQUIRED)
	public Status process(XmlObject file, File storedFile) {
	    Document document = file.getDocument();
	    String seriesInstanceUID = AnnotationUtil.getSeriesInstanceUID(document);
	    String studyInstanceUID = AnnotationUtil.getStudyInstanceUID(document);

	    return processImpl(studyInstanceUID,
	    		           seriesInstanceUID,
	    		           storedFile,
	    		           file.getFile());
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public Status process(ZipObject file, File storedFile) {
        if (file.getManifestText().equals("")) {
            log.error("No valid manifest.xml test found");

            return Status.FAIL;
        }

	    Document document = file.getManifestDocument();
	    String seriesInstanceUID = AnnotationUtil.getSeriesInstanceUID(document);
	    String studyInstanceUID = AnnotationUtil.getStudyInstanceUID(document);

	    return processImpl(studyInstanceUID,
	    		           seriesInstanceUID,
	    		           storedFile,
	    		           file.getFile());
	}

	/////////////////////////////////////PROTECTED//////////////////////////////////////////
	/**
     * This is a transaction boundary for adding a database row
     * to the annotation table for a submission XML or Zip annotation
     *
     * @param studyInstanceUID Parsed from annotation file or manifest of zip
     * @param seriesInstanceUID Parsed from annotation file or manifest of zip
     * @param storedFile The file on disk
     * @param objectFile This may be the same as storedFile?  But legacy code
     *                   ask the XmlObject or ZipObject for getFile instead of
     *                   using storedFile... so that's what this is
     * @param annotationType XML or ZIP
     */
    protected Status processImpl(String studyInstanceUID,
    		                     String seriesInstanceUID,
    		                     File storedFile,
    		                     File objectFile) {
        // 1)get serial instance uid from xml file
        // 2) insert a new record to anotation table and get a new annotation pk id
        // 3) check if general_series table has it, make sure it links to right study
    	// 4)update its annotation column
        if (seriesInstanceUID.equals("") || studyInstanceUID.equals("")) {
            log.error("No seriesInstanceUID or studyInstanceUID found for file " +
            		objectFile.getAbsolutePath());

            return Status.FAIL;
        }

        String filename = storedFile.getAbsolutePath();
        long filesize = storedFile.length();
        Status status = null;

        try {
            status = storeAnnotation(studyInstanceUID,
            		                 seriesInstanceUID,
            		                 filename,
            		                 filesize);
        }
        catch(Exception e) {
        	final String ERROR_MSG = "Error in processAnnotation() for file ";
            if(e.getMessage() != null) {
                log.error(ERROR_MSG + objectFile.getAbsolutePath()+  " " +e.getMessage());
            }
            else {
                log.error(ERROR_MSG + objectFile.getAbsolutePath());
            }
            return Status.FAIL;
        }
        return status;
    }

    //////////////////////////////////////PRIVATE//////////////////////////////////////////////////

	@Autowired
	private AnnotationOperationInterface ao;

	@Autowired
	private AnnotationSubmissionHistoryOperationInterface annotationSubmissionHistoryOperation;

	private static Logger log = Logger.getLogger(TraditionalAnnotationSubmissionProcessor.class);






	private Status storeAnnotation(String studyInstanceUID,
			                       String seriesInstanceUID,
			                       String fileName,
			                       long fileSize) {
		Annotation annotation = (Annotation)SpringApplicationContext.getBean("annotation");
		if (fileName.toUpperCase().endsWith(".XML")) {
			annotation.setAnnotationType("xml");
		}
		else if (fileName.toUpperCase().endsWith(".ZIP")) {
			annotation.setAnnotationType("zip");
		}	
			
        annotation.setFilePath(fileName);
        Integer i = Integer.valueOf((int)fileSize);
        annotation.setFileSize(i);
        annotation.setSubmissionDate(new Date());
        annotation.setSeriesInstanceUID(seriesInstanceUID);

        try{
        	ao.insertOrReplaceAnnotation(annotation,
        			                     fileName,
        			                     studyInstanceUID);
        }catch(Exception e) {
        	log.error("Exception in storeAnnotation " + e);
        	e.printStackTrace();
        	return Status.FAIL;
        }

        try {

        	annotationSubmissionHistoryOperation.setOperation(studyInstanceUID, seriesInstanceUID);

        	SubmissionHistory submissionHistory =
        		(SubmissionHistory)annotationSubmissionHistoryOperation.validate(null);
            getHibernateTemplate().saveOrUpdate(submissionHistory);
        }
        catch(Exception e) {
        	log.error("Exception in storeAnnotation doing submission history " + e);
        	e.printStackTrace();
        	return Status.FAIL;

        }
		return Status.OK;
	}

}