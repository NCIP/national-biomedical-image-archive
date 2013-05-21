package gov.nih.nci.ncia.griddao;

import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;

public interface PatientDAOInterface {
	public List<Date> getTimepointStudyForPatient(String patientId) throws DataAccessException;
}
