/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import java.util.List;

import gov.nih.nci.nbia.dto.ModalityDescDTO;

import org.springframework.dao.DataAccessException;

public interface ModalityDescDAO {
	public ModalityDescDTO findModalityDescByModalityName(String modalityName) throws DataAccessException;
	
	public List<ModalityDescDTO> findAllModalityDesc() throws DataAccessException;

}
