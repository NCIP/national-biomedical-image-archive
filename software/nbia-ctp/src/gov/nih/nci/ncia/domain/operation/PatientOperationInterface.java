package gov.nih.nci.ncia.domain.operation;

import gov.nih.nci.ncia.internaldomain.TrialDataProvenance;
import gov.nih.nci.ncia.internaldomain.TrialSite;

import java.util.Map;

public interface PatientOperationInterface {
	public Object validate(Map numbers) throws Exception ;
	public void setSite(TrialSite site);
	public void setTdp(TrialDataProvenance tdp);
	
}
