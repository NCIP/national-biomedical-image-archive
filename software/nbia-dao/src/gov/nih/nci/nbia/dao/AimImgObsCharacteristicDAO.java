package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.ImgObsCharacteristicDTO;

import java.util.Collection;

import org.springframework.dao.DataAccessException;

public interface AimImgObsCharacteristicDAO {
	public Collection<ImgObsCharacteristicDTO> findAllCodeMeaningNamesAndValuePairs() throws DataAccessException;

	
	//public Collection<AimImagingObservationCharacteristicDTO> findBySeriesId(int seriesId) throws DataAccessException;

}