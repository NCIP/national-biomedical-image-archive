/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id: ImageModalityCriteria.java 4417 2008-04-18 20:43:12Z saksass $
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
* Revision 1.12  2006/12/13 14:04:14  dietrich
* Grid enhancement
*
* Revision 1.11  2006/09/27 20:46:27  panq
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
 * @author Qinyan Pan - NCICB/SAIC
 *
 *
 *
 */
public class UsMultiModalityCriteria extends MultipleValuePersistentCriteria
    implements Serializable {
    private Collection<String> usMultiModalityObjects;

    //Constructor
    public UsMultiModalityCriteria() {
    }

    // getters and setters to add moadility values to 
    // Collection
    /**
     * @return Returns the usMultiModalityObjects.
     */
    public Collection<String> getUsMultiModalityObjects() {
        return usMultiModalityObjects;
    }

    protected Collection<String> getMultipleValues() {
        return getUsMultiModalityObjects();
    }

    /**
     * @param imageModalityObjects The imageModalityObjects to set.
     */
    public void setUsMultiModalityObjects(Collection usMultiModalityObjects) {
        for (Iterator iter = usMultiModalityObjects.iterator(); iter.hasNext();) {
            Object thisObj = iter.next();

            if (thisObj instanceof String) {
                getCreateUsMultiModalityObjects().add((String) thisObj);
            }
        }
    }

    private Collection<String> getCreateUsMultiModalityObjects() {
        if (usMultiModalityObjects == null) {
        	usMultiModalityObjects = new ArrayList<String>();
        }
        return usMultiModalityObjects;
    }

    public void setUsMultiModalityValue(String modalityValue) {
        this.getCreateUsMultiModalityObjects().add(modalityValue);
    }

    public void addValueFromQueryAttribute(QueryAttributeWrapper attr) {
        setUsMultiModalityValue(attr.getAttributeValue());
    }

}
