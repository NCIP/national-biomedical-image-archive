/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.customserieslist;

import gov.nih.nci.nbia.dao.CustomSeriesListDAO;
import gov.nih.nci.nbia.dao.GeneralSeriesDAO;
import gov.nih.nci.nbia.dto.CustomSeriesDTO;
import gov.nih.nci.nbia.dto.CustomSeriesListAttributeDTO;
import gov.nih.nci.nbia.dto.CustomSeriesListDTO;
import gov.nih.nci.nbia.dto.SeriesDTO;
import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.nbia.util.SiteData;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class CustomSeriesListProcessor {
	private static Logger logger = Logger.getLogger(CustomSeriesListProcessor.class);
	private AuthorizationManager am;
	private AuthorizationManager amForPublicRole;
	private GeneralSeriesDAO generalSeriesDAO = (GeneralSeriesDAO)SpringApplicationContext.getBean("generalSeriesDAO");
	private CustomSeriesListDAO	 customSeriesDAO = (CustomSeriesListDAO)SpringApplicationContext.getBean("customSeriesListDAO");
	private List<SiteData> authorizedSites, authorizedPublicSites;
	private List<String> authorizedSeriesSecurityGroups;
	private String username;

	public CustomSeriesListProcessor(String username, AuthorizationManager am) {
		this.username = username;
		try{
			System.out.println("username: " + username);
			//am = new AuthorizationManager(username);
			authorizedSites = am.getAuthorizedSites();
			authorizedSeriesSecurityGroups = am.getAuthorizedSeriesSecurityGroups();

			amForPublicRole = new AuthorizationManager();
			authorizedPublicSites = amForPublicRole.getAuthorizedSites();

		}catch(Exception e){
			logger.error("Error getting AuthorizationManager for user: " + username + e);
			throw new RuntimeException (e);
		}
	}
	public CustomSeriesListProcessor(String username) {
		this.username = username;
		try{
			System.out.println("username: " + username);
			am = new AuthorizationManager(username);
			authorizedSites = am.getAuthorizedSites();
			authorizedSeriesSecurityGroups = am.getAuthorizedSeriesSecurityGroups();

			amForPublicRole = new AuthorizationManager();
			authorizedPublicSites = amForPublicRole.getAuthorizedSites();

		}catch(Exception e){
			logger.error("Error getting AuthorizationManager for user: " + username + e);
			throw new RuntimeException (e);
		}
	}

	/**
	 * Determine whether user has permission to see the seriesUids
	 * @param seriesUids
	 * @return the seriesUids that user does not have permission to see
	 */
	public List<String> validate(List<String> seriesUids){
		List<String> noPermissionList = new ArrayList<String>();
		try{
			List<SeriesDTO> seriesDTOsFound = generalSeriesDAO.findSeriesBySeriesInstanceUID(seriesUids, authorizedSites, authorizedSeriesSecurityGroups);
			//user do not have permission to see any in the list.
			if(seriesDTOsFound.isEmpty()){
				logger.debug("The returned SeriesDTOs is empty. user do not have permission to see any series in the list.");
				return seriesUids;
			}
			//System.out.println("found: " + seriesDTOsFound.size());
			List<String> seriesUidsFound = new ArrayList<String>();
			for(SeriesDTO seriesDTO: seriesDTOsFound){
				String seriesInstanceUid = seriesDTO.getSeriesUID();
				seriesUidsFound.add(seriesInstanceUid);	
			}
			for(String seriesUid : seriesUids){
				if(!seriesUidsFound.contains(seriesUid)){
					noPermissionList.add(seriesUid);
				}
			}
		}catch(Exception e){
			logger.error("Error getting series by series instance uid" + e);
			throw new RuntimeException(e);
		}
		return noPermissionList;
	}

	/**
	 * Determine whether any of the seriesUids belong to private collection
	 * @param seriesUids
	 * @return the seriesUids that are in private collection
	 */
	public List<String> isAnyPrivate(List<String> seriesUids){
		List<String> privateSeries = new ArrayList<String>();
		List<CustomSeriesDTO> publicSeries=null;

		if(seriesUids == null ){
			return privateSeries;
		}
		try{
			/* retrieve public series */
			publicSeries = customSeriesDAO.findSeriesForPublicCollection(seriesUids, authorizedPublicSites);
			System.out.println("publicSeries... " + publicSeries.size());
			List<String> publicSeriesUids = new ArrayList<String>();
			for(CustomSeriesDTO seriesDTO: publicSeries){
				String seriesInstanceUid = seriesDTO.getSeriesUID();
				publicSeriesUids.add(seriesInstanceUid);
			}
			/* filter out what is not in public series -- they are private */			
			for(String seriesUid : seriesUids){
				if(!publicSeriesUids.contains(seriesUid) && !privateSeries.contains(seriesUid)){
					System.out.println("series not in public: " +seriesUid);
					privateSeries.add(seriesUid);
				}
			}
		}catch(Exception e){
			logger.error(e);
			throw new RuntimeException(e);
		}
		System.out.println("private series: " + privateSeries.size());
		return privateSeries;
	}
	
	/**
	 * check to see if this name has already taken
	 * @param name
	 * @return true if it already exists; false otherwise
	 */
	public boolean isDuplicateName(String name){
		return customSeriesDAO.isDuplicateName(name);
	}
	
	public CustomSeriesListDTO searchByName(String name){
		CustomSeriesListDTO dto = customSeriesDAO.findCustomSeriesListByName(name);
		return dto;
	}

	public List<CustomSeriesListDTO> searchByNameLikeSearch(String name){
		List<CustomSeriesListDTO> dto = customSeriesDAO.findCustomSeriesListByNameLikeSearch(name);
		return dto;
	}

	public List<SeriesDTO> getSeriesDTO(List<String> seriesUids){
		List<SeriesDTO> seriesDTOs=null;
		try{
			seriesDTOs = generalSeriesDAO.findSeriesBySeriesInstanceUID(seriesUids, authorizedSites, authorizedSeriesSecurityGroups);
		}catch(Exception e){
			logger.error("Error getting series dto" + e);
			throw new RuntimeException(e);
		}
		return seriesDTOs;
	}
	/**
	 * retrieve all custom list the user has created
	 * @param username
	 */
	public List<CustomSeriesListDTO> getCustomListByUser(String username){
		return customSeriesDAO.findCustomSeriesListByUser(username);
	}
	
	/**
	 * create new custom list for the user
	 * @param customList
	 */
	public long create(CustomSeriesListDTO customList){
		return customSeriesDAO.insert(customList, this.username);
	}
	
	/**
	 * retrieve custom list attributes for a custom list
	 * @param customSeriesListPkId
	 */
	public List<CustomSeriesListAttributeDTO> getCustomseriesListAttributesById(Integer customSeriesListPkId){
		return customSeriesDAO.findCustomSeriesListAttribute(customSeriesListPkId);
	}
	
	/**
	 * update the list
	 * @param editedList
	 */
	public long update(CustomSeriesListDTO editedList, Boolean updatedSeries ){
		return customSeriesDAO.update(editedList, this.username, updatedSeries);
	}
	
	/**
	 * update the useageCount
	 * @param id
	 */
	public void updateUsageCount(Integer id){
		customSeriesDAO.updateUsageCount(id.intValue());
	}	
	/**
		 * 
		 * @param customList
		 * @return
	*/
	public long delete (CustomSeriesListDTO customList) {
		return customSeriesDAO.delete(customList);
	}
	/**
	 * 
	 * @return
	*/
	public List<String> getSharedListUserNames() {
		return customSeriesDAO.getSharedListUserNames(); 
	}
	/**
	 * 
	 * @param uName userName
	 * @return email add
	 */
	public String findEmailByUserName(String uName) {
		return customSeriesDAO.findEmailByUserName( uName); 
	}
}