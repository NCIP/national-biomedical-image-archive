/**
 * 
 */
package gov.nih.nci.nbia.dao;

import java.util.List;
import gov.nih.nci.nbia.internaldomain.Patient;
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
	public List<String> getCollectionValues() throws DataAccessException;
}
