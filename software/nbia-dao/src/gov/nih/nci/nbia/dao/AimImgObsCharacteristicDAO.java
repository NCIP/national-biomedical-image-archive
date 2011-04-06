package gov.nih.nci.ncia.dao;

import gov.nih.nci.ncia.dto.ImgObsCharacteristicDTO;

import java.util.Collection;

import org.springframework.dao.DataAccessException;

public interface AimImgObsCharacteristicDAO {
	public Collection<ImgObsCharacteristicDTO> findAllCodeMeaningNamesAndValuePairs() throws DataAccessException;

	
	//public Collection<AimImagingObservationCharacteristicDTO> findBySeriesId(int seriesId) throws DataAccessException;

}