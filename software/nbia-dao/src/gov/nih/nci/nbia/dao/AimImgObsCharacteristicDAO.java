/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.ImgObsCharacteristicDTO;

import java.util.Collection;

import org.springframework.dao.DataAccessException;

public interface AimImgObsCharacteristicDAO {
	public Collection<ImgObsCharacteristicDTO> findAllCodeMeaningNamesAndValuePairs() throws DataAccessException;

	
	//public Collection<AimImagingObservationCharacteristicDTO> findBySeriesId(int seriesId) throws DataAccessException;

}