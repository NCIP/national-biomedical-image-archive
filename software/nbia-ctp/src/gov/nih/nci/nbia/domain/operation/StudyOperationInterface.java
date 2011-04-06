package gov.nih.nci.nbia.domain.operation;

import gov.nih.nci.nbia.internaldomain.Patient;

import java.util.Map;

public interface StudyOperationInterface {
	public Object validate(Map numbers) throws Exception;
	public void setPatient(Patient patient);
	
}
