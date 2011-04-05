package gov.nih.nci.ncia.domain.operation;

import java.util.Map;

import gov.nih.nci.ncia.internaldomain.GeneralSeries;
import gov.nih.nci.ncia.internaldomain.Patient;
import gov.nih.nci.ncia.internaldomain.Study;
import gov.nih.nci.ncia.internaldomain.TrialDataProvenance;

public interface ImageSubmissionHistoryOperationInterface {
	public void setProperties(boolean replacement,
            TrialDataProvenance dataProvenance,
            Patient patient,
            Study study,
            GeneralSeries series);
	public Object validate(Map numbers) throws Exception;
	
}
