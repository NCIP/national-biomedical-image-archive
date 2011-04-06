package gov.nih.nci.ncia.dao;

import gov.nih.nci.ncia.dto.PatientDTO;

import org.springframework.dao.DataAccessException;

public interface PatientDAO {
	 
	/**
	 * Fetch Patient Object through patient PK ID
	 * @param pid patient PK id
	 */
	public PatientDTO getPatientById(Integer pid) throws DataAccessException;
}
