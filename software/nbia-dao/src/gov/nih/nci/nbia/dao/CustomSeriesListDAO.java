/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.CustomSeriesDTO;
import gov.nih.nci.nbia.dto.CustomSeriesListAttributeDTO;
import gov.nih.nci.nbia.dto.CustomSeriesListDTO;
import gov.nih.nci.nbia.dto.QcCustomSeriesListDTO;
import gov.nih.nci.nbia.util.SiteData;

import java.util.List;

import org.springframework.dao.DataAccessException;

/**
 * handle all database transactions for the custom series list
 * 
 * @author lethai
 * 
 */
public interface CustomSeriesListDAO  {


	/**
	 * query database table to check for existence of name
	 * 
	 * @param name
	 */
	public boolean isDuplicateName(String name) throws DataAccessException;
	
	/**
	 * find series that belongs to public group
	 * @param seriesUids
	 * @param authorizedPublicSites
	 */
	public List<CustomSeriesDTO> findSeriesForPublicCollection(List<String> seriesUids,
									                           List<SiteData> authorizedPublicSites) throws DataAccessException;
	
	/**
	 * find all series that contains all the seriesuids and user has permission
	 * to see
	 * 
	 * @param seriesUids
	 * @param authorizedSites
	 * @param authorizedSeriesSecurityGroups
	 */
	public List<CustomSeriesDTO> findSeriesBySeriesInstanceUids(List<String> seriesUids,
			                                                    List<SiteData> authorizedSites,
			                                                    List<String> authorizedSeriesSecurityGroups) throws DataAccessException;
	
	/**
	 * Find all shared list by a given list of series instance uids
	 * @param seriesUids
	 */
	public List<QcCustomSeriesListDTO> findSharedListBySeriesInstanceUids(List<String> seriesUids) throws DataAccessException;
	
	public String findEmailByUserName(String uName)throws DataAccessException;

	/**
	 * update database with data in the dto
	 * 
	 */
	public long update(CustomSeriesListDTO editList, String userName, Boolean updatedSeries) throws DataAccessException;

	/**
	 * insert a new record for the custom series list
	 * 
	 * @param customList
	 */
	public long insert(CustomSeriesListDTO customList, String username) throws DataAccessException;
	
	/**
	 * find all the shared list for a given user
	 * @param username
	 */
	public List<CustomSeriesListDTO> findCustomSeriesListByUser(String username) throws DataAccessException;

	public CustomSeriesListDTO findCustomSeriesListByName(String name) throws DataAccessException;
	
	public List<CustomSeriesListDTO> findCustomSeriesListByNameLikeSearch(String name) throws DataAccessException;

	public List<CustomSeriesListAttributeDTO> findCustomSeriesListAttribute(
			Integer customSeriesListPkId) throws DataAccessException;
	

 	/**
	 * delete from database with data in the dto
	 * 
	 */
	public long delete(CustomSeriesListDTO editList) throws DataAccessException;
	
	/**
	 * 
	 * @return
	 * @throws DataAccessException
	 */
	public List<String> getSharedListUserNames() throws DataAccessException;
	
}