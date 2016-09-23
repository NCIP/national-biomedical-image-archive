/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.QcSearchResultDTO;
import gov.nih.nci.nbia.dto.QcStatusHistoryDTO;

import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;


/**
 * 
 */
public interface QcStatusDAO {

	public List<QcSearchResultDTO> findSeries(String[] qcStatus,
			                                  List<String> collectionSites, String[] additionalQcFlagList, 
			                                  String[] patients) throws DataAccessException;
		
		
	public List<QcSearchResultDTO> findSeries(String[] qcStatus,
            List<String> collectionSites, String[] additionalQcFlagList, 
            String[] patients, Date fromDate, Date toDate, int maxRows) throws DataAccessException;

	public List<QcStatusHistoryDTO> findQcStatusHistoryInfo(List<String> seriesList)throws DataAccessException;


	public void updateQcStatus(List<String> seriesList,
			                   List<String> statusList, 
			                   String newStatus, String[] additionalQcFlagList, String[] newAdditionalQcFlagList, 
			                   String userName, 
			                   String comment) throws DataAccessException;
	
	
}
