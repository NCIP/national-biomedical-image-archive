package gov.nih.nci.ncia.dao;

import gov.nih.nci.ncia.dto.ImgObsCharacteristicQuantificationDTO;

import java.util.Collection;
import org.springframework.dao.DataAccessException;

public interface AimImgObsCharacteristicQuantificationDAO {

	public Collection<String> findAllQuantificationNames() throws DataAccessException;

	public Collection<String> findAllValuesByName(String quantificationName) throws DataAccessException;

    public Collection<ImgObsCharacteristicQuantificationDTO> findAllQuantifications() throws DataAccessException;
}