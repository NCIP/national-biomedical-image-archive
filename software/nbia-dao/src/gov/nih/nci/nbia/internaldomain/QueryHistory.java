/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id: QueryHistory.java 6028 2008-08-13 19:42:42Z panq $
*
* $Log: not supported by cvs2svn $
* Revision 1.1  2007/08/07 12:05:13  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:38  bauerd
* Initial Check in of reorganized components
*
* Revision 1.3  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
/**
 *
 * copyright
 */
package gov.nih.nci.nbia.internaldomain;

import gov.nih.nci.nbia.querystorage.PersistentQuery;
import gov.nih.nci.nbia.querystorage.QueryAttributeWrapper;
import gov.nih.nci.nbia.security.NCIAUser;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * Domain class to represent a query history record
 * @author  NCIATeam
 */
public class QueryHistory implements Serializable, PersistentQuery {
    private static final long serialVersionUID = 120909944929218045L;

    // primayr key
    private long id;

    // The user that ran the query
    private NCIAUser user;

    // Timestamp of when the query was run
    private Date executeTime;

    // The saved query that was run (if any)
    private SavedQuery savedQuery;

    // How long it took the query to run
    private Long elapsedTime;

    // User ID of the user associated with this query history record
    private long userId;

    // The criteria of the query
    private Set<QueryHistoryAttribute> queryHistoryAttributes;

    public QueryHistory() {
        queryHistoryAttributes = new HashSet<QueryHistoryAttribute>();
    }

    /**
     *
     * @param attr
     */
    public void removeQueryHistoryAttribute(QueryHistoryAttribute attr) {
        queryHistoryAttributes.remove(attr);
    }

    /**
     *
     */
    public Date getExecuteTime() {
        return executeTime;
    }

    /**
     *
     * @param executeTime
     */
    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    /**
     *
     */
    public long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     */
    public Set<QueryHistoryAttribute> getQueryHistoryAttributes() {
        return queryHistoryAttributes;
    }

    /**
     *
     * @param queryAttributes
     */
    public void setQueryHistoryAttributes(
        Set<QueryHistoryAttribute> queryAttributes) {
        this.queryHistoryAttributes = queryAttributes;
    }

    /**
     *
     */
    public NCIAUser getUser() {
        return user;
    }

    /**
     *
     * @param usr
     */
    public void setUser(NCIAUser usr) {
        this.user = usr;
    }

    /**
     *
     */
    public Long getElapsedTime() {
        return elapsedTime;
    }

    /**
     *
     * @param elapsedTime
     */
    public void setElapsedTime(Long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    /**
     *
     */
    public SavedQuery getSavedQuery() {
        return savedQuery;
    }

    /**
     *
     * @param savedQuery
     */
    public void setSavedQuery(SavedQuery savedQuery) {
        this.savedQuery = savedQuery;
    }

    /**
     *
     * @param attr
     */
    public void addQueryAttribute(QueryAttributeWrapper attr, int sequenceNumber) {
        QueryHistoryAttribute qha = new QueryHistoryAttribute(attr, this,
                sequenceNumber);
        queryHistoryAttributes.add(qha);
    }

    /**
     *
     */
    public long getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }
}
