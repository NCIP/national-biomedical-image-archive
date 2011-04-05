package gov.nih.nci.ncia.domain.operation;

import gov.nih.nci.ncia.internaldomain.GeneralImage;

import java.util.Map;

public interface CTImageOperationInterface {
	public Object validate(Map numbers) throws Exception;
	public void setGeneralImage(GeneralImage gi);
	
}
