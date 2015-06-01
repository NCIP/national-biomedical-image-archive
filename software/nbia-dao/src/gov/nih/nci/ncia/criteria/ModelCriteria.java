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
* Revision 1.15  2006/12/13 14:04:14  dietrich
* Grid enhancement
*
* Revision 1.14  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
/*
 * Created on Aug 9, 2005
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
 * @author guptaa
 *
 *
 *
 */
public class ModelCriteria extends MultipleValuePersistentCriteria
    implements Serializable {
    private Collection<String> modelObjects;

    /**
     *
     */
    public ModelCriteria() {
    }

    /**
     * @return Returns the collectionValue.
     */
    public Collection<String> getModelObjects() {
        return modelObjects;
    }

    protected Collection<String> getMultipleValues() {
        return getModelObjects();
    }

    /**
     * These #$^#'ing strings are manufacturer and model separated by
     * two pipes: ||.  Otherwise it will match nothing.
     */
    public void setCollectionObjects(Collection<String> collectionObjects) {
        for (Iterator iter = collectionObjects.iterator(); iter.hasNext();) {
            Object thisObj = iter.next();

            if (thisObj instanceof String) {
                getCreateModelObjects().add((String) thisObj);
            }
        }
    }

    private Collection<String> getCreateModelObjects() {
        if (modelObjects == null) {
            modelObjects = new ArrayList<String>();
        }

        return modelObjects;
    }

    public void setCollectionValue(String collectionValue) {
        this.getCreateModelObjects().add(collectionValue);
    }

    public void addValueFromQueryAttribute(QueryAttributeWrapper attr) {
        setCollectionValue(attr.getAttributeValue());
    }

    @Override
    public String getDisplayValue() {
        ArrayList<String> values = new ArrayList<String>();

        for (String s : modelObjects) {
            values.add(s.split("\\|\\|")[1]);
        }

        return values.toString().substring(1, values.toString().length() - 1);
    }
}
