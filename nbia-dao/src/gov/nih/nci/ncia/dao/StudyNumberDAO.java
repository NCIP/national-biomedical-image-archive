package gov.nih.nci.ncia.dao;

import gov.nih.nci.ncia.dto.StudyNumberDTO;

import java.util.Map;

import org.springframework.dao.DataAccessException;

public interface StudyNumberDAO {

	public Map<Integer, StudyNumberDTO> findAllStudyNumbers() throws DataAccessException;

}
