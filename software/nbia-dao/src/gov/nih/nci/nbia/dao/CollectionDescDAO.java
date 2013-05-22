/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.CollectionDescDTO;

import java.util.List;

import org.springframework.dao.DataAccessException;

/**
 * handle query for Editing collection description feature
 * @author lethai
 *
 */
public interface CollectionDescDAO  {
	/**
	 * retrieve list of collection name from trial data provenance table
	 */
	public List<String> findCollectionNames() throws DataAccessException;

	
	public CollectionDescDTO findCollectionDescByCollectionName(String collectionName) throws DataAccessException;
	
	public long save(CollectionDescDTO collectionDescDTO) throws DataAccessException;
}
