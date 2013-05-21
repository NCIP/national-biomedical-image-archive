package gov.nih.nci.nbia.domain.operation;

import java.util.Map;

public interface AnnotationSubmissionHistoryOperationInterface {
	public Object validate(Map numbers) throws Exception;
	public void setOperation(String studyInstanceUid,
            				String seriesInstanceUid);
}
