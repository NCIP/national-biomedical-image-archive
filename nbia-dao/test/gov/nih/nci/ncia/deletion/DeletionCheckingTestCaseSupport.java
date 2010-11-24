package gov.nih.nci.ncia.deletion;

import gov.nih.nci.ncia.exception.DataAccessException;
import gov.nih.nci.ncia.internaldomain.GeneralSeries;
import gov.nih.nci.ncia.internaldomain.Patient;
import gov.nih.nci.ncia.internaldomain.Study;

public interface DeletionCheckingTestCaseSupport {
	public GeneralSeries getSeries(Integer id) throws DataAccessException;
	public Study getStudy(Integer id) throws DataAccessException;
	public Patient getPatient(Integer id) throws DataAccessException;
}
