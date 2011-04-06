package gov.nih.nci.nbia.domain.operation;

import gov.nih.nci.nbia.internaldomain.GeneralEquipment;
import gov.nih.nci.nbia.internaldomain.Patient;
import gov.nih.nci.nbia.internaldomain.Study;

import java.util.Map;

public interface SeriesOperationInterface {
	public Object validate(Map numbers) throws Exception;
	public void setEquip(GeneralEquipment equip);
	public void setPatient(Patient patient);
	public void setStudy(Study study);
	
}
