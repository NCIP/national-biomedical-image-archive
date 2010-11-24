package gov.nih.nci.ncia.deletion.dao;

import java.util.List;

import gov.nih.nci.ncia.deletion.DeletionAuditPatientInfo;
import gov.nih.nci.ncia.deletion.DeletionAuditSeriesInfo;
import gov.nih.nci.ncia.deletion.DeletionAuditStudyInfo;
import gov.nih.nci.ncia.exception.DataAccessException;

public interface DeletionAuditTrailDAO {

	public void recordSeries(List<DeletionAuditSeriesInfo> seriesInfos, String userName) throws DataAccessException;
	public void recordStudy(DeletionAuditStudyInfo study, String userName) throws DataAccessException;
	public void recordPatient(DeletionAuditPatientInfo patient, String userName) throws DataAccessException;

}
