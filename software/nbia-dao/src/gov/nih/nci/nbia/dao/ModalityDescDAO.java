package gov.nih.nci.nbia.dao;

import java.util.List;

import gov.nih.nci.nbia.dto.ModalityDescDTO;

import org.springframework.dao.DataAccessException;

public interface ModalityDescDAO {
	public ModalityDescDTO findModalityDescByModalityName(String modalityName) throws DataAccessException;
	
	public List<ModalityDescDTO> findAllModalityDesc() throws DataAccessException;

}
