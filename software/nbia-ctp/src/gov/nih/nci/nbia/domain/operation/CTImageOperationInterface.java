package gov.nih.nci.nbia.domain.operation;

import gov.nih.nci.nbia.internaldomain.GeneralImage;

import java.util.Map;

public interface CTImageOperationInterface {
	public Object validate(Map numbers) throws Exception;
	public void setGeneralImage(GeneralImage gi);
	
}
