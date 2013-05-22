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
import org.rsna.ctp.pipeline.Status;

public interface AnnotationStorage {

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

	public Status process(XmlObject file, File storedFile);

	public Status process(ZipObject file, File storedFile);
}
