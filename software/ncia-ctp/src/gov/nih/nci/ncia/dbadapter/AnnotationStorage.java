package gov.nih.nci.ncia.dbadapter;

import gov.nih.nci.ncia.internaldomain.Annotation;
import gov.nih.nci.ncia.internaldomain.SubmissionHistory;
import gov.nih.nci.ncia.util.SpringApplicationContext;
import gov.nih.nci.ncia.domain.operation.AnnotationOperationInterface;
import gov.nih.nci.ncia.domain.operation.AnnotationSubmissionHistoryOperationInterface;

import java.util.Date;
import org.apache.log4j.Logger;
import org.rsna.ctp.pipeline.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class AnnotationStorage extends HibernateDaoSupport{
	@Autowired
	private AnnotationOperationInterface ao;
	@Autowired
	private AnnotationSubmissionHistoryOperationInterface annotationSubmissionHistoryOperation;

	private Annotation annotation;

	public enum AnnotationType {
		XML("xml"),
		ZIP("zip");

		AnnotationType(String annotationType) {
			this.annotationType = annotationType;
		}

		private String annotationType;

		public String toString() {
			return annotationType;
		}
	};

	/**
	 * This code used to be BuildQueriesMediator.storeZip and storeXml.
	 * Those two methods were exactly the same except for the annotation type.
	 *
	 * This method will create a row in the annotation table
	 */
	public Status storeAnnotation(String studyInstanceUID,
			                      String seriesInstanceUID,
			                      String fileName,
			                      long fileSize,
			                      AnnotationType annotationType) {
		annotation = (Annotation)SpringApplicationContext.getBean("annotation");
        annotation.setAnnotationType(annotationType.toString());
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

	private static Logger log = Logger.getLogger(AnnotationStorage.class);
}
