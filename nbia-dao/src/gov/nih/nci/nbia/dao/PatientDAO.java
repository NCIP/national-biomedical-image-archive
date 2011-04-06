package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.PatientDTO;

import org.springframework.dao.DataAccessException;

public interface PatientDAO {
	 
	/**
	 * Fetch Patient Object through patient PK ID
	 * @param pid patient PK id
	 */
	public PatientDTO getPatientById(Integer pid) throws DataAccessException;
}
