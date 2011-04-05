package gov.nih.nci.ncia.domain.operation;

import gov.nih.nci.ncia.internaldomain.GeneralSeries;

import java.util.Map;

public interface AimOperationInterface {
	
	public void insertAimAnnotation(Map numbers, GeneralSeries series) throws Exception;
}