package gov.nih.nci.nbia.annotations;

import java.io.File;

import org.rsna.ctp.objects.XmlObject;
import org.rsna.ctp.objects.ZipObject;
import org.rsna.ctp.pipeline.Status;

public interface AnnotationSubmissionProcessor {

	public Status process(XmlObject file, File storedFile);

    public Status process(ZipObject file, File storedFile);
}
