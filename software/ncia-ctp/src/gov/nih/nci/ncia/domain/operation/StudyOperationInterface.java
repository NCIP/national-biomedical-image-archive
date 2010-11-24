package gov.nih.nci.ncia.domain.operation;

import gov.nih.nci.ncia.internaldomain.Patient;

import java.util.Map;

public interface StudyOperationInterface {
	public Object validate(Map numbers) throws Exception;
	public void setPatient(Patient patient);
	
}
