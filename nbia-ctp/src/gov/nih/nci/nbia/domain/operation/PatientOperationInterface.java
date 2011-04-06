package gov.nih.nci.nbia.domain.operation;

import gov.nih.nci.nbia.internaldomain.TrialDataProvenance;
import gov.nih.nci.nbia.internaldomain.TrialSite;

import java.util.Map;

public interface PatientOperationInterface {
	public Object validate(Map numbers) throws Exception ;
	public void setSite(TrialSite site);
	public void setTdp(TrialDataProvenance tdp);
	
}
