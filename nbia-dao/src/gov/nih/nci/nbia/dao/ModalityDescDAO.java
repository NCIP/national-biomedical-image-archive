package gov.nih.nci.ncia.dao;

import java.util.List;

import gov.nih.nci.ncia.dto.ModalityDescDTO;

import org.springframework.dao.DataAccessException;

public interface ModalityDescDAO {
	public ModalityDescDTO findModalityDescByModalityName(String modalityName) throws DataAccessException;
	
	public List<ModalityDescDTO> findAllModalityDesc() throws DataAccessException;

}
