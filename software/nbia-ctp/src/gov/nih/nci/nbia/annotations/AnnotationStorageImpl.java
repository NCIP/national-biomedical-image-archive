/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.annotations;

import java.io.File;
import org.rsna.ctp.objects.ZipObject;
import org.rsna.ctp.objects.XmlObject;
import org.rsna.ctp.pipeline.Status;
import org.springframework.beans.factory.annotation.Autowired;

public class AnnotationStorageImpl implements AnnotationStorage {
	public Status process(XmlObject file, File storedFile) {

		try {
		    AnnotationSubmissionProcessor processor = recognizer.recognizeAnnotation(file, storedFile);

	        return processor.process(file, storedFile);
		}
		catch(Throwable t) {
			t.printStackTrace();
			return Status.FAIL;
		}
	}

	public Status process(ZipObject file, File storedFile) {

		try {
		    AnnotationSubmissionProcessor processor = recognizer.recognizeAnnotation(file, storedFile);

	        return processor.process(file, storedFile);
		}
		catch(Throwable t) {
			t.printStackTrace();
			return Status.FAIL;
		}
	}

	@Autowired
	AnnotationRecognizer recognizer;
}
