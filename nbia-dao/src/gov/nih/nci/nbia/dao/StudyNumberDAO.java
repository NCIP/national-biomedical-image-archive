package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.StudyNumberDTO;

import java.util.Map;

import org.springframework.dao.DataAccessException;

public interface StudyNumberDAO {

	public Map<Integer, StudyNumberDTO> findAllStudyNumbers() throws DataAccessException;

}
