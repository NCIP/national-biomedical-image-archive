package gov.nih.nci.nbia.annotations;

import java.io.File;
import org.rsna.ctp.objects.XmlObject;
import org.rsna.ctp.objects.ZipObject;

public interface AnnotationRecognizer {
	public AnnotationSubmissionProcessor recognizeAnnotation(XmlObject file,
	  	                                                     File storedFile);

	public AnnotationSubmissionProcessor recognizeAnnotation(ZipObject file,
	  	                                                     File storedFile);
}
