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
* Revision 1.13  2006/12/13 14:04:14  dietrich
* Grid enhancement
*
* Revision 1.12  2006/09/27 20:46:27  panq
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
import java.util.Iterator;


/**
 * @author Ajay Gupta - NCICB/SAIC
 *
 *
 *
 */
public class SoftwareVersionCriteria extends MultipleValuePersistentCriteria
    implements Serializable {
    private Collection<String> softwareVersionValues;

    public SoftwareVersionCriteria() {
        super();
    }

    /**
     * These #$^#'ing strings are manufacturer, model and version separated by
     * two pipes: ||.  Otherwise it will match nothing.
     */    
    public void setSoftwareVersionObjects(Collection objects) {
        for (Iterator iter = objects.iterator(); iter.hasNext();) {
            Object thisObj = iter.next();

            if (thisObj instanceof String) {
                getCreateSoftwareObjects().add((String) thisObj);
            }
        }
    }

    public Collection getSoftwareVersionObjects() {
        return softwareVersionValues;
    }

    public void setSoftwareVersionValue(String softwareValue) {
        this.getCreateSoftwareObjects().add(softwareValue);
    }

    private Collection<String> getCreateSoftwareObjects() {
        if (softwareVersionValues == null) {
            softwareVersionValues = new ArrayList<String>();
        }

        return softwareVersionValues;
    }

    public void addValueFromQueryAttribute(QueryAttributeWrapper attr) {
        setSoftwareVersionValue(attr.getAttributeValue());
    }

    @Override
    protected Collection<String> getMultipleValues() {
        // TODO Auto-generated method stub
        return softwareVersionValues;
    }

    @Override
    public String getDisplayValue() {
        ArrayList<String> values = new ArrayList<String>();

        for (String s : softwareVersionValues) {
            values.add(s.split("\\|\\|")[2]);
        }

        return values.toString().substring(1, values.toString().length() - 1);
    }
}
