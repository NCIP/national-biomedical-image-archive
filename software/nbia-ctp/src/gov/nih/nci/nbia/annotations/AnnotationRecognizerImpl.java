/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.annotations;

import java.io.File;

import org.rsna.ctp.objects.XmlObject;
import org.rsna.ctp.objects.ZipObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * As an XML annotation is submitted, look at content to see what kind it is
 * and provide a processor object that knows how to deal with it.
 *
 * <P>There's probably alot of opportunity to make this more extensible,
 * but no need for prototype...just supporting AIM or "traditional" (i.e. LIDC) annotations.
 */
public class AnnotationRecognizerImpl implements AnnotationRecognizer {

	public AnnotationSubmissionProcessor recognizeAnnotation(ZipObject file,
	  	                                                     File storedFile) {
		System.out.println("zip:"+file+", stored:"+storedFile);
        return traditionalAnnotationSubmissionProcessor;
    }

	public AnnotationSubmissionProcessor recognizeAnnotation(XmlObject file,
			                                                 File storedFile) {
        	return traditionalAnnotationSubmissionProcessor;
	}

	////////////////////////////////////////PRIVATE//////////////////////////////////////
	@Autowired
	@Qualifier("traditionalAnnotationSubmissionProcessor")
	private AnnotationSubmissionProcessor  traditionalAnnotationSubmissionProcessor;
}
