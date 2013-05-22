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
* Revision 1.3  2007/08/15 16:49:59  bauerd
* *** empty log message ***
*
* Revision 1.2  2007/08/14 16:53:47  bauerd
* Removed the repopulate logic and cleaned up the class files
*
* Revision 1.1  2007/08/07 12:05:14  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:38  bauerd
* Initial Check in of reorganized components
*
* Revision 1.25  2006/12/13 14:04:14  dietrich
* Grid enhancement
*
* Revision 1.24  2006/09/27 20:46:27  panq
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
 * @author Ajay Gupta - NCICB/SAIC
 *
 *
 *
 */
public class KilovoltagePeakDistribution extends MultipleValuePersistentCriteria
    implements Serializable, RangeCriteria {
    protected RangeData rangeData = new RangeData();

    public KilovoltagePeakDistribution(String kilovoltagePeakDistributionFrom,
        String kilovoltagePeakDistributionFromValue,
        String kilovoltagePeakDistributionTo,
        String kilovoltagePeakDistributionToValue) {
        rangeData.setFromOperator(kilovoltagePeakDistributionFrom);
        rangeData.setFromValue(kilovoltagePeakDistributionFromValue);

        if ((kilovoltagePeakDistributionTo != null) &&
                !kilovoltagePeakDistributionTo.equals("")) {
            rangeData.setToOperator(kilovoltagePeakDistributionTo);
            rangeData.setToValue(Double.parseDouble(
                    kilovoltagePeakDistributionToValue));
        }
    }

    public KilovoltagePeakDistribution() {
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

    protected List<String> getMultipleValues() {
        return rangeData.getMultipleValues();
    }

    public RangeData getRangeData() {
        return rangeData;
    }

    protected Collection<String> getOrderedRangeValues() {
        Collection<String> attrList = new ArrayList<String>();
        attrList.add(rangeData.getFromOperator());
        attrList.add(String.valueOf(rangeData.getFromValue()));

        if (rangeData.getToOperator() != null) {
            attrList.add(rangeData.getToOperator());
            attrList.add(String.valueOf(rangeData.getToValue()));
        }

        return attrList;
    }

}
