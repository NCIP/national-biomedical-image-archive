/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id: QueryStorageManager.java 11967 2010-01-23 14:41:43Z kascice $
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

import gov.nih.nci.ncia.criteria.PersistentCriteria;
import gov.nih.nci.nbia.dao.AbstractDAO;
import gov.nih.nci.nbia.dto.QueryHistoryDTO;
import gov.nih.nci.nbia.dto.SavedQueryDTO;
import gov.nih.nci.nbia.exception.DuplicateQueryException;
import gov.nih.nci.nbia.internaldomain.QueryHistory;
import gov.nih.nci.nbia.internaldomain.SavedQuery;
import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.nbia.security.NCIAUser;
import gov.nih.nci.nbia.util.NCIAConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public class QueryStorageManagerImpl extends AbstractDAO
                                     implements QueryStorageManager {


    // Logger for this class
    private static Logger logger = Logger.getLogger(QueryStorageManager.class);

    /**
     * Records a query in the query history
     *
     * @param query - the query that was run
     * @return The primary key of the new query history record
     * @throws Exception
     */
	@Transactional(propagation=Propagation.REQUIRED)
    public long addQueryToHistory(DICOMQuery query) throws DataAccessException {
        long queryHistoryId = 0;

        // Create a query history object
        QueryHistory queryHistory = new QueryHistory();
        queryHistory.setElapsedTime(query.getElapsedTimeInMillis());
        queryHistory.setExecuteTime(query.getExecuteTime());

        // Associate with the user that ran the query
        queryHistory.setUser(getUser(query.getUserID()));

        // If a saved query was run, associate the query history record
        if (query.getSavedQueryId() > 0) {
            SavedQuery assocSavedQuery = (SavedQuery) getHibernateTemplate().load(SavedQuery.class,
                                                                                  query.getSavedQueryId());
            queryHistory.setSavedQuery(assocSavedQuery);
        }

        // Add criteria to be saved
        addStoredAttributes(queryHistory, query);

        // Save to database
        getHibernateTemplate().saveOrUpdate(queryHistory);

        // Return the primary key of the query history record just created
        queryHistoryId = queryHistory.getId();

        logger.info("Added query history record with ID = " +
            queryHistoryId);

        return queryHistoryId;
    }




    /**
     * Saves a query without inactivating any other existing queries
     *
     * @param query - the query to be saved
     * @return primary key of the saved query object created
     * @throws Exception
     */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public long saveQuery(DICOMQuery query) throws DataAccessException, DuplicateQueryException {
        // Default second param to zero and pass through
        return saveQuery(query, 0);
    }

    /**
     * This is called when a user edits a query.  It will add a new
     * active query and deactivate the existing query.
     *
     * @param query - the query to be saved
     * @return primary key of the saved query object created
     * @throws Exception
     */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public long updateQuery(DICOMQuery query) throws DataAccessException, DuplicateQueryException {
        // Pass the ID of the existing query to be deactivated
        return saveQuery(query, query.getSavedQueryId());
    }

    /**
     * Returns a count of the number of saved queries for a user
     *
     * @param userName
     * @throws Exception
     */
	@Transactional(propagation=Propagation.REQUIRED)
    public int getSavedQueryCount(String userName) throws DataAccessException {
        Integer count = null;

        // Create criteria to get the count of saved queries
        SavedQuery sq = new SavedQuery();
        sq.setUserId(getUser(userName).getUserId());
        sq.setActive(true);

        DetachedCriteria crit = DetachedCriteria.forClass(SavedQuery.class);
        crit.add(Example.create(sq));

        // Need to do distinct because the query will
        // bring back one row for each query attribute
        crit.setProjection(Projections.countDistinct("id"));

        List results = getHibernateTemplate().findByCriteria(crit);
        if(results.size()>0) {
        	count = (Integer) results.get(0);
        }

        return (count != null) ? count : 0;
    }

    /**
     * Updates the new results flag for a saved query
     *
     * @param savedQueryId primary key of the saved query to update
     * @throws Exception
     */
	@Transactional(propagation=Propagation.REQUIRED)
    public void updateNewResultsFlag(Long savedQueryId) throws DataAccessException {
        setNewResultsFlagForQuery(savedQueryId, false);
    }

    /**
     * Sets new results flag to true
     *
     * @param savedQueryId
     *            primary key of the saved query to update
     * @throws Exception
     */
	@Transactional(propagation=Propagation.REQUIRED)
    public void addNewResultsForQuery(Long savedQueryId) throws DataAccessException {
        setNewResultsFlagForQuery(savedQueryId, true);
    }



    /**
     * Deactivates a list of queries
     *
     * @param savedQueries -
     *            list of queries to be deactivated
     * @throws Exception
     */
	@Transactional(propagation=Propagation.REQUIRED)
    public void deleteQueries(List<SavedQueryDTO> savedQueries) throws DataAccessException {


        for (SavedQueryDTO dto : savedQueries) {
            // Load the saved query, deactivate it and then save it
            SavedQuery savedQuery = (SavedQuery) getHibernateTemplate().load(SavedQuery.class,
                                                                             dto.getId());
            savedQuery.setActive(false);
            logger.info("QueryStorageManager.deleteQueries..... calling store()");

            getHibernateTemplate().saveOrUpdate(savedQuery);
            logger.info("Deactivated saved query ID = " + dto.getId());
        }
    }

    /**
     * Retrieves a list of query history records for a user.  It will
     * not return all records; the results will be limited to a certain
     * number.
     *
     * @param username - login id of the user
     * @return - a list of DTOs for the user's queries
     * @throws Exception
     */
	@Transactional(propagation=Propagation.REQUIRED)
    public List<QueryHistoryDTO> retrieveQueryHistory(String username) throws DataAccessException {
        // List to hold the results
        List<QueryHistoryDTO> queryHistoryRecords = new ArrayList<QueryHistoryDTO>();

        // Determine the number of results to a return (read from config)
        int numberOfQueriesToReturn = NCIAConfig.getNumberOfQueriesOnHistoryPage();


        // Build criteria to get user's query history
        QueryHistory qh = new QueryHistory();
        qh.setUserId(this.getUser(username).getUserId());

        DetachedCriteria crit = DetachedCriteria.forClass(QueryHistory.class);
        crit.add(Example.create(qh));

        // Load saved query data in the same query
        crit.setFetchMode("savedQuery", FetchMode.JOIN);

        // Need to do this to avoid a 1+N problem on last execute date
        // Because it is a one-to-one, Hibernate always tries to pull it in
        crit.setFetchMode("savedQuery.lastExecuteDate", FetchMode.JOIN);

        // Need to do distinct because query will actually return
        // one row for each query criterion, resulting in duplicate rows
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        // Sort query history in descending order
        crit.addOrder(Order.desc("executeTime"));

        List results = getHibernateTemplate().findByCriteria(crit);

        // Loop through results and build the DTOs
        Iterator iter = results.iterator();

        int count = 0;

        while (iter.hasNext() && (count < numberOfQueriesToReturn)) {
            QueryHistory queryHistory = (QueryHistory) iter.next();
            QueryHistoryDTO dto = new QueryHistoryDTO();
            dto.setId(queryHistory.getId());
            dto.setExecutionTime(queryHistory.getExecuteTime());

            // Set the list of criteria
            dto.setCriteriaList(getCriteriaFromStoredAttributes(queryHistory.getQueryHistoryAttributes()));

            // If the query history record is associated with a saved query,
            // get some data from the saved query
            SavedQuery associatedSavedQuery = queryHistory.getSavedQuery();

            if (associatedSavedQuery != null) {
                dto.setQueryName(associatedSavedQuery.getQueryName());
                dto.setSavedQueryIsInactive(!associatedSavedQuery.getActive());
                dto.setSavedQueryId(associatedSavedQuery.getId());
            }

            queryHistoryRecords.add(dto);

            // Count so that it only returns the proper amount
            count++;
        }


        return queryHistoryRecords;
    }

    /**
     * Retrieves a list of active saved queries for a user
     *
     * @param username - login id of the user
     * @return list of active saved queries for the user
     * @throws Exception
     */
	@Transactional(propagation=Propagation.REQUIRED)
    public List<SavedQueryDTO> retrieveSavedQueries(String username) throws DataAccessException {

        // Create criteria to get all active saved queries
        // for a user
        SavedQuery sq = new SavedQuery();
        sq.setActive(true);
        sq.setUserId(getUser(username).getUserId());

        DetachedCriteria crit = DetachedCriteria.forClass(SavedQuery.class);
        crit.add(Example.create(sq));

        // Set fetch modes so that these will be included in the same query
        crit.setFetchMode("savedQueryAttributes", FetchMode.JOIN);
        crit.setFetchMode("lastExecuteDate", FetchMode.JOIN);

        // Need to do distinct because query will actually return
        // one row for each query criterion, resulting in duplicate rows
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        return this.populateSavedQueryDTOs(getHibernateTemplate().findByCriteria(crit));
    }

    /**
     * Retrieves a list of all saved queries for all user that are both
     * active and not marked as new
     *
     * @return list of saved queries that are active and not new
     * @throws Exception
     */
	@Transactional(propagation=Propagation.REQUIRED)
    public List<SavedQueryDTO> getListOfActiveNoNew() throws DataAccessException {
        SavedQuery sq = new SavedQuery();
        sq.setActive(true);
        sq.setNewResults(false);

        DetachedCriteria crit = DetachedCriteria.forClass(SavedQuery.class);
        crit.add(Example.create(sq));

        return populateSavedQueryDTOs(getHibernateTemplate().findByCriteria(crit));
    }


	////////////////////////////////////////////////////////////////////PRIVATE////////////////////////////////////////////////////////////////

	   /**
     * Saves a query with a name so that the user can later retrieve it and resubmit
     *
     * @param query - the query to be saved
     * @param savedQueryToInactivateId - ID of an already existing query to be inactivated
     * @return primary key of the saved query object created
     * @throws Exception
     */
    private long saveQuery(DICOMQuery query, long savedQueryToInactivateId) throws DuplicateQueryException {
        long savedQueryId;

        NCIAUser user = getUser(query.getUserID());

        String nameForQuery = query.getQueryName();

        // Check for another saved query to inactivate
        // When editing a query, a new saved query will be created and the
        // old version will be inactivated.
        if (savedQueryToInactivateId > 0) {
            // Get a handle to the existing saved query, inactivate, and save
            SavedQuery savedQueryToInactivate = (SavedQuery) getHibernateTemplate().load(SavedQuery.class,
                                                                                         savedQueryToInactivateId);
            savedQueryToInactivate.setActive(false);
            getHibernateTemplate().saveOrUpdate(savedQueryToInactivate);
            logger.info("Deactivated saved query ID = " +
                savedQueryToInactivateId);
        }

        // Build and execute a query to check for the existence of
        // another active query with the same name
        SavedQuery sq = new SavedQuery();
        sq.setUserId(user.getUserId());
        sq.setActive(true);
        sq.setQueryName(nameForQuery);

        DetachedCriteria crit = DetachedCriteria.forClass(SavedQuery.class);
        crit.add(Example.create(sq));

        List results = getHibernateTemplate().findByCriteria(crit);

        // If there is a duplicate, throw an exception
        if (results.size() > 0) {
            throw new DuplicateQueryException();
        }

        // Build the saved query
        SavedQuery savedQuery = new SavedQuery();
        savedQuery.setActive(true);
        savedQuery.setNewResults(false);
        savedQuery.setQueryName(nameForQuery);

        // Associate with the user that is saving the query
        savedQuery.setUser(user);

        // Create criteria
        addStoredAttributes(savedQuery, query);

        // The most query history record (representing the data being saved)
        // also needs to be updated to point to the saved query
        // The query history was created when the user first ran the
        // query to see the results before saving it
        QueryHistory mostRecentQueryHistory = (QueryHistory) getHibernateTemplate().load(QueryHistory.class,
                                                                                         query.getQueryHistoryId());
        mostRecentQueryHistory.setSavedQuery(savedQuery);
        mostRecentQueryHistory.getQueryHistoryAttributes();

        // Store to the database
        getHibernateTemplate().saveOrUpdate(savedQuery);
        getHibernateTemplate().update(mostRecentQueryHistory);

        savedQueryId = savedQuery.getId();

        logger.info("Saved query record with ID = " + savedQueryId);

        return savedQueryId;
    }
    /**
     * Convenience method to look up a user object based on the login name
     *
     * @param loginName - user's login name
     * @param dataAccess - access to Hibernate
     */
    private NCIAUser getUser(String loginName) {
        // Create the example criteria and run the query
        NCIAUser user = new NCIAUser();
        user.setLoginName(loginName);

        DetachedCriteria crit = DetachedCriteria.forClass(NCIAUser.class);
        crit.add(Example.create(user));

        List result = getHibernateTemplate().findByCriteria(crit);

        if ((result != null) && (result.size() > 0)) {
            return (NCIAUser) result.get(0);
        } else {
            throw new RuntimeException("User not found in getUser():"+loginName);
        }
    }

    /**
     * Converts Criteria objects into QueryHistoryAttributes
     *
     * @param query - the query to get criteria from
     */
    private void addStoredAttributes(PersistentQuery persistentQuery,
                                     DICOMQuery query) {
        // Keep track of how many instances exist of each class of criteria
        // For most, there is only going to be one.  But for a few types
        // (such as CurationDataCriteria) there can be multiple
        Map<String, Integer> criteriaCounts = new HashMap<String, Integer>();

        // Loop through each criteria that will be persisted
        for (PersistentCriteria criteria : query.getPersistentCriteria()) {
            // Determine the criteria instance number to use
            String criteriaClassName = criteria.getClass().getName();
            Integer criteriaCount = criteriaCounts.get(criteriaClassName);

            if (criteriaCount == null) {
                criteriaCount = 0;
            }

            criteriaCounts.put(criteriaClassName, ++criteriaCount);

            // Get the attribute wrapper for each criteria and add to the query history
            for (QueryAttributeWrapper attr : criteria.getQueryAttributes()) {
                persistentQuery.addQueryAttribute(attr, criteriaCount);
            }
        }
    }

    /**
     * Converts stored attributes into criteria
     *
     * @param attributes - set of stored attributes
     * @return a set of criteria
     * @throws Exception
     */
    private Set<gov.nih.nci.ncia.criteria.Criteria> getCriteriaFromStoredAttributes(Set attributes)  {
        // A map to store each instance of saved criteria
        // The key is the concatenation of the class name and the instance number
        // This ensures one entry in the map per instance number and class
        // combination in the saved criteria
        Map<String, PersistentCriteria> map = new HashMap<String, PersistentCriteria>();

        // Loop through the attributess
        Iterator iter = attributes.iterator();

        while (iter.hasNext()) {
            QueryAttribute attribute = (QueryAttribute) iter.next();

            // See if a criteria of the class has already been added to the map
            String className = attribute.getAttributeName();
            int instanceNumber = attribute.getInstanceNumber();

            String key = className + "/" + instanceNumber;

            PersistentCriteria criteria = map.get(key);

            if (criteria == null) {
            	try {
            		// 	If not in the map, create the criteria using reflection
            		criteria = (PersistentCriteria) Class.forName(className)
            		.newInstance();
            		map.put(key, criteria);
            	}
            	catch(Exception e) {
            		throw new RuntimeException(e);
            	}
            }

            // Add the attribute values to the criteria
            criteria.addValueFromQueryAttribute(attribute.getQueryAttributeWrapper());
        }

        // Convert to a Set to return
        return new HashSet<gov.nih.nci.ncia.criteria.Criteria>(map.values());
    }

    /**
     * This will update the SavedQuery with the specified ID to have the new
     * results flag set to the passed in boolean value.
     *
     * @param savedQueryId
     * @param flagValue
     * @throws Exception
     */
    private void setNewResultsFlagForQuery(Long savedQueryId,
                                           boolean flagValue) {

        SavedQuery savedQuery = (SavedQuery) getHibernateTemplate().load(SavedQuery.class,
                                                                         savedQueryId);
        savedQuery.setNewResults(flagValue);

        getHibernateTemplate().saveOrUpdate(savedQuery);
    }

    private List<SavedQueryDTO> populateSavedQueryDTOs(List results) {
	    List<SavedQueryDTO> savedQueries = new ArrayList<SavedQueryDTO>();

	    // Loop through results and build the DTOs
	    Iterator iter = results.iterator();

	    while (iter.hasNext()) {
	        SavedQuery savedQuery = (SavedQuery) iter.next();

	        SavedQueryDTO dto = new SavedQueryDTO();
	        dto.setId(savedQuery.getId());

	        if (savedQuery.getLastExecuteDate() != null) {
	            dto.setExecutionTime(savedQuery.getLastExecuteDate().getLastExecuteDate());
	        }

	        dto.setNewResults(savedQuery.getNewResults());
	        dto.setQueryName(savedQuery.getQueryName());
	        dto.setCriteriaList(getCriteriaFromStoredAttributes(savedQuery.getSavedQueryAttributes()));
	        dto.setUserId(savedQuery.getUserId());

	        savedQueries.add(dto);
	    }

	    return savedQueries;
    }
}
