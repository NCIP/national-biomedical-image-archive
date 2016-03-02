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
* Revision 1.16  2006/12/13 14:04:14  dietrich
* Grid enhancement
*
* Revision 1.15  2006/09/27 20:46:27  panq
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
 * @author Prashant Shah/Ajay Gupta - NCICB/SAIC
 *
 *
 *
 */
public class CollectionCriteria extends MultipleValuePersistentCriteria
    implements Serializable {
    private Collection<String> collectionObjects;

    /**
     *
     */
    public CollectionCriteria() {
    }

    /**
     * @return Returns the collectionValue.
     */
    public Collection<String> getCollectionObjects() {
        return collectionObjects;
    }

    protected Collection<String> getMultipleValues() {
        return getCollectionObjects();
    }

    /**
     * 
     */
    public void setCollectionObjects(Collection collectionObjects) {
        for (Iterator iter = collectionObjects.iterator(); iter.hasNext();) {
            Object thisObj = iter.next();

            if (thisObj instanceof String) {
                getCreateCollectionObjects().add((String) thisObj);
            }
        }
    }

    private Collection<String> getCreateCollectionObjects() {
        if (collectionObjects == null) {
            collectionObjects = new ArrayList<String>();
        }

        return collectionObjects;
    }

    public Collection getCollectionValue() {
        return collectionObjects;
    }

    public void setCollectionValue(String collectionValue) {
        this.getCreateCollectionObjects().add(collectionValue);
    }

    public void addValueFromQueryAttribute(QueryAttributeWrapper attr) {
        setCollectionValue(attr.getAttributeValue());
    }

    public void removeCollection(String collectionToRemove) {
        collectionObjects.remove(collectionToRemove);
    }

}
