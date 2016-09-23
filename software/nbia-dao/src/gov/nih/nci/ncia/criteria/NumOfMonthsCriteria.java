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
* Revision 1.2  2007/08/14 16:53:47  bauerd
* Removed the repopulate logic and cleaned up the class files
*
* Revision 1.1  2007/08/07 12:05:15  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:38  bauerd
* Initial Check in of reorganized components
*
* Revision 1.23  2006/12/13 14:04:14  dietrich
* Grid enhancement
*
* Revision 1.22  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
/*
 * Created on Jul 24, 2005
 *
 *
 *
 */
package gov.nih.nci.ncia.criteria;

import gov.nih.nci.nbia.querystorage.QueryAttributeWrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * @author Prashant Shah - NCICB/SAIC
 *
 *
 *
 */
public class NumOfMonthsCriteria extends MultipleValuePersistentCriteria
    implements Serializable, RangeCriteria {
    protected RangeData rangeData = new RangeData();

    public NumOfMonthsCriteria(String numOfMonthsOperator,
        String numOfMonthsValue) {
        rangeData.setFromOperator(numOfMonthsOperator);
        rangeData.setFromValue(numOfMonthsValue);
    }

    public NumOfMonthsCriteria() {
    }

    public boolean isEmpty() {
        return rangeData.isEmpty();
    }

    public String getDisplayValue() {
        return rangeData.getDisplayValue();
    }

    public void addValueFromQueryAttribute(QueryAttributeWrapper attr) {
        rangeData.addValueFromQueryAttribute(attr);
    }

    public RangeData getRangeData() {
        return rangeData;
    }

    protected Collection<String> getOrderedRangeValues() {
        Collection<String> attrList = new ArrayList<String>();
        attrList.add(rangeData.getFromOperator());
        attrList.add(String.valueOf(getToOperator()));

        return attrList;
    }

    public String getToOperator() {
        throw new RuntimeException("Method not implemented");
    }

    public Integer getToValue() {
        throw new RuntimeException("Method not implemented");
    }

    protected List<String> getMultipleValues() {
        List<String> returnList = new ArrayList<String>();
        returnList.add(0, rangeData.getFromOperator());
        returnList.add(1, String.valueOf(rangeData.getFromValue()));

        return returnList;
    }

}
