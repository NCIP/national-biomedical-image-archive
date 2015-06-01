/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.markup;

import org.springframework.dao.DataAccessException;

import gov.nih.nci.nbia.dto.MarkupDTO;

public interface MarkupManager {

	public abstract String getMarkups(MarkupDTO dto) throws DataAccessException;

	public abstract void saveMarkup(MarkupDTO dto)throws DataAccessException;

	public abstract void updateMarkup(MarkupDTO dto)throws DataAccessException;

	public abstract void insertMarkup(MarkupDTO dto)throws DataAccessException;

	public abstract boolean markupExist(MarkupDTO dto)throws DataAccessException;

	public abstract boolean isDuplicate(MarkupDTO dto)throws DataAccessException;

}