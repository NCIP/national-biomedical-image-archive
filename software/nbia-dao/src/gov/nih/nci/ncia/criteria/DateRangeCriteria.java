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
* Revision 1.1  2007/08/05 21:44:38  bauerd
* Initial Check in of reorganized components
*
* Revision 1.3  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.ncia.criteria;


import gov.nih.nci.nbia.querystorage.QueryAttributeWrapper;
import gov.nih.nci.nbia.util.CrossDatabaseUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;


public class DateRangeCriteria extends PersistentCriteria {
    private static final long serialVersionUID = 1L;
    private Date fromDate;
    private Date toDate;
    private static Logger logger = Logger.getLogger(DateRangeCriteria.class);

    /**
     * @return Returns the fromDate.
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate The fromDate to set.
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return Returns the toDate.
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     * @param toDate The toDate to set.
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    @Override
    public boolean isEmpty() {

        if (fromDate == null && toDate == null) {
        	return true;
        } else {
        	return false;
        }
    }

    @Override
    public String getDisplayValue() {
    	if (fromDate != null && toDate != null){
    		 SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    		return sdf.format(fromDate)+ " ~ " + sdf.format(toDate);
    	}
        return "";
    }

    @Override
    public String getDisplayName() {

        return "Date Available on NCIA";
    }

    public List<QueryAttributeWrapper> getQueryAttributes() {
    	List<QueryAttributeWrapper> attrList = new ArrayList<QueryAttributeWrapper>();
    	String fromDateString = null;
        String toDateString = null;
        int subAttrName = 1;
        SimpleDateFormat sdf = CrossDatabaseUtil.getDatabaseSpecificDatePattern();

		if (fromDate != null) {
		    fromDateString = sdf.format(fromDate);
		}

		if (toDate != null) {
		    toDateString = sdf.format(toDate);
		}
        // Create a wrapper for each of the criteria's values

        QueryAttributeWrapper attr = new QueryAttributeWrapper();
        attr.setCriteriaClassName(getClass().getName());
        attr.setSubAttributeName(String.valueOf(subAttrName++));
        attr.setAttributeValue(fromDateString);
        attrList.add(attr);

        QueryAttributeWrapper attr2 = new QueryAttributeWrapper();
        attr2.setCriteriaClassName(getClass().getName());
        attr2.setSubAttributeName(String.valueOf(subAttrName++));
        attr2.setAttributeValue(toDateString);
        attrList.add(attr2);
        return attrList;
    }

    public void addValueFromQueryAttribute(QueryAttributeWrapper attr) {
    	SimpleDateFormat sdf = null;
    	if( CrossDatabaseUtil.isDateInOracleFormat(attr.getAttributeValue())) {
    		sdf = CrossDatabaseUtil.getOracleDBFormat();
    	}
    	else {
    		sdf = CrossDatabaseUtil.getMySqlDBFormat();
    	}

    	try {
    		if (attr.getSubAttributeName().equals("1")) {
    			fromDate = sdf.parse(attr.getAttributeValue());
    		} else if (attr.getSubAttributeName().equals("2")) {
    			toDate = sdf.parse(attr.getAttributeValue());
    		}
    	}
    	catch (Exception ex) {
    		logger.error("error in parsing date in database", ex);
    	}
    }
}
