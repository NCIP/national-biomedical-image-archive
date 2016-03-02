/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id$
*
* $Log: not supported by cvs2svn $
* Revision 1.2  2007/09/27 23:32:22  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/07 12:05:23  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:39  bauerd
* Initial Check in of reorganized components
*
* Revision 1.40  2006/12/13 14:04:14  dietrich
* Grid enhancement
*
* Revision 1.39  2006/09/27 20:46:28  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.querystorage;

import gov.nih.nci.nbia.dto.QueryHistoryDTO;
import gov.nih.nci.nbia.dto.SavedQueryDTO;
import gov.nih.nci.nbia.exception.DuplicateQueryException;
import gov.nih.nci.nbia.query.DICOMQuery;

import java.util.List;
import org.springframework.dao.DataAccessException;


public interface QueryStorageManager {



    /**
     * Records a query in the query history
     *
     * @param query - the query that was run
     * @return The primary key of the new query history record
     * @throws Exception
     */
    public long addQueryToHistory(DICOMQuery query) throws DataAccessException;


    /**
     * Saves a query without inactivating any other existing queries
     *
     * @param query - the query to be saved
     * @return primary key of the saved query object created
     * @throws Exception
     */
    public long saveQuery(DICOMQuery query) throws DataAccessException, DuplicateQueryException;

    /**
     * This is called when a user edits a query.  It will add a new
     * active query and deactivate the existing query.
     *
     * @param query - the query to be saved
     * @return primary key of the saved query object created
     * @throws Exception
     */
    public long updateQuery(DICOMQuery query) throws DataAccessException, DuplicateQueryException;


    /**
     * Returns a count of the number of saved queries for a user
     *
     * @param userName
     * @throws Exception
     */
    public int getSavedQueryCount(String userName)throws DataAccessException;


    /**
     * Updates the new results flag for a saved query
     *
     * @param savedQueryId primary key of the saved query to update
     * @throws Exception
     */
    public void updateNewResultsFlag(Long savedQueryId) throws DataAccessException;

    /**
     * Sets new results flag to true
     *
     * @param savedQueryId
     *            primary key of the saved query to update
     * @throws Exception
     */
    public void addNewResultsForQuery(Long savedQueryId) throws DataAccessException;


    /**
     * Deactivates a list of queries
     *
     * @param savedQueries -
     *            list of queries to be deactivated
     * @throws Exception
     */
    public void deleteQueries(List<SavedQueryDTO> savedQueries) throws DataAccessException;


    /**
     * Retrieves a list of query history records for a user.  It will
     * not return all records; the results will be limited to a certain
     * number.
     *
     * @param username - login id of the user
     * @return - a list of DTOs for the user's queries
     * @throws Exception
     */
    public List<QueryHistoryDTO> retrieveQueryHistory(String username) throws DataAccessException;


    /**
     * Retrieves a list of active saved queries for a user
     *
     * @param username - login id of the user
     * @return list of active saved queries for the user
     * @throws Exception
     */
    public List<SavedQueryDTO> retrieveSavedQueries(String username) throws DataAccessException;

	/**
	 * Retrieves a list of active saved queries for a user
	 *
	 * @param username - login id of the user
	 * @return list of active saved queries for the user
	 * @throws Exception
	 */
	public List<SavedQueryDTO> retrieveAllSavedQueries() throws DataAccessException;



    /**
     * Retrieves a list of all saved queries for all user that are both
     * active and not marked as new
     *
     * @return list of saved queries that are active and not new
     * @throws Exception
     */
    public List<SavedQueryDTO> getListOfActiveNoNew() throws DataAccessException;
}
