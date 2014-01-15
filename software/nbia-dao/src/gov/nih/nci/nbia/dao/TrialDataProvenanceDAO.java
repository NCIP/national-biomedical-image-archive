/**
 * 
 */
package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.util.SiteData;

import java.util.List;

import org.springframework.dao.DataAccessException;

/**
 * @author panq
 *
 */


public interface TrialDataProvenanceDAO {
	/**
	 * Fetch Patient Object through project, ie. collection
	 * @param collection A label used to name a set of images collected for a specific trial or other reason.
	 * Assigned during the process of curating the data. The info is kept under project column
	 * This method is used for NBIA Rest API.
	 */
	public List<String> getCollectionValues(List<SiteData> siteData) throws DataAccessException;
}
