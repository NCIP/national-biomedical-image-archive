package gov.nih.nci.nbia.domain.operation;

import java.util.Map;

import gov.nih.nci.nbia.internaldomain.GeneralSeries;
import gov.nih.nci.nbia.internaldomain.Patient;
import gov.nih.nci.nbia.internaldomain.Study;
import gov.nih.nci.nbia.internaldomain.TrialDataProvenance;

public interface ImageSubmissionHistoryOperationInterface {
	public void setProperties(boolean replacement,
            TrialDataProvenance dataProvenance,
            Patient patient,
            Study study,
            GeneralSeries series);
	public Object validate(Map numbers) throws Exception;
	
}
