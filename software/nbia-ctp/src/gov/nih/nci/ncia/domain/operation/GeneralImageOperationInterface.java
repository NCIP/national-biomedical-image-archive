package gov.nih.nci.ncia.domain.operation;

import gov.nih.nci.ncia.internaldomain.GeneralSeries;
import gov.nih.nci.ncia.internaldomain.Patient;
import gov.nih.nci.ncia.internaldomain.Study;
import gov.nih.nci.ncia.internaldomain.TrialDataProvenance;

import java.util.Map;

public interface GeneralImageOperationInterface {
	   public Object validate(Map numbers) throws Exception;
	   public void setDataProvenance(TrialDataProvenance dataProvenance) ;
	   public void setPatient(Patient patient);
	   public void setSeries(GeneralSeries series);
	   public void setStudy(Study study);
	   public boolean isReplacement();
}
