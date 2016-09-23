/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id: AbstractStoredQueryDTO.java 4417 2008-04-18 20:43:12Z saksass $
*
* $Log: not supported by cvs2svn $
* Revision 1.3  2007/09/27 23:30:39  bauerd
* This is the checked in seperation of dependent classes betweeen the grid and the commons module...
*
* Revision 1.1  2007/08/05 21:52:15  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:48:51  bauerd
* *** empty log message ***
*
* Revision 1.6  2006/09/27 20:46:28  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.dto;

import gov.nih.nci.ncia.criteria.Criteria;
import gov.nih.nci.nbia.util.CriteriaComparator;

import java.text.DateFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;


public abstract class AbstractStoredQueryDTO {
    protected long id;
    protected String queryName;
    protected Date executionTime;
    protected List<Criteria> criteriaList;
    protected boolean showCriteria;
    protected Long userId;

    public String getFormattedDate() {
        if (executionTime == null) {
            return "";
        }

        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                DateFormat.SHORT);
        String date = formatter.format(executionTime);

        return date;
    }

    public List<Criteria> getCriteriaList() {
        Collections.sort(criteriaList, new CriteriaComparator());

        return criteriaList;
    }

    public void setCriteriaList(Set<Criteria> criteriaList) {
        this.criteriaList = new ArrayList<Criteria>(criteriaList);
    }

    public Date getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Date executionTime) {
        this.executionTime = executionTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public abstract String getQueryName();

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public boolean isShowCriteria() {
        return showCriteria;
    }

    public void setShowCriteria(boolean showCriteria) {
        this.showCriteria = showCriteria;
    }

    public int getNumCriteria() {
        return criteriaList.size();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
