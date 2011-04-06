package gov.nih.nci.ncia.deletion.dao;

import gov.nih.nci.ncia.deletion.DeletionAuditPatientInfo;
import gov.nih.nci.ncia.exception.DataAccessException;

public interface PatientDAO {

	public void removePatient(Integer patientId) throws DataAccessException;
	public int getTotalStudiesInPatient(Integer patientId) throws DataAccessException;
	public boolean checkPatientNeedToBeRemoved(Integer patientId, Integer count) throws DataAccessException;
	public DeletionAuditPatientInfo getDeletionAuditPatientInfo(Integer PatientId);
}
